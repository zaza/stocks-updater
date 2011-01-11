import java.util.Date;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		DataCollector invfiz = new StooqHistoricalDataCollector("INVFIZ", new Date(2009, 1, 1), new Date(System.currentTimeMillis()), StooqHistoricalDataInterval.Daily);
		List stooqData = invfiz.collectData();
		
		DataCollector invfizInvestorsPl = new InvestorsPlDataCollector("investor-fiz", "Investor FIZ");
		List investorsPlData = invfizInvestorsPl.collectData();
	}
}
