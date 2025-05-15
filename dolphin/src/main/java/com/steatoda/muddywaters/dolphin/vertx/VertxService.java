package com.steatoda.muddywaters.dolphin.vertx;

import com.steatoda.muddywaters.dolphin.DolphinDestroyEvent;
import com.steatoda.muddywaters.dolphin.DolphinPreStartEvent;
import com.steatoda.muddywaters.dolphin.DolphinPreStopEvent;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * <p>Wraps {@link Vertx}, manages its lifecycle and makes sure it is closed on app shutdown.</p>
 */
@Singleton
public class VertxService {

	@Inject
	public VertxService(Vertx vertx, EventBus eventBus) {
		this.vertx = vertx;
		eventBus.register(this);
	}

	@Subscribe
	@SuppressWarnings("unused")
	public void onDolphinPreStart(DolphinPreStartEvent event) {

		try {
			awaitComplete(Future.all(
				Stream.of(
					RestVerticle.deploy(vertx)
				)
				.peek(future -> future.onSuccess(verticeDeploymentIds::add))
				.toList()
			));
		} catch (Exception e) {
			throw new RuntimeException("Error deploying vertice(s)", e);
		}

		Log.info("{} Vert.x vertice(s) deployed", verticeDeploymentIds.size());

	}

	@Subscribe
	@SuppressWarnings("unused")
	public void onDolphinPreStop(DolphinPreStopEvent event) {

		int verticlesCount = verticeDeploymentIds.size();

		if (verticlesCount > 0) {
			try {
				awaitComplete(Future.all(verticeDeploymentIds.stream().map(vertx::undeploy).toList()));
				verticeDeploymentIds.clear();
			} catch (Exception e) {
				Log.error("Error undeploying vertices", e);
			}
		}

		Log.info("{} vertices undeployed", verticlesCount);

	}

	@Subscribe
	@SuppressWarnings("unused")
	public void onDolphinDestroy(DolphinDestroyEvent event) {

		try {
			awaitComplete(vertx.close());
		} catch (Exception e) {
			Log.error("Error closing Vert.x", e);
		}

		Log.info("Vert.x closed");

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

	private static final Logger Log = LoggerFactory.getLogger(VertxService.class);

	private final Vertx vertx;

	private final Set<String> verticeDeploymentIds = new HashSet<>();

}
