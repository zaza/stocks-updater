import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DataColletorTests {

		public void testInvestorsPl() throws Exception {
			DataCollector invfizInvestorsPl = new InvestorsPlDataCollector(
					"investor-fiz");
			List data = invfizInvestorsPl.collectData();
			for (Iterator iterator = data.iterator(); iterator.hasNext();) {
				Data d = (Data) iterator.next();
				d.getDate();
				d.getValue();
				d.getName();
				d.getFullName();
				
			}
		}

		public void testStooqHistoricalData() throws Exception {
			DataCollector invfizInvestorsPl = new StooqHistoricalDataCollector(
					"INVFIZ", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), StooqHistoricalDataInterval.Daily);
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
				//d.getValue(); == getClose(); 
				
			}
		}
		
		public void testStooqToday() throws Exception {
			DataCollector invfizInvestorsPl = new StooqDataCollector(
					"INVFIZ", new Date(System.currentTimeMillis()));
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
		
		public void testStooqLast() throws Exception {
			DataCollector invfizInvestorsPl = new StooqDataCollector(
					"INVFIZ");
			List data = invfizInvestorsPl.collectData();
			for (Iterator iterator = data.iterator(); iterator.hasNext();) {
				Data d = (Data) iterator.next();
				d.getDate();
				d.getValue();
			}
		}
}
