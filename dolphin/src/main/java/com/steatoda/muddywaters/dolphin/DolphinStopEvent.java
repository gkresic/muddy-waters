package com.steatoda.muddywaters.dolphin;

import java.util.EventObject;

/** Fired <b>after</b> app is stopped */
public class DolphinStopEvent extends EventObject {

	public DolphinStopEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
