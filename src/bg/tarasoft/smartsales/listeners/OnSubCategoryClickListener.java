package bg.tarasoft.smartsales.listeners;

import java.util.ArrayList;

import bg.tarasoft.smartsales.SubCategoriesActivity;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.database.CategoryDataSource;
import bg.tarasoft.smartsales.database.SeriesDataSource;
import bg.tarasoft.smartsales.requests.GetChecksumRequest;
import bg.tarasoft.smartsales.requests.SamsungRequests;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OnSubCategoryClickListener implements OnItemClickListener {

	private Context context;
	private Intent intent;
	private String parentName;
	private SeriesDataSource dataSource;
	private CategoryDataSource catDS;
	private ArrayList<Category> categoriesForBar;

	public OnSubCategoryClickListener(Context context, String parentName,
			SeriesDataSource dataSource, ArrayList<Category> categoriesForBar) {
		this.context = context;
		this.categoriesForBar = categoriesForBar;
		this.parentName = parentName;
		this.dataSource = dataSource;
		catDS = new CategoryDataSource(context);

	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Category category = (Category) arg0.getItemAtPosition(arg2);
		catDS.open();
		if (catDS.containsSubCategories(category.getId())) {
			System.out.println("CAT FOR BAR SIZE: " + categoriesForBar.size());

			intent = new Intent(context,
					bg.tarasoft.smartsales.SubCategoriesActivity.class);
			intent.putExtra("noSeries", true);
			intent.putExtra("parentId", category.getId());
		} else if (dataSource.containsSeries(category.getId())) {
			if (categoriesForBar.size() >= 2) {
				intent = new Intent(context,
						bg.tarasoft.smartsales.SubSubCategoriesActivity.class);

			} else {
				intent = new Intent(context,
						bg.tarasoft.smartsales.SubCategoriesActivity.class);
			}
		} else {

			intent = new Intent(context,
					bg.tarasoft.smartsales.ProductsActivity.class);

		}
		catDS.close();
		intent.putExtra("headerBar", categoriesForBar);
		intent.putExtra("subCatName", category.getName());
		intent.putExtra("categoryName", parentName);
		intent.putExtra("addToBar", true);
		System.out.println("CLICK: " + category.getName() + category.getId());
		intent.putExtra("categoryId", category.getId());
		context.startActivity(intent);

	}

}
