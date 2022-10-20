package com.steatoda.muddywaters.beluga;

import java.util.EventObject;

/** Fired <b>after</b> app is started */
public class BelugaStartEvent extends EventObject {

	public BelugaStartEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
