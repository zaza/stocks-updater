import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		DataCollector invfizInvestorsPl = new InvestorsPlDataCollector("investor-fiz");
		List<Data> investorsPlData = invfizInvestorsPl.collectData();
		System.out.println("investorsPlData size="+investorsPlData.size());
		Date start = investorsPlData.get(0).getDate();
		Date end = new Date(System.currentTimeMillis());
		DataCollector invfiz = new StooqHistoricalDataCollector("invfiz", start, end, StooqHistoricalDataInterval.Daily);
		List<Data> stooqHistData = invfiz.collectData();
		System.out.println("stooqHistData size="+stooqHistData.size());
		List<Data[]> matched = DataUtils.matchByDate(investorsPlData, stooqHistData);
		for (Iterator<Data[]> iterator = matched.iterator(); iterator.hasNext();) {
			Data[] datas = (Data[]) iterator.next();
			System.out.println(datas[0].getDate() + "," + datas[0].getValue() + "," + datas[1]==null?"<null>":datas[1].getValue());
		}
	}
}
