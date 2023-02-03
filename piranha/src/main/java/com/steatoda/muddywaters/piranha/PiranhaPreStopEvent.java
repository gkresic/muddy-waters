package com.steatoda.muddywaters.piranha;

import java.util.EventObject;

/** Fired when app stopping process has <b>begun</b> */
public class PiranhaPreStopEvent extends EventObject {

	public PiranhaPreStopEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
