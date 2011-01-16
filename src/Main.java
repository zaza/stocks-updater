import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

public class Main {
	public static void main(String[] args) {
		DataCollector invfizInvestorsPl = new InvestorsPlDataCollector("investor-fiz");
		List<Data> investorsPlData = invfizInvestorsPl.collectData();
		Date start = investorsPlData.get(0).getDate();
		Date today = new Date(System.currentTimeMillis());
		Date end = DateUtils.truncate(today, Calendar.DAY_OF_MONTH);
		DataCollector invfiz = new StooqHistoricalDataCollector("invfiz", start, end, StooqHistoricalDataInterval.Daily);
		List<Data> stooqHistData = invfiz.collectData();
		List<Data[]> matched = DataUtils.matchByDate(investorsPlData, stooqHistData);
		for (Iterator<Data[]> iterator = matched.iterator(); iterator.hasNext();) {
			Data[] datas = (Data[]) iterator.next();
			String d2 = datas[1] == null ? "" : datas[1].getValue() + "";
			// TODO: to file, floats with ',', separate by ';'
			System.out.println(datas[0].getFormattedDate() + "," + datas[0].getValue() + "," + d2);
		}
	}
}
