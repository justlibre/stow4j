package com.justlibre.stow4j.s3;

import com.justlibre.stow4j.LocationBuilder;
import com.justlibre.stow4j.Stow;

public class S3Stow {

	public static final String KEY = "key";
	public static final String SECRET = "secret";
	
	private static LocationBuilder builder = conf -> {
		//not implemented
		throw new UnsupportedOperationException();
	};
	
	public static void register() {
		Stow.register("s3", builder);
	}
}
