package com.github.zaza.stockreader;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SeventyNinthElementScrapper extends Scrapper {
	private static final URI KRUGERRAND_URI = URI.create(
			"https://79element.pl/zlote-monety-inwestycyjne-1oz/south-african-gold-krugerrand-1-oz-lata-losowe");

	private static final Pattern PRICE_PATTERN = Pattern.compile("(\\d \\d{3},\\d{2}) zł");

	private String id;

	public SeventyNinthElementScrapper(String id) {
		this.id = id;
	}

	List<Map<String, Map<String, String>>> collectItems() {
		return Collections.singletonList(asMap(getPrice()));
	}

	private String getPrice() {
		try {
			Document document = Jsoup.parse(KRUGERRAND_URI.toURL(), FIVE_SECONDS);
			String price = document.getElementById("our_price_display").text();
			Matcher m = PRICE_PATTERN.matcher(price);
			m.find();
			return m.group(1).replace(',', '.').replace(" ", "");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Map<String, Map<String, String>> asMap(String price) {
		Map<String, Map<String, String>> result = new HashMap<>();
		Map<String, String> item = new HashMap<>();
		item.put("name", id);
		item.put("price", price);
		item.put("date", today());
		item.put("modifier", "95%");
		result.put(id, item);
		return result;
	}

}
