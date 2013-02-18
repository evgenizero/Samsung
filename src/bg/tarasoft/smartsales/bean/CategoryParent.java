package bg.tarasoft.smartsales.bean;

import java.util.List;

public class CategoryParent {
	private Category category;
	private List<Category> categories;
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
