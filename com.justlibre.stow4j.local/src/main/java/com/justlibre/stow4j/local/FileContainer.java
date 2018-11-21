package com.justlibre.stow4j.local;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.justlibre.stow4j.Container;
import com.justlibre.stow4j.Item;
import com.justlibre.stow4j.Pair;
import com.justlibre.stow4j.StowException;

class FileContainer implements Container {
	private String name, path;
	
	public FileContainer(String name, String path) {
		this.name = name;
		this.path = path;
	}

	@Override
	public String id() {
		return path;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public Item put(String name, Reader r, long size, Map<String, Object> metadata) throws StowException {
		if (metadata != null && metadata.size() > 0) {
			throw new StowException("metadata is not supported");
		}
		
		File f = new File(path, name);
		f.getParentFile().mkdirs();
		Item ret = new FileItem(f.getAbsolutePath());
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(f);
			long ws = IOUtils.copyLarge(r, fw);
			
			if (ws != size) {
				throw new StowException("bad size at write");
			}
		} catch (IOException e) {
			throw new StowException("while writing to " + name, e);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					//ignore
				}
			}
		}
		return ret;
	}

	@Override
	public Pair<List<Item>, String> items(String prefix, String cursor, int count) throws StowException {
		IOFileFilter fileFilter = prefix.isEmpty()?TrueFileFilter.INSTANCE:FileFilterUtils.prefixFileFilter(prefix);
		List<File> files = (List<File>) FileUtils.listFiles(new File(path), fileFilter, TrueFileFilter.INSTANCE);
		
		Collections.sort(files);
		List<Item> all = filesToItems(path, files);
		Pair<List<Item>, String> ret = Paging.page(all, cursor, count);
		
		return ret;
	}
	
	@Override
	public Item item(String id) throws StowException {
		File f = new File(id);
		if (f.isDirectory()) {
			throw new StowException("unexpected directory");
		}
		if (!f.exists()) {
			throw new StowException("not found");
		}
		return new FileItem(id);
	}
	
	private List<Item> filesToItems(String path, List<File> files) {
		List<Item> itms = new ArrayList<>();
		for (File f: files) {
			itms.add(new FileItem(f.getAbsolutePath()));
		}
		return itms;
	}
}
