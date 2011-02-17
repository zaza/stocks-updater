package stocks.collector;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import stocks.data.Data;

public abstract class DataCollector {
	public abstract List<Data> collectData();
	protected abstract InputStream getInput() throws IOException;
}
