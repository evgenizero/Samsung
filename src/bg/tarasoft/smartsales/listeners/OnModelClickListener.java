package bg.tarasoft.smartsales.listeners;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.Model;
import bg.tarasoft.smartsales.bean.Serie;
import bg.tarasoft.smartsales.database.CategoryDataSource;

public class OnModelClickListener implements OnItemClickListener {

	private Context context;
	private Intent intent;
	private String parentName;
	private ArrayList<Category> categoriesForBar;

	public OnModelClickListener(Context context, String parentName,
			ArrayList<Category> categoriesForBar) {
		this.context = context;
		this.parentName = parentName;
		this.categoriesForBar = categoriesForBar;
		intent = new Intent(context,
				bg.tarasoft.smartsales.ProductsActivity.class);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Model model = (Model) arg0.getItemAtPosition(arg2);
		intent.putExtra("modelId", model.getId());

		//TODO
//		CategoryDataSource ds = new CategoryDataSource(context);
//		ds.open();
//		Category category = ds.getCategory(serie.getCategoryId());
//		ds.close();
//		intent.putExtra("subCatName", category.getName());
		intent.putExtra("modelName", model.getModelName());
		intent.putExtra("categoryName", parentName);
		intent.putExtra("headerBar", categoriesForBar);
		intent.putExtra("addToBar", true);
		context.startActivity(intent);

	}
}