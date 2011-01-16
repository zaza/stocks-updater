package stocks.data;

import java.util.Date;

public class AllegroData extends Data {

	private int id;

	public AllegroData(Date date, float value, int id, String name) {
		super(date, value, name);
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	@Override
	public int compareTo(Data o) {
		int s = super.compareTo(o);
		if (s == 0) {
			return id - ((AllegroData)o).getId();
		}
		return s;
			
	}

}
