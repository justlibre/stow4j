package com.justlibre.stow4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Stow {
	
	public static String StartCursor = "";
	
	//simple locking, lock on the whole object
	private static Map<String, LocationBuilder> locations = Collections.synchronizedMap(new HashMap<String, LocationBuilder>());
	
	public static Location dial(String kind, Properties conf) throws UnknownKind, ConfigurationException {
		if (!locations.containsKey(kind)) {
			throw new UnknownKind(kind);
		}
		return locations.get(kind).build(conf);
	}
	
	public static void register(String kind, LocationBuilder builder) {
		// if already registered, leave
		if (locations.containsKey(kind)) {
			return;
		}
		
		locations.put(kind, builder);
	}

	public static boolean isStartCursor(String cursor) {
		return cursor.equals(StartCursor);
	}

	public static boolean isCursorEnd(String cursor) {
		return cursor.equals(StartCursor);
	}
}
