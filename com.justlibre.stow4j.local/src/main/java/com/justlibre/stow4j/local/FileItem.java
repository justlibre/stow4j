package com.justlibre.stow4j.local;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.justlibre.stow4j.Item;
import com.justlibre.stow4j.StowException;

public class FileItem implements Item {
	Logger L = LoggerFactory.getLogger(FileItem.class);
	
	private String path;
	
	public FileItem(String path) {
		this.path = path;
	}

	@Override
	public String getId() {
		return path;
	}

	@Override
	public String getName() {
		return (new File(path)).getName();
	}
	
	@Override
	public URL getUrl() {
		URL u = null;
		try {
			u = new URL("file", "", path);
		} catch (MalformedURLException e) {
			L.error("could not construct url for:" + path);
		}
		
		return u;
	}

	@Override
	public long getSize() throws StowException {
		return (new File(path)).length();
	}

	@Override
	public Reader open() throws StowException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(path)));
		} catch (FileNotFoundException e) {
			throw new StowException("file not found " + path, e);
		}
		return br;
	}
}
