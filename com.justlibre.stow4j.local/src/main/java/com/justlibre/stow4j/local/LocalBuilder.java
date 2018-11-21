package com.justlibre.stow4j.local;

import java.util.Properties;

import com.justlibre.stow4j.StowBuilder;

public class LocalBuilder implements StowBuilder {
	public static final String PATH = "pathKey";
	
	private Properties prop = new Properties();
	
	public LocalBuilder(String path) {
		withKey(PATH, path);
	}
	
	@Override
	public StowBuilder withKey(String key, String value) {
		prop.put(key, value);
		return this;
	}
	
	public Properties build() {
		return prop;
	}
}
