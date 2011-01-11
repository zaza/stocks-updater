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
	public void testInvestorsPl() throws Exception {
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

		Data first = data.get(data.size()-1);
		assertEquals("investor-fiz", first.getName());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = df.parse("2005-9-2"); 
		assertEquals(d, first.getDate());
		assertEquals(10000, first.getValue(), 0);
		assertEquals("Investor FIZ", first.getFullName());
	}

	@Test
	public void testStooqHistoricalData() throws Exception {
		DataCollector invfizInvestorsPl = new StooqHistoricalDataCollector(
				"INVFIZ", new Date(System.currentTimeMillis()), new Date(System
						.currentTimeMillis()),
				StooqHistoricalDataInterval.Daily);
		List data = invfizInvestorsPl.collectData();
		// TODO: ignore empty result on Sat and Sun
		for (Iterator iterator = data.iterator(); iterator.hasNext();) {
			StooqHistoricalData d = (StooqHistoricalData) iterator.next();
			d.getDate();
			d.getOpen();
			d.getHigh();
			d.getLow();
			d.getClose();
			d.getVolume();
			// d.getValue(); == getClose();

		}
	}

	@Test
	public void testStooqToday() throws Exception {
		DataCollector invfizInvestorsPl = new StooqDataCollector("INVFIZ",
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
