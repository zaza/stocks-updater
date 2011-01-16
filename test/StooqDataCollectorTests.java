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

import org.junit.Ignore;
import org.junit.Test;

import stocks.collector.DataCollector;
import stocks.collector.InvestorsPlDataCollector;
import stocks.collector.StooqDataCollector;
import stocks.collector.StooqHistoricalDataCollector;
import stocks.collector.StooqHistoricalDataInterval;
import stocks.data.Data;
import stocks.data.StooqData;
import stocks.data.StooqHistoricalData;


public class StooqDataCollectorTests {

	@Test
	public void testStooqHistoricalData() throws Exception {
		DataCollector invfizInvestorsPl = new StooqHistoricalDataCollector(
				"invfiz", new Date(System.currentTimeMillis()), new Date(System
						.currentTimeMillis()),
				StooqHistoricalDataInterval.Daily) {
			protected InputStream getInput() {
				File file = new File(
						"data/invfiz_d.csv");
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
		assertEquals(997, data.size());
		
		StooqHistoricalData first = (StooqHistoricalData) data.get(0);
		assertEquals("invfiz", first.getName());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = df.parse("2007-1-22");
		assertEquals(d, first.getDate());
		assertEquals(2199f, first.getOpen(), 0);
		assertEquals(2199f, first.getHigh(), 0);
		assertEquals(2050f, first.getLow(), 0);
		assertEquals(2070f, first.getClose(), 0);
		assertEquals(2070f, first.getValue(), 0);
		assertEquals(7, first.getVolume(), 0);
	}

	@Test
	@Ignore
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
	@Ignore
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
