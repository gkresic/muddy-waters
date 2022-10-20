package com.steatoda.muddywaters.kaluga;

import java.util.EventObject;

/** Fired when app starting process has <b>begun</b> */
public class KalugaPreStartEvent extends EventObject {

	public KalugaPreStartEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
