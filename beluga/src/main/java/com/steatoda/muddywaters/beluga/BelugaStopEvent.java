package com.steatoda.muddywaters.beluga;

import java.util.EventObject;

/** Fired <b>after</b> app is stopped */
public class BelugaStopEvent extends EventObject {

	public BelugaStopEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
