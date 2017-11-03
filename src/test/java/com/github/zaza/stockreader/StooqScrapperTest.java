package com.github.zaza.stockreader;

import java.util.Collections;

import org.junit.Test;

public class StooqScrapperTest extends ScrapperTest {
	@Test
	public void kghm() throws Exception {
		assertItem(new StooqScrapper(Collections.singletonList("KGH")).collectItems(), "KGH");
	}
}
