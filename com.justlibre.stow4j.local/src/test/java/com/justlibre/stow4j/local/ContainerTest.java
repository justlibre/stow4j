package com.justlibre.stow4j.local;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.StringReader;
import java.nio.file.Paths;
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

public class ContainerTest {
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
	public void testDeepItem() throws Exception {
		Properties conf = new LocalBuilder(base).build();

		Location location = Stow.dial("local", conf);
		Container c = location.getContainer("a1");

		Item it = c.put("deep/deeper/item", new StringReader("item"), "item".getBytes().length, null);
		assertEquals("item", it.getName());
		assertEquals(Paths.get(base, "a1", "deep", "deeper", "item").toAbsolutePath().toString(), it.getId());
	}
	
	@Test
	public void testItemById() throws Exception {
		Properties conf = new LocalBuilder(base).build();

		Location location = Stow.dial("local", conf);
		Container c = location.getContainer("a1");

		Item it = c.put("item", new StringReader("item"), "item".getBytes().length, null);
		Item it2 = c.getItem(it.getId());
		
		assertEquals(it.getId(), it2.getId());
		assertEquals(it.getName(), it2.getName());
	}
	@Test
	public void testPaging() throws Exception {
		Properties conf = new LocalBuilder(base).build();

		Location location = Stow.dial("local", conf);
		
		Container c = location.createContainer("base");
		
		for (int i = 0; i < 25; i++) {
			String it = String.format("item-%02d", i);
			c.put(it, new StringReader(it), it.getBytes().length, null);
		}
		
		Pair<List<Item>, String> ret;
		String cursor = Stow.StartCursor;
		
		// get first page
		ret = c.getItems("item-", cursor, 10);
		assertEquals("full page (10)", 10, ret.left().size());
		assertEquals("cursor", "item-10", (new File(ret.right())).getName());
		cursor = ret.right();
		
		// get second page
		ret = c.getItems("item-", cursor, 10);
		assertEquals("full page (10)", 10, ret.left().size());
		assertEquals("cursor", "item-20", (new File(ret.right())).getName());
		cursor = ret.right();
				
		// get last page
		ret = c.getItems("item-", cursor, 10);
		assertEquals("reminder", 5, ret.left().size());
		assertTrue("no more", Stow.isCursorEnd(ret.right()));
		cursor = ret.right();
	}

}
