package com.github.zaza.stockreader;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class StooqScrapperTest extends ScrapperTest {

	@Rule
	public TestName name = new TestName();

	@Test
	public void kgh() throws Exception {
		assertStooqItem();
	}

	@Test
	public void pge() throws Exception {
		assertStooqItem();
	}

	@Test
	public void mnc() throws Exception {
		assertStooqItem();
	}

	private void assertStooqItem() {
		assertItem(new StooqScrapper(name.getMethodName()).collectItems(), name.getMethodName());
	}
}
