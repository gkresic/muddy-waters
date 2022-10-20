package com.steatoda.muddywaters.kaluga;

import java.util.EventObject;

/** Fired <b>after</b> app is destroyed */
public class KalugaDestroyEvent extends EventObject {

	public KalugaDestroyEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
