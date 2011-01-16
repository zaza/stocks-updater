package stocks;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

import stocks.collector.DataCollector;
import stocks.collector.InvestorsPlDataCollector;
import stocks.collector.StooqHistoricalDataCollector;
import stocks.collector.StooqHistoricalDataInterval;
import stocks.collector.InvestorsPlDataCollector.Fund;
import stocks.data.Data;
import stocks.data.DataUtils;


public class Main {
	public static void main(String[] args) {
		for (Fund fund : Fund.values()) {
			DataCollector invfizInvestorsPl = new InvestorsPlDataCollector(fund.getInvestorsPl());
			List<Data> investorsPlData = invfizInvestorsPl.collectData();
			Date start = investorsPlData.get(0).getDate();
			Date today = new Date(System.currentTimeMillis());
			Date end = DateUtils.truncate(today, Calendar.DAY_OF_MONTH);
			DataCollector invfiz = new StooqHistoricalDataCollector(fund.getStooq(), start, end, StooqHistoricalDataInterval.Daily);
			List<Data> stooqHistData = invfiz.collectData();
			List<Data[]> matched = DataUtils.matchByDate(investorsPlData, stooqHistData);
			// TODO: add today
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
		}
	}
}
