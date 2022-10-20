package com.steatoda.muddywaters.orca;

import java.util.EventObject;

/** Fired <b>after</b> app is started */
public class OrcaStartEvent extends EventObject {

	public OrcaStartEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
