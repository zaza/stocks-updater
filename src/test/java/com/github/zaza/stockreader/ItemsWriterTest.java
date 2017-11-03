package com.github.zaza.stockreader;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ItemsWriterTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void write() throws Exception {
		File file = folder.newFile();
		Collection<Map<String, Map<String, String>>> items = new ArrayList<>();
		
		new ItemsWriter(file).write(items);
		
		assertThat(file).exists();
		assertThat(file).hasContent("[]");
	}
}
