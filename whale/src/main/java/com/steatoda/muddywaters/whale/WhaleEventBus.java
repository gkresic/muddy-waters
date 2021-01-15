package com.steatoda.muddywaters.whale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

public class WhaleEventBus extends EventBus {

	public WhaleEventBus() {
		super((exception, context) -> Log.error("Error in event handler ({})", context, exception));
	}

	private static final Logger Log = LoggerFactory.getLogger(WhaleEventBus.class);

}
