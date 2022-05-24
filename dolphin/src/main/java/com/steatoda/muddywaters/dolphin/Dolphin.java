package com.steatoda.muddywaters.dolphin;

import com.steatoda.muddywaters.dolphin.vertx.RestVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.NoSubscriberEvent;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class Dolphin {

	@Inject
	public Dolphin(EventBus eventBus, Vertx vertx) {
		this.eventBus = eventBus;
		this.vertx = vertx;
		eventBus.register(this);
	}

	/** One dummy method with {@link Subscribe} annotation to make sure annotation processor is triggered */
	@Subscribe
	public void onNoSubscriber(NoSubscriberEvent event) {
		// this event should not be posted, so warn
		Log.warn("No subscribers for {} (but still notified?)", event.originalEvent.getClass());
	}

	public void start() {
		
		Log.debug("Dolphin {} starting...", DolphinProperties.get().getVersion());

		eventBus.post(new DolphinPreStartEvent(this));

		try {
			awaitComplete(CompositeFuture.all(
				Stream.of(
					RestVerticle.deploy(vertx)
				).peek(future -> future.onSuccess(verticeDeploymentIds::add)).collect(Collectors.toList())
			));
		} catch (Exception e) {
			throw new RuntimeException("Error deploying vertice(s)", e);
		}
		Log.info("{} Vert.x vertice(s) deployed", verticeDeploymentIds.size());

		eventBus.post(new DolphinStartEvent(this));
		
		Log.info("Dolphin {} is up and runnin'", DolphinProperties.get().getVersion());

	}
	
	public void stop() {
		
		Log.debug("Dolphin {} stopping...", DolphinProperties.get().getVersion());

		eventBus.post(new DolphinPreStopEvent(this));

		try {
			int verticlesCount = verticeDeploymentIds.size();
			if (verticlesCount > 0)
				awaitComplete(CompositeFuture.all(verticeDeploymentIds.stream().map(vertx::undeploy).collect(Collectors.toList())));
			verticeDeploymentIds.clear();
			Log.info("{} vertices undeployed", verticlesCount);
		} catch (Exception e) {
			Log.error("Error undeploying vertices", e);
		}

		eventBus.post(new DolphinStopEvent(this));
		
		Log.info("Dolphin {} stopped", DolphinProperties.get().getVersion());

	}
	
	public void destroy() {

		Log.debug("Dolphin {} is initializing shutdown...", DolphinProperties.get().getVersion());

		eventBus.post(new DolphinPreDestroyEvent(this));

		try {
			Promise<Void> vertxClosePromise = Promise.promise();
			vertx.close(result -> {
				if (result.succeeded())
					vertxClosePromise.complete(result.result());
				else
					vertxClosePromise.fail(result.cause());
			});
			awaitComplete(vertxClosePromise.future());
			Log.info("Vert.x closed");
		} catch (Exception e) {
			Log.error("Error closing Vert.x", e);
		}

		eventBus.post(new DolphinDestroyEvent(this));
		
		Log.info("Dolphin {} successfully shut down - bye!", DolphinProperties.get().getVersion());
		
	}

	// adopted from https://stackoverflow.com/a/68543780/4553548
	private static <T> T awaitComplete(io.vertx.core.Future<T> future) throws InterruptedException {

		final Object lock = new Object();
		final AtomicReference<AsyncResult<T>> resultRef = new AtomicReference<>(null);

		T result;
		Throwable cause;

		if (future.isComplete()) {
			result = future.result();
			cause = future.cause();
		} else {
			synchronized (lock) {
				// We *must* be locked before registering a callback.
				// If result is ready, the callback is called immediately!
				future.onComplete((AsyncResult<T> result2) -> {
					resultRef.set(result2);
					synchronized (lock) {
						lock.notify();
					}
				});
				while (resultRef.get() == null) {
					// Nested sync on lock is fine.  If we get a spurious wake-up before resultRef is set, we need to reacquire the lock, then wait again.
					// Ref: https://stackoverflow.com/a/249907/257299
					synchronized (lock) {
						// @Blocking
						lock.wait();
					}
				}
			}
			AsyncResult<T> asyncResult = resultRef.get();
			result = asyncResult.result();
			cause = asyncResult.cause();
		}

		if (cause != null)
			throw new RuntimeException("Future failed", cause);

		return result;

	}

	private static final Logger Log = LoggerFactory.getLogger(Dolphin.class);

	private final EventBus eventBus;
	private final Vertx vertx;

	private final Set<String> verticeDeploymentIds = new HashSet<>();

}
