import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StooqHistoricalDataCollector extends DataCollector {

	private String asset;
	private String fullName;
	private Date start;
	private Date end;
	private StooqHistoricalDataInterval interval;

	public StooqHistoricalDataCollector(String asset, String fullName, Date start, Date end,
			StooqHistoricalDataInterval interval) {
		this.asset = asset;
		this.fullName = fullName;
		this.start = start;
		this.end = end;
		this.interval = interval;
	}

	@Override
	public List collectData() {
		List<Data> result = new ArrayList<Data>();
		InputStream inputStream = getInput();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				String[] split = line.split(",");
				      DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date d;
				try {
					d = df.parse(split[0]);
				} catch (ParseException e) {
					// ignore
					continue;
				} 
				// split[1] otwarcie
				// split[2] najwyzszy
				// split[3] najnizszy
				String close = split[4];
				// split[5] wolumen
				StooqHistoricalData data = new StooqHistoricalData(d, Float.parseFloat(close), asset, fullName);
				result.add(data);
			}
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	protected InputStream getInput() {
		// http://stooq.pl/q/d/?s=invfiz&c=0&d1=20080122&d2=20110111
		// http://stooq.pl/q/d/l/?s=invfiz&d1=20080122&d2=20110111&i=d csv,
		// przecinek
		throw new UnsupportedOperationException("not implemented yet");
	}

}
