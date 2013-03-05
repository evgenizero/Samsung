package bg.tarasoft.smartsales.bean;

public class Product implements ProductsGroup{
	private int id;
	private String name;
	private String imageUrl;
	private int categoryId;
	private int label;
	private int price;
	private boolean isChecked;
	public static final int LABEL_NONE = 0;
	public static final int LABEL_NEW = 1;
	public static final int LABEL_LAST = 2;
	public static final int LABEL_PROMO = 3;

	public Product(Product p) {
		this.id = p.getId();
		this.name = p.getName();
		this.imageUrl = p.getImageUrl();
		this.categoryId = p.getCategoryId();
		this.label = p.getLabel();

	}

	public Product() {
		// TODO Auto-generated constructor stub
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public boolean isToCompare() {
		return isChecked;
	}
	
	public void setToCompare(boolean isChecked) {
		this.isChecked = isChecked;
	}
}