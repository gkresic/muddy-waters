package com.steatoda.muddywaters.dolphin;

import com.steatoda.muddywaters.dolphin.vertx.VertxService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.NoSubscriberEvent;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Dolphin {

	@Inject
	@SuppressWarnings("unused")	// extra parameters are injected only to be initialized as soon as possible ("eager singletons")
	public Dolphin(
		EventBus eventBus,
		VertxService vertxService
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
		
		Log.debug("Dolphin {} starting...", DolphinProperties.get().getVersion());

		eventBus.post(new DolphinPreStartEvent(this));

		eventBus.post(new DolphinStartEvent(this));
		
		Log.info("Dolphin {} is up and runnin'", DolphinProperties.get().getVersion());

	}
	
	public void stop() {
		
		Log.debug("Dolphin {} stopping...", DolphinProperties.get().getVersion());

		eventBus.post(new DolphinPreStopEvent(this));

		eventBus.post(new DolphinStopEvent(this));
		
		Log.info("Dolphin {} stopped", DolphinProperties.get().getVersion());

	}
	
	public void destroy() {

		Log.debug("Dolphin {} is initializing shutdown...", DolphinProperties.get().getVersion());

		eventBus.post(new DolphinPreDestroyEvent(this));

		eventBus.post(new DolphinDestroyEvent(this));
		
		Log.info("Dolphin {} successfully shut down - bye!", DolphinProperties.get().getVersion());
		
	}

	private static final Logger Log = LoggerFactory.getLogger(Dolphin.class);

	private final EventBus eventBus;

}
