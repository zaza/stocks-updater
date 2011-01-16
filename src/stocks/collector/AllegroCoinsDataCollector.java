package stocks.collector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import stocks.data.AllegroData;
import stocks.data.Data;
import stocks.data.DataUtils;

public class AllegroCoinsDataCollector extends DataCollector {
	
	String path;
	
	@Override
	public List<Data> collectData() {
		List<Data> result = new ArrayList<Data>();
		path = "data/allegro-srebrne-uncje.csv";
		try {
			InputStream inputStream = getInput();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] split = line.split(";");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date d = df.parse(split[0]);
				float price = Float.parseFloat(split[1].replace(',', '.'));
				int id = (split.length > 2 && split[2] != null) ? Integer
						.parseInt(split[2]) : 0;
				String name = (split.length > 2 && split[2] != null) ? split[3]
						: null;
				AllegroData data = new AllegroData(d, price, id, name);
				result.add(data);
			}
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		path = "../webapi-client/20110116.txt";
		try{
			InputStream inputStream = getInput();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] split = line.split(";");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date d = df.parse(split[0]);
				float price = Float.parseFloat(split[1].replace(',', '.'));
				int id = Integer.parseInt(split[2]);
				String name = split[3];
				AllegroData data = new AllegroData(d, price, id, name);
				if (!isAlreadyAdded(result, data))
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

	private boolean isAlreadyAdded(List<Data> result, AllegroData data) {
		List<Data> byDate = DataUtils.getByDate(result, data.getDate());
		for (Iterator<Data> iterator = byDate.iterator(); iterator.hasNext();) {
			AllegroData d = (AllegroData) iterator.next();
			if (d.getId() == data.getId())
				return true;
		}
		return false;
	}
	
	@Override
	protected InputStream getInput() throws IOException {
		File file = new File(path);
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}