import java.util.Date;
import java.util.List;


public class StooqDataCollector extends DataCollector {

	public StooqDataCollector(String asset, Date date) {
		// TODO Auto-generated constructor stub
	}

	public StooqDataCollector(String asset) {
		this(asset, getLastDate(asset));
	}
	
	private static Date getLastDate(String asset) {
		// TODO
		return new Date();
	}
	
	@Override
	public List collectData() {
		// TODO Auto-generated method stub
		return null;
	}

}
