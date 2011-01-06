import java.util.Date;
import java.util.List;


public class StooqHistoricalDataCollector extends DataCollector {
	
	private String name;
	private Date start;
	private Date end;
	private StooqHistoricalDataInterval interval;
	
	public StooqHistoricalDataCollector(String name, Date start, Date end,
			StooqHistoricalDataInterval interval) {
		this.name = name;
		this.start = start;
		this.end = end;
		this.interval = interval;
	}

	@Override
	public List collectData() {
		// TODO Auto-generated method stub
		return null;
	}

}
