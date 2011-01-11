import java.util.Date;

public class Data {

	private Date date;
	private float value;
	private String name;
	private String fullName;

	public Data(Date date, float value, String name, String fullName) {
		this.date = date;
		this.value = value;
		this.name = name;
		this.fullName = fullName;
	}

	public Date getDate() {
		return date;
	}

	public float getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public String getFullName() {
		return fullName;
	}

}
