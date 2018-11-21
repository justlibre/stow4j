package com.justlibre.stow4j;

public class UnknownKind extends Exception {
	public UnknownKind(String kind) {
		super(
				(kind == null || kind.trim().isEmpty())
				? "stow4j: unknown kind"
			    : "stow4j: unknown kind \"" + kind + "\""
		);
		
	}
}
