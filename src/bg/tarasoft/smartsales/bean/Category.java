package bg.tarasoft.smartsales.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable, ProductsGroup {
	/**
	 * 
	 */
	private static final long serialVersionUID = 988599878716866673L;
	private int id;
	private int parentId;
	private String name;
	private String imageUrl;
	private int sortOrder;
	private List<Integer> series;
	private int isShown;
	
	public List<Integer> getSeries() {
		return series;
	}
	
	public void addSerie(int serie) {
		if(series == null) {
			series = new ArrayList<Integer>();
		}
		
		series.add(serie);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public void setIsShown(int isShown) {
		this.isShown = isShown;
	}
	
	public int getIsShown() {
		return isShown;
	}
}
