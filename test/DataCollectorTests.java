import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class DataCollectorTests {

	@Test
	public void testInvestorsPl() throws Exception {
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
		// 2010+2009+2008+2007+2006+2005
		assertEquals(12+12+12+12+12+6, data.size());
		for (Iterator<Data> iterator = data.iterator(); iterator.hasNext();) {
			Data d = (Data) iterator.next();
			d.getDate();
			d.getValue();
			d.getName();
			d.getFullName();

		}
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
