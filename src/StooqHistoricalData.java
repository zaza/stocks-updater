import java.util.Date;


public class StooqHistoricalData extends Data {

	public StooqHistoricalData(Date date, float value) {
		super(date, value);
		// TODO Auto-generated constructor stub
	}

	public float getOpen() {
		return 0;
		// TODO Auto-generated method stub
		
	}

	public float getHigh() {
		return 0;
		// TODO Auto-generated method stub
		
	}

	public float getLow() {
		return 0;
		// TODO Auto-generated method stub
		
	}

	public float getClose() {
		return 0;
		// TODO Auto-generated method stub
		
	}

	public int getVolume() {
		return 0;
		// TODO Auto-generated method stub
	}
	
	@Override
	public float getValue() {
		return getClose();
	}

}
