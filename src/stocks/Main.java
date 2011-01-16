package stocks;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
	public static void main(String[] args) throws IOException {
		// investors.pl
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
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Calendar c1 = Calendar.getInstance(); // today
			String file = "output/" + fund.getStooq() + "_" + sdf.format(c1.getTime()) + ".csv";
			toCsvFile(matched, file);
		}
		// silver
//		AllegroCoinsCollector allegroCoins = new AllegroCoinsCollector();
//		List<Data> allegroCoinsData = allegroCoins.collectData();
		// arkafrn12
	}
	
	private static void toCsvFile(List<Data[]> matched, String filePath) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
		for (Iterator<Data[]> iterator = matched.iterator(); iterator.hasNext();) {
			Data[] datas = (Data[]) iterator.next();
			float v1 = datas[0].getValue();
			String value1 = Float.toString(v1);
			String value2 = "";
			if (datas[1] != null) {
				float v2 = datas[1].getValue();
				value2 = Float.toString(v2);
			}
			// TODO: to file, floats with ',', separate by ';'
			out.write(datas[0].getFormattedDate() + "," + value1 + ","	+ value2);
			out.newLine();
		}
		out.close();
	}
}
