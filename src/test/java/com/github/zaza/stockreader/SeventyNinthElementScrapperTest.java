package com.github.zaza.stockreader;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Iterables;

public class SeventyNinthElementScrapperTest extends ScrapperTest {
	@Test
	public void krugerrand() throws Exception {
		List<Map<String, Map<String, String>>> items = new SeventyNinthElementScrapper("Krugerrand").collectItems();
		assertItem(items, "Krugerrand");
		assertThat(Iterables.getOnlyElement(items).get("Krugerrand").get("modifier")).isNotBlank();
	}
}
