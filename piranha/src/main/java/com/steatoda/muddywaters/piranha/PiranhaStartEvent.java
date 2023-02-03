package com.steatoda.muddywaters.piranha;

import java.util.EventObject;

/** Fired <b>after</b> app is started */
public class PiranhaStartEvent extends EventObject {

	public PiranhaStartEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
