package com.steatoda.muddywaters.beluga;

import java.util.EventObject;

/** Fired <b>after</b> app is destroyed */
public class BelugaDestroyEvent extends EventObject {

	public BelugaDestroyEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
