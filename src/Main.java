import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

public class Main {
	public static void main(String[] args) {
		if (args[0].startsWith("inv")) {
			String[] keys = InvestorsPlFactory.createKeys(args[0]);
			DataCollector invfizInvestorsPl = new InvestorsPlDataCollector(keys[0]);
			List<Data> investorsPlData = invfizInvestorsPl.collectData();
			Date start = investorsPlData.get(0).getDate();
			Date today = new Date(System.currentTimeMillis());
			Date end = DateUtils.truncate(today, Calendar.DAY_OF_MONTH);
			DataCollector invfiz = new StooqHistoricalDataCollector(keys[1], start, end, StooqHistoricalDataInterval.Daily);
			List<Data> stooqHistData = invfiz.collectData();
			List<Data[]> matched = DataUtils.matchByDate(investorsPlData, stooqHistData);
			for (Iterator<Data[]> iterator = matched.iterator(); iterator.hasNext();) {
				Data[] datas = (Data[]) iterator.next();
				float v1 = datas[0].getValue();
				String value1 = Float.toString(v1);
				String value2 = "";
				String ratio = "";
				if (datas[1] != null) {
					float v2 = datas[1].getValue();
					value2 = Float.toString(v2);
					ratio = Float.toString(v2 / v1);
				}
				// TODO: to file, floats with ',', separate by ';'
				System.out.println(datas[0].getFormattedDate() + "," + value1 + ","	+ value2 + "," + ratio);
			}
		} else if (args[0].equals("silver")){
			// TODO:
		}
	}
}
