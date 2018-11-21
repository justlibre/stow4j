package com.justlibre.stow4j;

import java.io.Reader;
import java.util.List;
import java.util.Map;

/**
 * represents a storage container
 */
public interface Container extends Identifiable {
	/**
	 * @return a human-readable name describing this Container
	 */
	String name();
	
	/**
	 * creates a new Item with the specified name, and contents
	 * read from the reader.
	 */
	Item put(String name, Reader r, long size, Map<String, Object> metadata) throws StowException;
	
	Pair<List<Item>, String> items(String prefix, String cursor, int count) throws StowException;

	Item item(String id) throws StowException;

}
