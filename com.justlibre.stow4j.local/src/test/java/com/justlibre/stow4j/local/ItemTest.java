package com.justlibre.stow4j.local;

import static org.junit.Assert.assertEquals;

import java.io.Reader;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
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

public class ItemTest {
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
	public void testURL() throws Exception {
		Properties conf = new LocalBuilder(base).build();

		Location location = Stow.dial("local", conf);
		Container c = location.getContainer("dir3");
		
		Pair<List<Item>, String> ret = c.items("deepitem1", Stow.StartCursor, 1);
		Item it = ret.left().get(0);
		
		URL url = new URL("file", "", Paths.get(base, "dir3", "deep", "deepitem1").toFile().toString());
		assertEquals(url, it.url());
	}
	
	@Test
	public void testDeepItem() throws Exception {
		Properties conf = new LocalBuilder(base).build();

		Location location = Stow.dial("local", conf);
		Container c = location.getContainer("dir3");
		
		Pair<List<Item>, String> ret = c.items("deepitem1", Stow.StartCursor, 1);
		Item it = ret.left().get(0);
		
		Reader r = it.open();
		char[] buf = new char[100];
		long n = r.read(buf);
		String read = new String(Arrays.copyOfRange(buf, 0, (int)n));
		assertEquals("same lenght", "3.deep.1".length(), n);
		assertEquals("content", "3.deep.1", read);

		r.close();
	}
	
}
