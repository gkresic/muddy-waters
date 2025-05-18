package com.steatoda.muddywaters.beluga;

import com.steatoda.muddywaters.beluga.servlet.JavalinService;
import io.javalin.Javalin;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.NoSubscriberEvent;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Beluga {

	@Inject
	@SuppressWarnings("unused")	// extra parameters are injected only to be initialized as soon as possible ("eager singletons")
	public Beluga(
		EventBus eventBus,
		JavalinService javalinService,
		Javalin javalin
	) {
		this.eventBus = eventBus;
		eventBus.register(this);
	}

	/** One dummy method with {@link Subscribe} annotation to make sure annotation processor is triggered */
	@Subscribe
	public void onNoSubscriber(NoSubscriberEvent event) {
		// this event should not be posted, so warn
		Log.warn("No subscribers for {} (but still notified?)", event.originalEvent.getClass());
	}

	public void start() {
		
		Log.debug("Beluga {} starting...", BelugaProperties.get().getVersion());

		eventBus.post(new BelugaPreStartEvent(this));

		eventBus.post(new BelugaStartEvent(this));
		
		Log.info("Beluga {} is up and runnin'", BelugaProperties.get().getVersion());

	}
	
	public void stop() {
		
		Log.debug("Beluga {} stopping...", BelugaProperties.get().getVersion());

		eventBus.post(new BelugaPreStopEvent(this));

		eventBus.post(new BelugaStopEvent(this));
		
		Log.info("Beluga {} stopped", BelugaProperties.get().getVersion());

	}
	
	public void destroy() {

		Log.debug("Beluga {} is initializing shutdown...", BelugaProperties.get().getVersion());

		eventBus.post(new BelugaPreDestroyEvent(this));

		eventBus.post(new BelugaDestroyEvent(this));
		
		Log.info("Beluga {} successfully shut down - bye!", BelugaProperties.get().getVersion());
		
	}

	private static final Logger Log = LoggerFactory.getLogger(Beluga.class);

	private final EventBus eventBus;

}
