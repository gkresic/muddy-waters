package com.steatoda.muddywaters.piranha;

import java.util.EventObject;

/** Fired when app starting process has <b>begun</b> */
public class PiranhaPreStartEvent extends EventObject {

	public PiranhaPreStartEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
