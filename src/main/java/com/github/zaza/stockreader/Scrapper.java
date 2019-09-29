package com.github.zaza.stockreader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.ImmutableMap;

abstract class Scrapper {
	protected static final int FIVE_SECONDS = (int) TimeUnit.SECONDS.toMillis(5);

	abstract List<Map<String, Map<String, String>>> collectItems();

	protected String formatDate(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	protected String today() {
		return formatDate(new Date());
	}

	protected Map<String, Map<String, String>> asMap(String id, String price, String date) {
		return ImmutableMap.of(id, ImmutableMap.of("name", id, "price", price, "date", date));
	}
}
