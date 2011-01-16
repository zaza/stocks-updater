

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

public class InvestorsPlDataCollector extends DataCollector {

	private String asset;
	private Document dom;

	public InvestorsPlDataCollector(String asset) {
		this.asset = asset;
	}

	@Override
	public List<Data> collectData() {
		List<Data> result = new ArrayList<Data>();
		try {
			InputStream inputStream = getInput();
			parseXmlFile(inputStream);
			System.out.print("Reading DOM... ");
			NodeList nodes = XPathAPI.selectNodeList(dom, "//div[@class='resultsYear']/table/tr");
			if (nodes == null || nodes.getLength() == 0)
				// results still in one table
				nodes = XPathAPI.selectNodeList(dom, "//div[@id='results']/table/tr[position()>1]");
			if (nodes != null && nodes.getLength() > 0) {
				for (int i = 0; i < nodes.getLength(); i++) {
					Element element = (Element) nodes.item(i);
					NodeList childNodes = element.getChildNodes();
					// getTextContent() is not supported!
					String date = childNodes.item(0).getFirstChild().getNodeValue();
					String value = childNodes.item(1).getFirstChild().getNodeValue();
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date d = df.parse(date); 
					Data data = new Data(d, Float.parseFloat(value), asset);
					result.add(data);
				}
			}
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(result);
		return result;
	}
	
	protected InputStream getInput() throws IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://tfi.investors.pl/" + asset
				+ "/wyniki.html");
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = httpclient.execute(httpget, responseHandler);
		httpclient.getConnectionManager().shutdown();
		return new ByteArrayInputStream(responseBody.getBytes());
	}
	
	/*
	protected InputStream getInput() {
		HttpClient client = new HttpClient();

		GetMethod method = new GetMethod("http://tfi.investors.pl/" + asset
				+ "/wyniki.html");
		try {
			System.out.print("Executing GET... ");
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
				// TODO: return null
			}
			System.out.println("done.");

			// byte[] responseBody = method.getResponseBody();
			// System.out.println(new String(responseBody));

			return method.getResponseBodyAsStream();
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return null;
	}
	*/
	private void parseXmlFile(InputStream in) {
		System.out.print("Parsing the input stream... ");
		Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		tidy.setShowWarnings(false); 
//		tidy.setErrout(null);
		dom = tidy.parseDOM(in, null);
		System.out.println("done.");
	}

}
