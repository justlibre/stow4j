package com.justlibre.stow4j;

public class NotFoundException extends StowException {
	public NotFoundException(String path) {
		super(path);
	}
}
