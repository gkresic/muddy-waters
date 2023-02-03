package com.steatoda.muddywaters.piranha;

import java.util.EventObject;

/** Fired when app destroying process has <b>begun</b> */
public class PiranhaPreDestroyEvent extends EventObject {

	public PiranhaPreDestroyEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
