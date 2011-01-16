import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

public class DataUtils {
	public static Data getByDate(List<Data> list, Date date) {
		for (Iterator<Data> iterator = list.iterator(); iterator.hasNext();) {
			Data data = (Data) iterator.next();
			if (data.getDate().equals(date)) {
				return data;
			}
		}
		return null;
	}

	public static List<Data[]> matchByDate(List<Data> fund, List<Data> stockExchange) {
		Date date1 = fund.get(0).getDate();
		Date date2 = stockExchange.get(0).getDate();
		Date start = date1;
		if (date1.after(date2))
			throw new IllegalStateException();
		
		date1 = fund.get(fund.size() - 1).getDate();
		date2 = stockExchange.get(stockExchange.size() - 1).getDate();
		Date end = date1;
		if (date1.before(date2))
			end = date2;

		long diff = end.getTime() - start.getTime();
		int days = (int) (diff / (1000 * 60 * 60 * 24));
		days++;

		List<Data[]> result = new ArrayList<Data[]>(days);
		for (int i = 0; i < days; i++) {
			Date d = DateUtils.addDays(start, i);
			Data data1 = getByDate(fund, d);
			if (data1 == null) {
				// use previous
				Data previous = result.get(i-1)[0];
				data1 = new Data(d, previous.getValue(), previous.getName());
			}
			Data data2 = getByDate(stockExchange, d);
			result.add(new Data[] {data1, data2});
		} 

		return result;
	}
}
