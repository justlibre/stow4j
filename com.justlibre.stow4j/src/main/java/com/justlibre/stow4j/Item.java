package com.justlibre.stow4j;

import java.io.Reader;
import java.net.URL;

/**
 * represents a storage item
 */
public interface Item extends Identifiable {
	/**
	 * item's name
	 * @return
	 */
	String getName();

	/**
	 * get a URL for this item. it is dependent
	 * on the back-end
	 * local: file:///path/to/something
	 */
	URL getUrl();

	/**
	 * @return the size of the Item's contents in bytes.
	 * @throws StowException
	 */
	long getSize() throws StowException;
	
	/**
	 * opens the item for reading. The caller must close
	 * the Reader
	 * @return 
	 * @throws StowException
	 */
	Reader open() throws StowException;
}
