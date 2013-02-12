package bg.tarasoft.smartsales.bean;

public class Store {
	private int storeID;
	private String storeName;
	private int hallId;
	private boolean chosen;
	
	public int getStoreID() {
		return storeID;
	}
	public void setStoreID(int storeID) {
		this.storeID = storeID;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public int getHallId() {
		return hallId;
	}
	public void setHallId(int hallId) {
		this.hallId = hallId;
	}
	public boolean isChosen() {
		return chosen;
	}
	public void setIsChosen(boolean chosen) {
		this.chosen = chosen;
	}
}
