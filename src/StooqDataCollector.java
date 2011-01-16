import java.io.InputStream;
import java.util.Collections;
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
	public List<Data> collectData() {
		// TODO Auto-generated method stub
		// Collections.sort(result);
		return null;
	}

	protected InputStream getInput() {
		throw new UnsupportedOperationException();
		// http://stooq.pl/q/d/?s=invfiz&c=0&d1=20080122&d2=20110111
	}

}
