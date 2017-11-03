package com.github.zaza.stockreader;

import java.util.Collections;

import org.junit.Test;

public class BaksyScrapperTest extends ScrapperTest {
	@Test
	public void usd() throws Exception {
		assertItem(new BaksyScrapper(Collections.singletonList("USD")).collectItems(), "USD");
	}
}
