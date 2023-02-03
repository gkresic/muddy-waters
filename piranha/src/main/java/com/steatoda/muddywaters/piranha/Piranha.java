package com.steatoda.muddywaters.piranha;

import com.steatoda.muddywaters.piranha.servlet.ExternalJettyServer;
import com.steatoda.muddywaters.piranha.servlet.JettyService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.NoSubscriberEvent;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Piranha {

	@Inject
	@SuppressWarnings("unused")	// extra parameters are injected only to be initialized as soon as possible ("eager singletons")
	public Piranha(
		EventBus eventBus,
		JettyService jettyService,
		ExternalJettyServer pippoServer
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
		
		Log.debug("Piranha {} starting...", PiranhaProperties.get().getVersion());

		eventBus.post(new PiranhaPreStartEvent(this));

		eventBus.post(new PiranhaStartEvent(this));
		
		Log.info("Piranha {} is up and runnin'", PiranhaProperties.get().getVersion());

	}
	
	public void stop() {
		
		Log.debug("Piranha {} stopping...", PiranhaProperties.get().getVersion());

		eventBus.post(new PiranhaPreStopEvent(this));

		eventBus.post(new PiranhaStopEvent(this));
		
		Log.info("Piranha {} stopped", PiranhaProperties.get().getVersion());

	}
	
	public void destroy() {

		Log.debug("Piranha {} is initializing shutdown...", PiranhaProperties.get().getVersion());

		eventBus.post(new PiranhaPreDestroyEvent(this));

		eventBus.post(new PiranhaDestroyEvent(this));
		
		Log.info("Piranha {} successfully shut down - bye!", PiranhaProperties.get().getVersion());
		
	}

	private static final Logger Log = LoggerFactory.getLogger(Piranha.class);

	private final EventBus eventBus;

}
