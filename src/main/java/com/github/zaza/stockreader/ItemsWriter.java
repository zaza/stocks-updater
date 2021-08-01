package com.github.zaza.stockreader;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ItemsWriter {

	private File file;
	private Gson gson;

	ItemsWriter(String filename) {
		this(new File(filename));
	}

	ItemsWriter(File file) {
		this.file = file;
		this.gson = new GsonBuilder().create();
	}

	void write(Collection<Map<String, Map<String, String>>> items) {
		String json = gson.toJson(items);
		try {
			Files.asCharSink(file, Charsets.UTF_8).write(json);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
