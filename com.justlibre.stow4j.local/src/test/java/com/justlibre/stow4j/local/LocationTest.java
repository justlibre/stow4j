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
		
		Pair<List<Container>, String> ret = location.containers("", Stow.StartCursor, 10);
		assertTrue(Stow.isCursorEnd(ret.right()));
		
		List<Container> cs = ret.left();
		assertTrue("size must match", cs.size() == 5);
		
		assertEquals("a1", cs.get(0).name());
		assertEquals("a2", cs.get(1).name());
		assertEquals("dir1", cs.get(2).name());
		assertEquals("dir2", cs.get(3).name());
		assertEquals("dir3", cs.get(4).name());
		
		assertTrue(isDir(cs.get(0).id()));
		assertTrue(isDir(cs.get(1).id()));
		assertTrue(isDir(cs.get(2).id()));
		assertTrue(isDir(cs.get(3).id()));
		assertTrue(isDir(cs.get(4).id()));
	}
	
	@Test
	public void testPrefix() throws Exception {
		Properties conf = new LocalBuilder(base).build();

		Location location = Stow.dial("local", conf);
		
		Pair<List<Container>, String> ret = location.containers("d", Stow.StartCursor, 10);
		assertTrue(Stow.isCursorEnd(ret.right()));
		
		List<Container> cs = ret.left();
		assertTrue("size must match", cs.size() == 3);
		
		assertEquals("dir1", cs.get(0).name());
		assertEquals("dir2", cs.get(1).name());
		assertEquals("dir3", cs.get(2).name());
		
		assertTrue(isDir(cs.get(0).id()));
		assertTrue(isDir(cs.get(1).id()));
		assertTrue(isDir(cs.get(2).id()));
	}
	
	@Test
	public void testRetrieve() throws Exception {
		Properties conf = new LocalBuilder(base).build();

		Location location = Stow.dial("local", conf);
		
		Pair<List<Container>, String> ret = location.containers("d", Stow.StartCursor, 10);
		assertTrue(Stow.isCursorEnd(ret.right()));
		
		List<Container> cs = ret.left();
		assertTrue("size must match", cs.size() == 3);
		
		assertEquals("dir1", cs.get(0).name());
		
		Container retrieved = location.getContainer(cs.get(0).id());
		assertEquals("dir1", retrieved.name());
	}
	
	@Test
	public void testCreate() throws Exception {
		Properties conf = new LocalBuilder(base).build();

		Location location = Stow.dial("local", conf);
		location.createContainer("cucurigu");
		
		Pair<List<Container>, String> ret = location.containers("cucu", Stow.StartCursor, 10);
		
		List<Container> cs = ret.left();
		assertTrue("size must match", cs.size() == 1);
		
		assertEquals("cucurigu", cs.get(0).name());
		
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
		ret = location.containers("container-", cursor, 10);
		assertEquals("full page (10)", 10, ret.left().size());
		assertEquals("cursor", "container-10", (new File(ret.right())).getName());
		cursor = ret.right();
		
		// get second page
		ret = location.containers("container-", cursor, 10);
		assertEquals("full page (10)", 10, ret.left().size());
		assertEquals("cursor", "container-20", (new File(ret.right())).getName());
		cursor = ret.right();
				
		// get last page
		ret = location.containers("container-", cursor, 10);
		assertEquals("reminder", 5, ret.left().size());
		assertTrue("no more", Stow.isCursorEnd(ret.right()));
		cursor = ret.right();
	}
	
	@Test
	public void testAsUrl() throws Exception {
		Properties conf = new LocalBuilder(base).build();

		Location location = Stow.dial("local", conf);
		Container c = location.getContainer("dir3");
		Pair<List<Item>, String> ret = c.items("deepitem", "", 1);
		Item itOrig = ret.left().get(0);
		
		Item itFound = location.itemByURL(itOrig.url());
		
		assertTrue(itFound != null);
		assertEquals(itOrig.name(), itFound.name());
		assertEquals(itOrig.id(), itFound.id());

	}
	
	private boolean isDir(String path) {
		File f = new File(path);
		return f.isDirectory();
	}
}
