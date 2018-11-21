package com.justlibre.stow4j.local;

import java.io.File;

import com.justlibre.stow4j.ConfigurationException;
import com.justlibre.stow4j.LocationBuilder;
import com.justlibre.stow4j.Stow;

public class LocalStow {

	public static final String PATH = "pathKey";
	
	private static LocationBuilder builder = conf -> {
		String path = conf.getProperty(PATH);
		if (path == null) {
			throw new ConfigurationException("stow.local: configuration path is missing");
		}
		File f = new File(path);
		if (!f.isDirectory()) {
			throw new ConfigurationException("stow.local: \""+ path + "\" must exist and must be a folder");
		}
		
		return new FileLocation(path);
	};
	
	public static void register() {
		Stow.register("local", builder);
	}
}
