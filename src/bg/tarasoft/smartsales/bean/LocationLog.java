package bg.tarasoft.smartsales.bean;

public class LocationLog {
	private Coordinate coordinates;
	private int storeId;
	private String deviceId;
	
	public Coordinate getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(Coordinate coordinates) {
		this.coordinates = coordinates;
	}
	public int getStoreId() {
		return storeId;
	}
	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String string) {
		this.deviceId = string;
	}
}
