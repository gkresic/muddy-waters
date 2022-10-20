package com.steatoda.muddywaters.orca;

import java.util.EventObject;

/** Fired when app destroying process has <b>begun</b> */
public class OrcaPreDestroyEvent extends EventObject {

	public OrcaPreDestroyEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
