package com.justlibre.stow4j;

import java.io.Reader;
import java.net.URL;

/**
 * represents a storage item
 */
public interface Item extends Identifiable {
	String name();

	/**
	 * get a URL for this item. it is dependent
	 * on the back-end
	 * local: file:///path/to/something
	 */
	URL url();

	/**
	 * @return the size of the Item's contents in bytes.
	 * @throws StowException
	 */
	long size() throws StowException;
	
	/**
	 * opens the item for reading. The caller must close
	 * the Reader
	 * @return 
	 * @throws StowException
	 */
	Reader open() throws StowException;
}
