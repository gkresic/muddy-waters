package com.steatoda.muddywaters.orca;

import com.steatoda.muddywaters.orca.spark.SparkService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.NoSubscriberEvent;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Orca {

	@Inject
	@SuppressWarnings("unused")	// extra parameters are injected only to be initialized as soon as possible ("eager singletons")
	public Orca(
		EventBus eventBus,
		SparkService sparkService
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
		
		Log.debug("Orca {} starting...", OrcaProperties.get().getVersion());

		eventBus.post(new OrcaPreStartEvent(this));

		eventBus.post(new OrcaStartEvent(this));
		
		Log.info("Orca {} is up and runnin'", OrcaProperties.get().getVersion());

	}
	
	public void stop() {
		
		Log.debug("Orca {} stopping...", OrcaProperties.get().getVersion());

		eventBus.post(new OrcaPreStopEvent(this));

		eventBus.post(new OrcaStopEvent(this));
		
		Log.info("Orca {} stopped", OrcaProperties.get().getVersion());

	}
	
	public void destroy() {

		Log.debug("Orca {} is initializing shutdown...", OrcaProperties.get().getVersion());

		eventBus.post(new OrcaPreDestroyEvent(this));

		eventBus.post(new OrcaDestroyEvent(this));
		
		Log.info("Orca {} successfully shut down - bye!", OrcaProperties.get().getVersion());
		
	}

	private static final Logger Log = LoggerFactory.getLogger(Orca.class);

	private final EventBus eventBus;

}
