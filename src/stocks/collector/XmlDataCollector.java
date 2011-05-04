package stocks.collector;

import java.io.InputStream;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import stocks.data.Data;

public abstract class XmlDataCollector extends DataCollector {
	
	@Override
	public abstract List<Data> collectData();

	public static Document parseXmlFile(InputStream in) {
		Tidy tidy = new Tidy();
		tidy.setShowWarnings(false);
		tidy.setQuiet(true);
		return tidy.parseDOM(in, null);
	}
}
