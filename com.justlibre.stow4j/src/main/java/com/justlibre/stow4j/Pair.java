package com.justlibre.stow4j;

public class Pair<U,V> {
	private U left;
	private V right;
	
	public Pair(U left, V right) {
		this.left = left;
		this.right = right;
	}
	
	public U left() {
		return left;
	}
	
	public V right() {
		return right;
	}
}
