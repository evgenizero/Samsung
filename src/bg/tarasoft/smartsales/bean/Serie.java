package bg.tarasoft.smartsales.bean;

import java.io.Serializable;

public class Serie implements Serializable, ProductsGroup {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3650956173714084807L;
	private int id;
	private String name;
	private String picUrl;
	private int categoryId;

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

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int id2) {
		this.categoryId = id2;
	}
}
