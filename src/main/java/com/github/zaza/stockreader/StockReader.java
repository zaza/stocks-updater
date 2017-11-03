package com.github.zaza.stockreader;

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StockReader {
	public static void main(String[] args) {
		new ItemsWriter("all_hash.tmp").write(new StockReader().read());
	}

	private Collection<Map<String, Map<String, String>>> read() {
		Collection<Map<String, Map<String, String>>> items = new ArrayList<>();
		// TODO: replace with Alior
		items.addAll(new BaksyScrapper(readLines("currencies.txt")).collectItems());
		items.addAll(new StooqScrapper(readLines("stooqs.txt")).collectItems());
		items.addAll(new ZlotyRankingScrapper("srebrne monety").collectItems());
		items.addAll(new SeventyNinthElementScrapper("Krugerrand").collectItems());
		return items;
	}

	private List<String> readLines(String filename) {
		try {
			File file = new File(filename);
			if (!file.exists()) {
				System.err.println(format("File not found %s", filename));
				return Collections.emptyList();
			}
			return Files.lines(file.toPath()).collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
