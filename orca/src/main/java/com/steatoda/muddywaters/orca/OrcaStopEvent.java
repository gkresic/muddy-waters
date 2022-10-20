package com.steatoda.muddywaters.orca;

import java.util.EventObject;

/** Fired <b>after</b> app is stopped */
public class OrcaStopEvent extends EventObject {

	public OrcaStopEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
