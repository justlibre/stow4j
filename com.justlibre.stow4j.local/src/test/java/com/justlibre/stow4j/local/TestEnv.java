package com.justlibre.stow4j.local;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

public class TestEnv {
	public static String setup() throws Exception {
		String rootPath =  Files.createTempDirectory("stow.test-local").toString();
		File root = new File(rootPath);
		File f;
		boolean ok = true;
		
		// add some "containers"
		f = new File(root, "a1");
		ok = f.mkdir();
		if (!ok) {
			throw new IllegalStateException("could not create folder " + f);
		}
		
		f = new File(root, "a2");
		ok = f.mkdir();
		if (!ok) {
			throw new IllegalStateException("could not create folder " + f);
		}
		
		f = new File(root, "dir1");
		ok = f.mkdir();
		if (!ok) {
			throw new IllegalStateException("could not create folder " + f);
		}
		
		f = new File(root, "dir2");
		ok = f.mkdir();
		if (!ok) {
			throw new IllegalStateException("could not create folder " + f);
		}
		
		f = new File(root, "dir3");
		ok = f.mkdir();
		if (!ok) {
			throw new IllegalStateException("could not create folder " + f);
		}
		
		f = new File(f, "deep");
		ok = f.mkdir();
		if (!ok) {
			throw new IllegalStateException("could not create folder " + f);
		}
		
		
		// add three items
		Files.write(Paths.get(rootPath, "dir3", "item1"), "3.1".getBytes());
		Files.write(Paths.get(rootPath, "dir3", "item2"), "3.2".getBytes());
		Files.write(Paths.get(rootPath, "dir3", "item3"), "3.3".getBytes());
		
		// add deep items
		Files.write(Paths.get(rootPath, "dir3", "deep", "deepitem1"), "3.deep.1".getBytes());
		Files.write(Paths.get(rootPath, "dir3", "deep", "deepitem2"), "3.deep.2".getBytes());
		Files.write(Paths.get(rootPath, "dir3", "deep", "deepitem3"), "3.deep.3".getBytes());
		
		
		return rootPath;
	}
	
	public static void tearDown(String base) throws Exception {
		FileUtils.deleteDirectory(new File(base));
	}
}
