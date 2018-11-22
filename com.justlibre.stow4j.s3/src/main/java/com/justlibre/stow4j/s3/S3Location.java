package com.justlibre.stow4j.s3;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.justlibre.stow4j.Container;
import com.justlibre.stow4j.Item;
import com.justlibre.stow4j.Location;
import com.justlibre.stow4j.Pair;
import com.justlibre.stow4j.StowException;

class S3Location implements Location {
	
	
	public S3Location(String key, String secret, String endpoint) {
		//not implemented
		throw new UnsupportedOperationException();
	}

	@Override
	public void close() throws IOException {
		// does not do anything

	}

	@Override
	public Container createContainer(String name) throws StowException {
		//not implemented
		throw new UnsupportedOperationException();
	}

	@Override
	public Container getContainer(String id) throws StowException {
		//not implemented
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Pair<List<Container>, String> getContainers(String prefix, String cursor, int count) throws StowException {
		//not implemented
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeContainer(String name) throws StowException {
		//not implemented
		throw new UnsupportedOperationException();
	}
	

	@Override
	public Item getItemByURL(URL url) throws StowException {
		//not implemented
		throw new UnsupportedOperationException();
	}
}
