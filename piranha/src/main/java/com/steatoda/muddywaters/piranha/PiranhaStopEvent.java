package com.steatoda.muddywaters.piranha;

import java.util.EventObject;

/** Fired <b>after</b> app is stopped */
public class PiranhaStopEvent extends EventObject {

	public PiranhaStopEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
