package com.steatoda.muddywaters.orca;

import java.util.EventObject;

/** Fired when app stopping process has <b>begun</b> */
public class OrcaPreStopEvent extends EventObject {

	public OrcaPreStopEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
