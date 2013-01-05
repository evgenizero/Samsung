package bg.tarasoft.smartsales.listeners;

import java.util.ArrayList;

import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.Serie;
import bg.tarasoft.smartsales.database.CategoryDataSource;
import bg.tarasoft.smartsales.database.SeriesDataSource;
import bg.tarasoft.smartsales.requests.GetChecksumRequest;
import bg.tarasoft.smartsales.requests.SamsungRequests;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OnSerieClickListener implements OnItemClickListener {

	private Context context;
	private Intent intent;
	private String parentName;
	private ArrayList<Category> categoriesForBar;
	
	public OnSerieClickListener(Context context, String parentName, ArrayList<Category> categoriesForBar) {
		this.context = context;
		this.parentName = parentName;
		this.categoriesForBar = categoriesForBar;
		intent = new Intent(context, bg.tarasoft.smartsales.ProductsActivity.class);
	}
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Serie serie = (Serie) arg0.getItemAtPosition(arg2);
		intent.putExtra("serieId", serie.getId());
		
		CategoryDataSource ds = new CategoryDataSource(context);
		ds.open();
		Category category = ds.getCategory(serie.getCategoryId());
		ds.close();
		System.out.println("SERIEIERISERISEIRS IDDD: " + String.valueOf(serie.getId()));
		intent.putExtra("subCatName", category.getName());
		intent.putExtra("serieName", serie.getName());
		intent.putExtra("categoryName", parentName);
		intent.putExtra("headerBar", categoriesForBar);
		intent.putExtra("addToBar", true);
		context.startActivity(intent);
		
		
		
		
	}

}
