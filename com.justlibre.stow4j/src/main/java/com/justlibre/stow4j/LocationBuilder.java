package com.justlibre.stow4j;

import java.util.Properties;

@FunctionalInterface
public interface LocationBuilder {
	Location build(Properties conf) throws ConfigurationException;
}
