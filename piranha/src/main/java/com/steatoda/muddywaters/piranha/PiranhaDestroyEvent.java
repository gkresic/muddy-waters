package com.steatoda.muddywaters.piranha;

import java.util.EventObject;

/** Fired <b>after</b> app is destroyed */
public class PiranhaDestroyEvent extends EventObject {

	public PiranhaDestroyEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
