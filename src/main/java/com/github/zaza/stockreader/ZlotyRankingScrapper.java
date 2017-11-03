package com.github.zaza.stockreader;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ZlotyRankingScrapper {

	private static final URI CENY_SREBRA_URI = URI.create("http://zlotyranking.pl/ceny-srebra");

	private static final int FIVE_SECONDS = (int) TimeUnit.SECONDS.toMillis(5);

	private String id;

	public ZlotyRankingScrapper(String id) {
		this.id = id;
	}

	List<Map<String, Map<String, String>>> collectItems() {
		return Collections.singletonList(asMap(getPriceFromFirstRow()));
	}

	private String getPriceFromFirstRow() {
		try {
			Document document = Jsoup.parse(CENY_SREBRA_URI.toURL(), FIVE_SECONDS);
			Element firstPrice = document
					.select("section#maincontent.twelve.columns > section.maincarousel > div.columns > div > div.price")
					.first();
			String ownText = firstPrice.ownText();
			return ownText.substring(0, ownText.indexOf(' '));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Map<String, Map<String, String>> asMap(String price) {
		Map<String, Map<String, String>> result = new HashMap<>();
		Map<String, String> item = new HashMap<>();
		item.put("name", id);
		item.put("price", price);
		item.put("date", formatDate(new Date()));
		result.put(id, item);
		return result;
	}

	private String formatDate(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
}
