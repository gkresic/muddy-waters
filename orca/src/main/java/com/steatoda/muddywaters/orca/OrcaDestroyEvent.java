package com.steatoda.muddywaters.orca;

import java.util.EventObject;

/** Fired <b>after</b> app is destroyed */
public class OrcaDestroyEvent extends EventObject {

	public OrcaDestroyEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
