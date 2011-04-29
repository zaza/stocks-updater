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
import stocks.collector.allegro.AllegroCoinsDataCollector;
import stocks.collector.arka.ArkaDataCollector;
import stocks.collector.investors.InvestorsPlDataCollector;
import stocks.collector.investors.InvestorsPlDataCollector.Fund;
import stocks.collector.stooq.StooqDataCollector;
import stocks.collector.stooq.StooqHistoricalDataCollector;
import stocks.collector.stooq.StooqHistoricalDataInterval;
import stocks.collector.stooq.StooqPageHistoricalDataCollector;
import stocks.data.Data;
import stocks.data.DataUtils;


public class Main {
	public static void main(String[] args) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar c1 = Calendar.getInstance(); // today

		// === investors.pl
		for (Fund fund : Fund.values()) {
			if (proceed(args, fund.getStooq())) {
				System.out.println("Processing '" + fund + "'...");
				DataCollector invfizInvestorsCollector = new InvestorsPlDataCollector(fund.getInvestorsPl());
				List<Data> invfizPl = invfizInvestorsCollector.collectData();
				Date start = invfizPl.get(0).getDate();
				Date today = new Date(System.currentTimeMillis());
				Date end = DateUtils.truncate(today, Calendar.DAY_OF_MONTH);
				DataCollector invfiz = new StooqPageHistoricalDataCollector(fund.getStooq(), start, end, StooqHistoricalDataInterval.Daily);
				List<Data> stooqHistData = invfiz.collectData();
				List<Data[]> matched = DataUtils.matchByDate(invfizPl, stooqHistData);

				// add latest
				DataCollector latestFromStooq = new StooqDataCollector(fund.getStooq());
				List<Data> stooqData = latestFromStooq.collectData();
				stooqHistData.add(stooqData.get(0));

				// TODO: add today
				String file = "output/" + fund.getStooq() + "_" + sdf.format(c1.getTime()) + ".csv";
				toCsvFile(matched, file);
			}
		}

		// === silver
		if (proceed(args, "silver")) {
			System.out.println("Processing 'silver'...");
			AllegroCoinsDataCollector allegroCoinsCollector = new AllegroCoinsDataCollector();
			List<Data> allegroCoins = allegroCoinsCollector.collectData();
			Date start = allegroCoins.get(0).getDate();
			Date today = new Date(System.currentTimeMillis());
			Date end = DateUtils.truncate(today, Calendar.DAY_OF_MONTH);
			DataCollector rcsilaopenHistory = new StooqHistoricalDataCollector("rcsilaopen", start, end, StooqHistoricalDataInterval.Daily);
			List<Data> stooqHistData = rcsilaopenHistory.collectData();
			// add latest
			DataCollector latestFromStooq = new StooqDataCollector("rcsilaopen");
			List<Data> stooqData = latestFromStooq.collectData();
			stooqHistData.add(stooqData.get(0));

			List<Data[]> matched = DataUtils.matchByDate(stooqHistData, allegroCoins);
			String file = "output/" + "silver" + "_" + sdf.format(c1.getTime()) + ".csv";
			toCsvFile(matched, file);
		}

		// === arkafrn12
		if (proceed(args, "arkafrn12")) {
			System.out.println("Processing 'arkafrn12'...");
			DataCollector arkafrn12Collector = new ArkaDataCollector("arka-bz-wbk-fundusz-rynku-nieruchomosci-fiz");
			List<Data> arkafrn = arkafrn12Collector.collectData();
			Date start = arkafrn.get(0).getDate();
			Date today = new Date(System.currentTimeMillis());
			Date end = DateUtils.truncate(today, Calendar.DAY_OF_MONTH);
			DataCollector arkafrn12History = new StooqHistoricalDataCollector("arkafrn12", start, end, StooqHistoricalDataInterval.Daily);
			List<Data> stooqHistData = arkafrn12History.collectData();
			// add latest
			StooqDataCollector latestFromStooq = new StooqDataCollector("arkafrn12");
			List<Data> stooqData = latestFromStooq.collectData();
			stooqHistData.add(stooqData.get(0));

			List<Data[]> matched = DataUtils.matchByDate(arkafrn, stooqHistData);
			String file = "output/" + "arkafrn12" + "_" + sdf.format(c1.getTime()) + ".csv";
			toCsvFile(matched, file);
		}
		System.out.println("Done.");
	}
	
	private static boolean proceed(String[] args, String string) {
		if (args.length == 0)
			return true;
		for (String arg : args) {
			if (arg.equalsIgnoreCase(string)) 
				return true;
		}
		return false;
	}

	private static void toCsvFile(List<Data[]> matched, String filePath) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
		for (Iterator<Data[]> iterator = matched.iterator(); iterator.hasNext();) {
			Data[] datas = (Data[]) iterator.next();
			String value1 = datas[0].toCsvString();
			String value2 = datas[1] != null ? datas[1].toCsvString() : "";
			out.write(datas[0].getFormattedDate() + ";" + value1 + ";"	+ value2);
			out.newLine();
		}
		out.close();
	}
}
