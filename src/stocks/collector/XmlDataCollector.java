package stocks.collector;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import stocks.data.Data;

public abstract class XmlDataCollector extends DataCollector {
	
	protected Document dom;
	
	public abstract List<Data> collectData();

	protected abstract InputStream[] getInput() throws IOException;
	
	protected void parseXmlFile(InputStream in) {
		Tidy tidy = new Tidy();
		tidy.setShowWarnings(false);
		tidy.setQuiet(true);
		dom = tidy.parseDOM(in, null);
	}
}
