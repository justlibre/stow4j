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
	String getName();
	
	/**
	 * creates a new Item with the specified name, and contents
	 * read from the reader.
	 */
	Item put(String name, Reader r, long size, Map<String, Object> metadata) throws StowException;
	
	/**
	 * returns a paged list of items in the container. Item names can be filtered by prefix
	 * the cursor can be reused in subsequent calls of the same method.
	 * 
	 * @param prefix
	 * @param cursor
	 * @param count
	 * @return
	 * @throws StowException
	 */
	Pair<List<Item>, String> getItems(String prefix, String cursor, int count) throws StowException;

	/**
	 * retrieves an item by its id
	 * @param id
	 * @return
	 * @throws StowException
	 */
	Item getItem(String id) throws StowException;

}
