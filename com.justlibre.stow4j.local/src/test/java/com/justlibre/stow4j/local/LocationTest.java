package com.justlibre.stow4j.local;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.justlibre.stow4j.Container;
import com.justlibre.stow4j.Item;
import com.justlibre.stow4j.Location;
import com.justlibre.stow4j.Pair;
import com.justlibre.stow4j.Stow;

public class LocationTest {
	String base;
	
	@Before
	public void setUp() throws Exception {
		LocalStow.register();
		base = TestEnv.setup();
	}

	@After
	public void tearDown() throws Exception {
		TestEnv.tearDown(base);
	}

	@Test
	public void testReRegistration() throws Exception {
		//already registered in setup
		LocalStow.register();
	}
	
	@Test
	public void testDial() throws Exception {
		Properties conf = new LocalBuilder(base).build();

		Location location = Stow.dial("local", conf);
		assertTrue("created location", location != null);
	}
	
	@Test
	public void testContainers() throws Exception {
		Properties conf = new LocalBuilder(base).build();

		Location location = Stow.dial("local", conf);
		
		Pair<List<Container>, String> ret = location.getContainers("", Stow.StartCursor, 10);
		assertTrue(Stow.isCursorEnd(ret.right()));
		
		List<Container> cs = ret.left();
		assertTrue("size must match", cs.size() == 5);
		
		assertEquals("a1", cs.get(0).getName());
		assertEquals("a2", cs.get(1).getName());
		assertEquals("dir1", cs.get(2).getName());
		assertEquals("dir2", cs.get(3).getName());
		assertEquals("dir3", cs.get(4).getName());
		
		assertTrue(isDir(cs.get(0).getId()));
		assertTrue(isDir(cs.get(1).getId()));
		assertTrue(isDir(cs.get(2).getId()));
		assertTrue(isDir(cs.get(3).getId()));
		assertTrue(isDir(cs.get(4).getId()));
	}
	
	@Test
	public void testPrefix() throws Exception {
		Properties conf = new LocalBuilder(base).build();

		Location location = Stow.dial("local", conf);
		
		Pair<List<Container>, String> ret = location.getContainers("d", Stow.StartCursor, 10);
		assertTrue(Stow.isCursorEnd(ret.right()));
		
		List<Container> cs = ret.left();
		assertTrue("size must match", cs.size() == 3);
		
		assertEquals("dir1", cs.get(0).getName());
		assertEquals("dir2", cs.get(1).getName());
		assertEquals("dir3", cs.get(2).getName());
		
		assertTrue(isDir(cs.get(0).getId()));
		assertTrue(isDir(cs.get(1).getId()));
		assertTrue(isDir(cs.get(2).getId()));
	}
	
	@Test
	public void testRetrieve() throws Exception {
		Properties conf = new LocalBuilder(base).build();

		Location location = Stow.dial("local", conf);
		
		Pair<List<Container>, String> ret = location.getContainers("d", Stow.StartCursor, 10);
		assertTrue(Stow.isCursorEnd(ret.right()));
		
		List<Container> cs = ret.left();
		assertTrue("size must match", cs.size() == 3);
		
		assertEquals("dir1", cs.get(0).getName());
		
		Container retrieved = location.getContainer(cs.get(0).getId());
		assertEquals("dir1", retrieved.getName());
	}
	
	@Test
	public void testCreate() throws Exception {
		Properties conf = new LocalBuilder(base).build();

		Location location = Stow.dial("local", conf);
		location.createContainer("cucurigu");
		
		Pair<List<Container>, String> ret = location.getContainers("cucu", Stow.StartCursor, 10);
		
		List<Container> cs = ret.left();
		assertTrue("size must match", cs.size() == 1);
		
		assertEquals("cucurigu", cs.get(0).getName());
		
	}
	
	@Test
	public void testPaging() throws Exception {
		Properties conf = new LocalBuilder(base).build();

		Location location = Stow.dial("local", conf);
		
		for (int i = 0; i < 25; i++) {
			location.createContainer(String.format("container-%02d", i));
		}
		
		Pair<List<Container>, String> ret;
		String cursor = Stow.StartCursor;
		
		// get first page
		ret = location.getContainers("container-", cursor, 10);
		assertEquals("full page (10)", 10, ret.left().size());
		assertEquals("cursor", "container-10", (new File(ret.right())).getName());
		cursor = ret.right();
		
		// get second page
		ret = location.getContainers("container-", cursor, 10);
		assertEquals("full page (10)", 10, ret.left().size());
		assertEquals("cursor", "container-20", (new File(ret.right())).getName());
		cursor = ret.right();
				
		// get last page
		ret = location.getContainers("container-", cursor, 10);
		assertEquals("reminder", 5, ret.left().size());
		assertTrue("no more", Stow.isCursorEnd(ret.right()));
		cursor = ret.right();
	}
	
	@Test
	public void testAsUrl() throws Exception {
		Properties conf = new LocalBuilder(base).build();

		Location location = Stow.dial("local", conf);
		Container c = location.getContainer("dir3");
		Pair<List<Item>, String> ret = c.getItems("deepitem", "", 1);
		Item itOrig = ret.left().get(0);
		
		Item itFound = location.getItemByURL(itOrig.getUrl());
		
		assertTrue(itFound != null);
		assertEquals(itOrig.getName(), itFound.getName());
		assertEquals(itOrig.getId(), itFound.getId());

	}
	
	private boolean isDir(String path) {
		File f = new File(path);
		return f.isDirectory();
	}
}
