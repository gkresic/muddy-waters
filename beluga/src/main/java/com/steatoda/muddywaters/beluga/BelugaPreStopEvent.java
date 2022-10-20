package com.steatoda.muddywaters.beluga;

import java.util.EventObject;

/** Fired when app stopping process has <b>begun</b> */
public class BelugaPreStopEvent extends EventObject {

	public BelugaPreStopEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
