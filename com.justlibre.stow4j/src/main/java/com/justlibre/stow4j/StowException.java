package com.justlibre.stow4j;

public class StowException extends Exception {
	public StowException(String msg) {
		super(msg);
	}
	
	public StowException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
