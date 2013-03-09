package bg.tarasoft.smartsales.listeners;

import java.util.ArrayList;

import bg.tarasoft.smartsales.SubCategoriesActivity;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.ProductsGroup;
import bg.tarasoft.smartsales.bean.Serie;
import bg.tarasoft.smartsales.database.CategoryDataSource;
import bg.tarasoft.smartsales.database.ModelsDataSource;
import bg.tarasoft.smartsales.database.SeriesDataSource;
import bg.tarasoft.smartsales.requests.GetChecksumRequest;
import bg.tarasoft.smartsales.requests.SamsungRequests;
import bg.tarasoft.smartsales.utilities.Utilities;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OnSerieNewClickListener implements OnItemClickListener {

	private Context context;
	private Intent intent;
	private String parentName;
	private SeriesDataSource dataSource;
	private ModelsDataSource modelsDS;
	private ArrayList<Category> categoriesForBar;
	private Integer currentCategory;

	public OnSerieNewClickListener(Context context, String parentName,
			ArrayList<Category> categoriesForBar) {
		this.context = context;
		this.parentName = parentName;
		this.categoriesForBar = categoriesForBar;
		modelsDS = new ModelsDataSource(context);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Serie serie = (Serie) arg0.getItemAtPosition(arg2);
		modelsDS.open();

		intent = new Intent(context,
				bg.tarasoft.smartsales.SubCategoriesActivity.class);
		intent.putExtra("models", true);
		intent.putExtra("parentId", serie.getId());
		Utilities.addToHistoryPath((Activity) context, serie);

		modelsDS.close();
		intent.putExtra("masterParentId", currentCategory);
		intent.putExtra("subCatName", serie.getName());
		intent.putExtra("categoryName", parentName);
		intent.putExtra("categoryId", serie.getId());
		context.startActivity(intent);

	}

}
