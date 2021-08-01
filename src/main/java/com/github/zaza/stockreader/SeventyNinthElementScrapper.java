package com.github.zaza.stockreader;

import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SeventyNinthElementScrapper extends Scrapper {
	private static final URI KRUGERRAND_URI = URI.create(
			"https://79element.pl/product/zlote-monety-inwestycyjne/161-zlota-moneta-lokacyjna-krugerrand-1oz-lata-losowe-o-c/");

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
			String price = document.getElementsByClass("woocommerce-Price-amount amount").first().text();
			checkState(price.endsWith("zł"));
			return price.substring(0, price.length()-"zł".length()).replaceAll("[^\\d]", "");
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
