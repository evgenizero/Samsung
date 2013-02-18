package bg.tarasoft.smartsales.bean;

import java.util.List;

public class StoreType {
	private List<Store> stores;
	private int id;
	private String name;

	public List<Store> getStores() {
		return stores;
	}

	public void setStores(List<Store> stores) {
		this.stores = stores;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
