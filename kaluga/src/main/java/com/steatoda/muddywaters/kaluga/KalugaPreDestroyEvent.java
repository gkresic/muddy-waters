package com.steatoda.muddywaters.kaluga;

import java.util.EventObject;

/** Fired when app destroying process has <b>begun</b> */
public class KalugaPreDestroyEvent extends EventObject {

	public KalugaPreDestroyEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
