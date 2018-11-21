package com.justlibre.stow4j;

import java.io.Closeable;
import java.net.URL;
import java.util.List;

/**
 * Location represents a storage location.
 *
 */
public interface Location extends Closeable {
	/**
	 * createContainer creates a new Container with the
	 * specified name.
	 * 
	 * @param string the name of the container
	 * @return the new container
	 * @throws StowException 
	 */
	Container createContainer(String name) throws StowException;
	
	/**
	 * gets the container with the specified name
	 * 
	 * @param name container name
	 * @return the container
	 * @throws StowException 
	 */
	Container getContainer(String name) throws StowException;
	
	/**
	 * remove the container with the specified name
	 * 
	 * @param name
	 * @return true if container was removed
	 * @throws StowException 
	 */
	boolean removeContainer(String name) throws StowException;
	
	/**
	 *  Containers gets a page of containers
	 * with the specified prefix from this Location.
	 * The specified cursor is a pointer to the start of
	 * the containers to get. It it obtained from a previous
	 * call to this method, or should be CursorStart for the
	 * first page.
	 * count is the number of items to return per page.
	 * The returned cursor can be checked with IsCursorEnd to
	 * decide if there are any more items or not.
	 * 
	 * @param prefix the prefix
	 * @param cursor the cursor
	 * @param count items per page
	 * @return pair {containers, new cursor}
	 * @throws StowException if the cursor is not ok
	 */
	Pair<List<Container>, String> containers(String prefix, String cursor, int count) throws StowException;
	
	/**
	 * ItemByURL gets an Item at this location with the
	 * specified URL.
	 */ 
	Item itemByURL(URL url) throws StowException;
}
