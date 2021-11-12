package com.steatoda.muddywaters.dolphin;

import java.util.EventObject;

/** Fired when app starting process has <b>begun</b> */
public class DolphinPreStartEvent extends EventObject {

	public DolphinPreStartEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
