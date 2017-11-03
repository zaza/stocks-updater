package com.github.zaza.stockreader;

import static java.lang.String.format;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class StooqScrapper {

	private static final String STOOQ = "https://stooq.pl/q/?s=%s";

	private static final int FIVE_SECONDS = (int) TimeUnit.SECONDS.toMillis(5);
	
	private Collection<String> ids;

	public StooqScrapper(Collection<String> ids) {
		this.ids = ids;
	}

	List<Map<String, Map<String, String>>> collectItems() {
		return ids.parallelStream().map(scrapPage()).collect(Collectors.toList());
	}

	private Function<String, Map<String, Map<String, String>>> scrapPage() {
		return new Function<String, Map<String, Map<String, String>>> () {

			@Override
			public Map<String, Map<String, String>> apply(String id) {
				try {
					Document document = Jsoup.parse(getUrl(id), FIVE_SECONDS);
					Element price = document.getElementById(format("aq_%s_c2|3", id.toLowerCase()));
					Element date = document.getElementById(format("aq_%s_d2", id.toLowerCase()));
					
					Map<String, Map<String, String>> result = new HashMap<>();
					Map<String, String> item = new HashMap<>();
					item.put("name", id);
					item.put("price", price.text());
					item.put("date", date.text());
					result.put(id, item);
					return result;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}};
	}
	
	private URL getUrl(String id) {
		try {
			return URI.create(format(STOOQ, id)).toURL();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

}
