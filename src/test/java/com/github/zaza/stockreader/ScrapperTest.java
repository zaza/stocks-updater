package com.github.zaza.stockreader;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.collect.Iterables;

abstract class ScrapperTest {
	protected static void assertItem(List<Map<String, Map<String, String>>> items, String id) {
		assertThat(items).hasSize(1);
		assertThat(Iterables.getOnlyElement(items)).containsKey(id);
		Map<String, String> item = Iterables.getOnlyElement(items).get(id);
		assertThat(item).containsKeys("name", "price", "date");
		assertThat(item.get("name")).isEqualTo(id);
		assertThat(item.get("price")).containsPattern(Pattern.compile("^\\d+.\\d+$"));
		assertThat(item.get("date")).containsPattern(Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$"));
	}
}
