import java.util.Date;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		DataCollector invfiz = new StooqHistoricalDataCollector("invfiz", "Investor FIZ", new Date(2009, 1, 1), new Date(System.currentTimeMillis()), StooqHistoricalDataInterval.Daily);
		List<Data> stooqData = invfiz.collectData();
		System.out.println("stooqData size="+stooqData.size());
		
		DataCollector invfizInvestorsPl = new InvestorsPlDataCollector("investor-fiz", "Investor FIZ");
		List<Data> investorsPlData = invfizInvestorsPl.collectData();
		System.out.println("investorsPlData size="+investorsPlData.size());
	}
}
