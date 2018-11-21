package com.justlibre.stow4j.local;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.justlibre.stow4j.Container;
import com.justlibre.stow4j.Location;
import com.justlibre.stow4j.NotFoundException;
import com.justlibre.stow4j.Pair;
import com.justlibre.stow4j.StowException;

class FileLocation implements Location {
	private String path;
	
	public FileLocation(String path) {
		this.path = path;
	}

	@Override
	public void close() throws IOException {
		// does not do anything

	}

	@Override
	public Container createContainer(String name) throws StowException {
		File f = new File(path, name);
		if (!f.mkdir()) {
			throw new StowException("could not create container " + name);
		}
		return new FileContainer(name, f.getAbsolutePath());
	}

	@Override
	public Container getContainer(String id) throws StowException {
		List<Container> containers = filesToContainers(path, id);
		if (containers.size() != 1) {
			throw new NotFoundException(id);
		}
		return containers.get(0);
	}
	
	@Override
	public Pair<List<Container>, String> containers(String prefix, String cursor, int count) throws StowException {
		File dir = new File(path);
		final String okp = prefix == null?"":prefix;
		String[] foundFiles = dir.list(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return prefix.isEmpty()?true:name.startsWith(okp);
		    }
		});
		Arrays.sort(foundFiles);

		List<Container> all = filesToContainers(path, foundFiles);
		Pair<List<Container>, String> ret = Paging.page(all, cursor, count);
		
		return ret;
	}
	
	

	@Override
	public boolean removeContainer(String name) throws StowException {
		try {
			FileUtils.deleteDirectory(new File(path, name));
		} catch (IOException e) {
			throw new StowException("could not delete container" + name, e);
		}
		return true;
	}
	
	/**
	 * takes a list of fileNames and turns it into a container list
	 * @param base
	 * @param names
	 * @return
	 */
	private List<Container> filesToContainers(String base, String... names) {
		List<Container> cont = new ArrayList<Container>();
		
		Path rootp = Paths.get(base);
		for (String name: names) {
			Path fp = rootp.resolve(name);
			Path relp = rootp.relativize(fp);
			if (fp.toFile().isDirectory()) {
				cont.add(new FileContainer(relp.toString(), fp.toAbsolutePath().toString()));
			}
		}
		
		return cont;
	}
}
