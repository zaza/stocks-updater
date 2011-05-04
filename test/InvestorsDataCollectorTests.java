import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import stocks.collector.DataCollector;
import stocks.collector.investors.InvestorsPlDataCollector;
import stocks.collector.stooq.StooqDataCollector;
import stocks.collector.stooq.StooqHistoricalDataCollector;
import stocks.collector.stooq.StooqHistoricalDataInterval;
import stocks.data.Data;
import stocks.data.StooqCurrentData;
import stocks.data.StooqHistoricalData;


public class InvestorsDataCollectorTests {

	@Test
	public void testInvestors() throws Exception {
		DataCollector invfizInvestorsPl = new InvestorsPlDataCollector(
				"investor-fiz") {
			protected InputStream getInput() {
				File file = new File("test/data/investors-fiz--wyniki.html");
				try {
					return new FileInputStream(file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			};
		};
		List<Data> data = invfizInvestorsPl.collectData();
		assertEquals(12 /* 2010 */+ 12 /* 2009 */+ 12 /* 2008 */+ 12 /* 2007 */
				+ 12 /* 2006 */+ 6/* 2005 */, data.size());

		Data first = data.get(0);
		assertEquals("investor-fiz", first.getName());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = df.parse("2005-9-2");
		assertEquals(d, first.getDate());
		assertEquals(10000, first.getValue(), 0);
	}

	@Test
	public void testInvestorsGold() throws Exception {
		DataCollector invfizInvestorsPl = new InvestorsPlDataCollector(
				"investor-gold-fiz") {
			protected InputStream getInput() {
				File file = new File(
						"test/data/investors-gold-fiz--wyniki.html");
				try {
					return new FileInputStream(file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			};
		};
		List<Data> data = invfizInvestorsPl.collectData();
		assertEquals(12 /* 2010 */+ 12 /* 2009 */+ 12 /* 2008 */+ 12 /* 2007 */
				+ 5/* 2006 */, data.size());

		Data first = data.get(0);
		assertEquals("investor-gold-fiz", first.getName());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = df.parse("2006-9-24");
		assertEquals(d, first.getDate());
		assertEquals(10000, first.getValue(), 0);
	}

	@Test
	public void testInvestorsCee() throws Exception {
		DataCollector invfizInvestorsPl = new InvestorsPlDataCollector(
				"investor-cee-fiz") {
			protected InputStream getInput() {
				File file = new File(
						"test/data/investors-cee-fiz--wyniki.html");
				try {
					return new FileInputStream(file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			};
		};
		List<Data> data = invfizInvestorsPl.collectData();
		assertEquals(12 /* 2010 */+ 12 /* 2009 */+ 12 /* 2008 */
				+ 9/* 2007 */, data.size());

		Data first = data.get(0);
		assertEquals("investor-cee-fiz", first.getName());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = df.parse("2007-5-21");
		assertEquals(d, first.getDate());
		assertEquals(1000, first.getValue(), 0);
	}

	@Test
	public void testInvestorsPe() throws Exception {
		DataCollector invfizInvestorsPl = new InvestorsPlDataCollector(
				"investor-pe-fiz") {
			protected InputStream getInput() {
				File file = new File(
						"test/data/investors-pe-fiz--wyniki.html");
				try {
					return new FileInputStream(file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			};
		};
		List<Data> data = invfizInvestorsPl.collectData();
		assertEquals(15, data.size());

		Data first = data.get(0);
		assertEquals("investor-pe-fiz", first.getName());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = df.parse("2007-9-21");
		assertEquals(d, first.getDate());
		assertEquals(1000, first.getValue(), 0);
	}
	
	@Test
	public void testInvestorsProperty() throws Exception {
		DataCollector invfizInvestorsPl = new InvestorsPlDataCollector(
				"investor-property-fiz") {
			protected InputStream getInput() {
				File file = new File(
						"test/data/investors-property-fiz--wyniki.html");
				try {
					return new FileInputStream(file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			};
		};
		List<Data> data = invfizInvestorsPl.collectData();
		assertEquals(4, data.size());

		Data first = data.get(0);
		assertEquals("investor-property-fiz", first.getName());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = df.parse("2010-7-9");
		assertEquals(d, first.getDate());
		assertEquals(1000, first.getValue(), 0);
	}
}
