package stocks.collector.stooq;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import stocks.collector.DataCollector;
import stocks.data.Data;
import stocks.data.StooqHistoricalData;

/**
 * Gather historical data from stooq.pl pages available online. 
 *
 */
public class StooqPageHistoricalDataCollector extends DataCollector {
	
	private String asset;
	private Date start;
	private Date end;
	private StooqHistoricalDataInterval interval;

	public StooqPageHistoricalDataCollector(String asset, Date start, Date end,
			StooqHistoricalDataInterval interval) {
		this.asset = asset;
		this.start = start;
		this.end = end;
		this.interval = interval;
	}

	@Override
	public List<Data> collectData() {
		List<Data> result = new ArrayList<Data>();
		try {
			InputStream inputStream = getInput()[0];
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			String line = bufferedReader.readLine();
			while ((line = bufferedReader.readLine()) != null) {
				String[] split = line.split(",");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date d = df.parse(split[0]);
				float open =  Float.parseFloat(split[1]);
				float high =  Float.parseFloat(split[2]);
				float low =  Float.parseFloat(split[3]);
				float close =  Float.parseFloat(split[4]);
				int volume =  Integer.parseInt(split[5]);
				StooqHistoricalData data = new StooqHistoricalData(d, open, high, low, close, volume, asset);
				result.add(data);
			}
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Collections.sort(result);
		return result;
	}
	
	// http://stooq.com/q/d/?s=invpefiz&c=0&d1=20101109&d2=20110429
	protected InputStream[] getInput() throws IOException {
		// TODO: combine streams if getNextPageInputStream !=null
		HttpClient httpclient = new DefaultHttpClient();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		HttpGet httpget = new HttpGet("http://stooq.pl/q/d/?s=" + asset
				+ "&c=0&d1=" + sdf.format(start) + "&d2=" + sdf.format(end) + "&i="
				+ interval.toString());
		return new InputStream[] { httpclient.execute(httpget).getEntity().getContent()};
	}
}
