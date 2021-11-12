package com.steatoda.muddywaters.dolphin;

import java.util.EventObject;

/** Fired <b>after</b> app is started */
public class DolphinStartEvent extends EventObject {

	public DolphinStartEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
