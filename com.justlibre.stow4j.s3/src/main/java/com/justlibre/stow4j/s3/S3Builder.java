package com.justlibre.stow4j.s3;

import java.util.Properties;

import com.justlibre.stow4j.StowBuilder;

public class S3Builder implements StowBuilder {
	private Properties prop = new Properties();
	
	public S3Builder() {
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
