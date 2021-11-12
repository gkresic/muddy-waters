package com.steatoda.muddywaters.dolphin;

import java.util.EventObject;

/** Fired <b>after</b> app is destroyed */
public class DolphinDestroyEvent extends EventObject {

	public DolphinDestroyEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
