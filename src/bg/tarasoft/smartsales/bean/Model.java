package bg.tarasoft.smartsales.bean;

import java.util.ArrayList;
import java.util.List;

public class Model {
	private int id;
	private String modelName;
	private String modelPicUrl;
	private List<Integer> series;
	
	public List<Integer> getSeries() {
		return series;
	}
	
	public void addSerie(Integer serie) {
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
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getModelPicUrl() {
		return modelPicUrl;
	}
	public void setModelPicUrl(String modelPicUrl) {
		this.modelPicUrl = modelPicUrl;
	}
}
