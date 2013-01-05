package bg.tarasoft.smartsales.bean;

public class Checksum {
	private String type;
	private String value;
	private int id;

	public Checksum(String type, String value) {
		this.type = type;
		this.value = value;
	}

	public Checksum() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
