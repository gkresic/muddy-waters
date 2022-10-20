package com.steatoda.muddywaters.kaluga;

import java.util.EventObject;

/** Fired when app stopping process has <b>begun</b> */
public class KalugaPreStopEvent extends EventObject {

	public KalugaPreStopEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
