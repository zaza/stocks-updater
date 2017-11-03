package com.github.zaza.stockreader;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ZlotyRankingScrapper extends Scrapper {

	private static final URI CENY_SREBRA_URI = URI.create("http://zlotyranking.pl/ceny-srebra");

	private String id;

	public ZlotyRankingScrapper(String id) {
		this.id = id;
	}

	List<Map<String, Map<String, String>>> collectItems() {
		return Collections.singletonList(asMap(id, getPriceFromFirstRow(), today()));
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

}
