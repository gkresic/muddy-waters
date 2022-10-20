package com.steatoda.muddywaters.kaluga;

import java.util.EventObject;

/** Fired <b>after</b> app is stopped */
public class KalugaStopEvent extends EventObject {

	public KalugaStopEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
