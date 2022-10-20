package com.steatoda.muddywaters.beluga;

import java.util.EventObject;

/** Fired when app destroying process has <b>begun</b> */
public class BelugaPreDestroyEvent extends EventObject {

	public BelugaPreDestroyEvent(Object source) {
		super(source);
	}
	
	private static final long serialVersionUID = 1L;

}
