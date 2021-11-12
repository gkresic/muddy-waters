package com.steatoda.muddywaters.dolphin;

import java.util.EventObject;

/** Fired when app destroying process has <b>begun</b> */
public class DolphinPreDestroyEvent extends EventObject {

	public DolphinPreDestroyEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
