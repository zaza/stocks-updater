package com.github.zaza.stockreader;

import org.junit.Test;

public class ZlotyRankingScrapperTest extends ScrapperTest {
	@Test
	public void srebrne_monety() throws Exception {
		assertItem(new ZlotyRankingScrapper("srebrne monety").collectItems(), "srebrne monety");
	}
}
