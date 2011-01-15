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


public class DataCollectorTests {

	@Test
	public void testInvestors() throws Exception {
		DataCollector invfizInvestorsPl = new InvestorsPlDataCollector(
				"investor-fiz", "Investor FIZ") {
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

		Data first = data.get(data.size() - 1);
		assertEquals("investor-fiz", first.getName());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = df.parse("2005-9-2");
		assertEquals(d, first.getDate());
		assertEquals(10000, first.getValue(), 0);
		assertEquals("Investor FIZ", first.getFullName());
	}

	@Test
	public void testInvestorsGold() throws Exception {
		DataCollector invfizInvestorsPl = new InvestorsPlDataCollector(
				"investor-gold-fiz", "Investor Gold FIZ") {
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

		Data first = data.get(data.size() - 1);
		assertEquals("investor-gold-fiz", first.getName());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = df.parse("2006-9-24");
		assertEquals(d, first.getDate());
		assertEquals(10000, first.getValue(), 0);
		assertEquals("Investor Gold FIZ", first.getFullName());
	}

	@Test
	public void testInvestorsCee() throws Exception {
		DataCollector invfizInvestorsPl = new InvestorsPlDataCollector(
				"investor-cee-fiz", "Investor CEE FIZ") {
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

		Data first = data.get(data.size() - 1);
		assertEquals("investor-cee-fiz", first.getName());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = df.parse("2007-5-21");
		assertEquals(d, first.getDate());
		assertEquals(1000, first.getValue(), 0);
		assertEquals("Investor CEE FIZ", first.getFullName());
	}

	@Test
	public void testInvestorsPe() throws Exception {
		DataCollector invfizInvestorsPl = new InvestorsPlDataCollector(
				"investor-pe-fiz", "Investor PE FIZ") {
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

		Data first = data.get(data.size() - 1);
		assertEquals("investor-pe-fiz", first.getName());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = df.parse("2007-9-21");
		assertEquals(d, first.getDate());
		assertEquals(1000, first.getValue(), 0);
		assertEquals("Investor PE FIZ", first.getFullName());
	}
	
	@Test
	public void testInvestorsProperty() throws Exception {
		DataCollector invfizInvestorsPl = new InvestorsPlDataCollector(
				"investor-property-fiz", "Investor Property FIZ") {
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

		Data first = data.get(data.size() - 1);
		assertEquals("investor-property-fiz", first.getName());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = df.parse("2010-7-9");
		assertEquals(d, first.getDate());
		assertEquals(1000, first.getValue(), 0);
		assertEquals("Investor Property FIZ", first.getFullName());
	}
	
	@Test
	public void testStooqHistoricalData() throws Exception {
		DataCollector invfizInvestorsPl = new StooqHistoricalDataCollector(
				"invfiz", "Investor FIZ", new Date(System.currentTimeMillis()), new Date(System
						.currentTimeMillis()),
				StooqHistoricalDataInterval.Daily) {
			protected InputStream getInput() {
				File file = new File(
						"test/data/invfiz_d.csv");
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
		assertEquals(746, data.size());
		
		StooqHistoricalData first = (StooqHistoricalData) data.get(data.size() - 1);
		assertEquals("invfiz", first.getName());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = df.parse("2011-1-11");
		assertEquals(d, first.getDate());
		assertEquals(2400.01f, first.getOpen(), 0);
		assertEquals(2412f, first.getHigh(), 0);
		assertEquals(2400.01f, first.getLow(), 0);
		assertEquals(2402f, first.getClose(), 0);
		assertEquals(2402f, first.getValue(), 0);
		assertEquals(21, first.getVolume(), 0);
		assertEquals("Investor FIZ", first.getFullName());
	}

	@Test
	public void testStooqToday() throws Exception {
		DataCollector invfizInvestorsPl = new StooqDataCollector("invfiz",
				new Date(System.currentTimeMillis()));
		List data = invfizInvestorsPl.collectData();
		// TODO: ignore empty result on Sat and Sun
		for (Iterator iterator = data.iterator(); iterator.hasNext();) {
			StooqData d = (StooqData) iterator.next();
			d.getDate();
			d.getValue();
			d.getAsk();
			d.getBid();
			d.getVolume();
		}
	}
	@Test
	public void testStooqLast() throws Exception {
		DataCollector invfizInvestorsPl = new StooqDataCollector("INVFIZ");
		List data = invfizInvestorsPl.collectData();
		for (Iterator iterator = data.iterator(); iterator.hasNext();) {
			Data d = (Data) iterator.next();
			d.getDate();
			d.getValue();
		}
	}
}
