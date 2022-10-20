package com.steatoda.muddywaters.beluga;

import java.util.EventObject;

/** Fired when app starting process has <b>begun</b> */
public class BelugaPreStartEvent extends EventObject {

	public BelugaPreStartEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
