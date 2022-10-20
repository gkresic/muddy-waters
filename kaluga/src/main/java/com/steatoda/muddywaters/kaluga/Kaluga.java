package com.steatoda.muddywaters.kaluga;

import com.steatoda.muddywaters.kaluga.helidon.HelidonService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.NoSubscriberEvent;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Kaluga {

	@Inject
	@SuppressWarnings("unused")	// extra parameters are injected only to be initialized as soon as possible ("eager singletons")
	public Kaluga(
		EventBus eventBus,
		HelidonService helidonService
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
		
		Log.debug("Kaluga {} starting...", KalugaProperties.get().getVersion());

		eventBus.post(new KalugaPreStartEvent(this));

		eventBus.post(new KalugaStartEvent(this));
		
		Log.info("Kaluga {} is up and runnin'", KalugaProperties.get().getVersion());

	}
	
	public void stop() {
		
		Log.debug("Kaluga {} stopping...", KalugaProperties.get().getVersion());

		eventBus.post(new KalugaPreStopEvent(this));

		eventBus.post(new KalugaStopEvent(this));
		
		Log.info("Kaluga {} stopped", KalugaProperties.get().getVersion());

	}
	
	public void destroy() {

		Log.debug("Kaluga {} is initializing shutdown...", KalugaProperties.get().getVersion());

		eventBus.post(new KalugaPreDestroyEvent(this));

		eventBus.post(new KalugaDestroyEvent(this));
		
		Log.info("Kaluga {} successfully shut down - bye!", KalugaProperties.get().getVersion());
		
	}

	private static final Logger Log = LoggerFactory.getLogger(Kaluga.class);

	private final EventBus eventBus;

}
