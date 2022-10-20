package com.steatoda.muddywaters.orca;

import java.util.EventObject;

/** Fired when app starting process has <b>begun</b> */
public class OrcaPreStartEvent extends EventObject {

	public OrcaPreStartEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
