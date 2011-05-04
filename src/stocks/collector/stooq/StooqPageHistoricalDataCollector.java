package stocks.collector.stooq;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.TransformerException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import stocks.collector.XmlDataCollector;
import stocks.data.Data;
import stocks.data.StooqHistoricalData;

import com.sun.org.apache.xpath.internal.XPathAPI;

/**
 * Gather historical data from stooq.pl pages available online. 
 *
 */
public class StooqPageHistoricalDataCollector extends XmlDataCollector {
	
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
			InputStream[] inputStreams = getInput();
			for (InputStream is : inputStreams) {
				parseXmlFile(is);
				NodeList nodes = XPathAPI.selectNodeList(dom, "//table/tbody[@id='f13' and @style='background-color:ffffff' and @align='right']/tr");
				
				if (nodes != null && nodes.getLength() > 0) {
					for (int i = 0; i < nodes.getLength(); i++) {
						Element element = (Element) nodes.item(i);
						NodeList childNodes = element.getChildNodes();
						// getTextContent() is not supported!
						String date = childNodes.item(1).getFirstChild().getNodeValue();
						DateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.US);
						Date d = df.parse(date);
						float open =  Float.parseFloat(childNodes.item(2).getFirstChild().getNodeValue());
						float high =  Float.parseFloat(childNodes.item(3).getFirstChild().getNodeValue());
						float low =  Float.parseFloat(childNodes.item(4).getFirstChild().getNodeValue());
						float close =  Float.parseFloat(childNodes.item(5).getFirstChild().getNodeValue());
						int volume =  Integer.parseInt(childNodes.item(6).getFirstChild().getNodeValue());
						StooqHistoricalData data = new StooqHistoricalData(d, open, high, low, close, volume, asset);
						result.add(data);
					}
				}
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(result);
		return result;
	}
	
	protected InputStream[] getInput() throws IOException {
		List<InputStream> streams = new ArrayList<InputStream>(); 
		HttpClient httpclient = new DefaultHttpClient();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		HttpGet httpget = new HttpGet("http://stooq.pl/q/d/?s=" + asset
				+ "&c=0&d1=" + sdf.format(start) + "&d2=" + sdf.format(end) + "&i="
				+ interval.toString());
		HttpResponse response = httpclient.execute(httpget);
		int l = 1;
		while (response.getStatusLine().getStatusCode() == 200) {
			streams.add(response.getEntity().getContent());
			l++;
			httpget = new HttpGet("http://stooq.pl/q/d/?s=" + asset
					+ "&c=0&d1=" + sdf.format(start) + "&d2=" + sdf.format(end) + "&i="
					+ interval.toString()+"&l="+l);
			response = httpclient.execute(httpget);
		}
		return streams.toArray(new InputStream[0]);
	}
}
