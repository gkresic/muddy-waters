package com.steatoda.muddywaters.dolphin;

import java.util.EventObject;

/** Fired when app stopping process has <b>begun</b> */
public class DolphinPreStopEvent extends EventObject {

	public DolphinPreStopEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
