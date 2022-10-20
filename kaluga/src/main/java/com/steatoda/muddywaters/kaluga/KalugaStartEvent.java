package com.steatoda.muddywaters.kaluga;

import java.util.EventObject;

/** Fired <b>after</b> app is started */
public class KalugaStartEvent extends EventObject {

	public KalugaStartEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
