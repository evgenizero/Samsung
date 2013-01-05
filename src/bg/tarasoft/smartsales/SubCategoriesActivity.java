package bg.tarasoft.smartsales;

import java.util.ArrayList;

import bg.tarasoft.smartsales.R;
import bg.tarasoft.smartsales.adapters.CategoryAdapter;
import bg.tarasoft.smartsales.adapters.GridAdapter;
import bg.tarasoft.smartsales.adapters.SeriesAdapter;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.LoggedActivity;
import bg.tarasoft.smartsales.bean.ProductsGroup;
import bg.tarasoft.smartsales.database.CategoryDataSource;
import bg.tarasoft.smartsales.database.SeriesDataSource;
import bg.tarasoft.smartsales.listeners.OnSerieClickListener;
import bg.tarasoft.smartsales.listeners.OnSubCategoryClickListener;
import bg.tarasoft.smartsales.utilities.Utilities;
import bg.tarasoft.smartsales.views.HeaderBar;
import bg.tarasoft.smartsales.views.HeaderLabel;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class SubCategoriesActivity extends Activity {

	private GridView gridView;
	private CategoryDataSource dataSource;
	private SeriesDataSource seriesDataSource;
	private Context mContext;
	private HeaderBar headerBar;
	private ArrayList<ProductsGroup> categoriesForBar;
	private int id;
	private int logType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_categories);

		// Keep screen on
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);

		System.out.println("CREATED SUBCAT ACTIVITy");
		gridView = (GridView) findViewById(R.id.sub_categories);

		dataSource = new CategoryDataSource(this);
		dataSource.open();
		seriesDataSource = new SeriesDataSource(this);
		seriesDataSource.open();
		headerBar = (HeaderBar) findViewById(R.id.header_bar);
		mContext = this;

		final Bundle extras = getIntent().getExtras();

		if (extras != null) {

			// because clear_history doesn't pop it from the stack
			if (extras.getBoolean("shouldFinish")) {
				System.out.println("GONNA FINISH");
				finish();
			}
			String categoryName = extras.getString("categoryName");
			id = 0;
			Object o = extras.get("parentId");
			if (o != null) {
				id = (Integer) o;
				logType = LoggedActivity.CATEGORY;
			}
			if (!extras.getBoolean("noSeries")) {
				System.out.println();
				logType = LoggedActivity.CATEGORY;
				id = extras.getInt("categoryId");
				/*
				 * headerLabel2.setVisibility(View.VISIBLE);
				 * headerLabel2.setText(extras.getString("subCatName"));
				 * headerLabel.setOnClickListener(new OnClickListener() {
				 * 
				 * public void onClick(View v) {
				 * Utilities.returnToFirst(mContext, headerLabel, true); } });
				 */
				System.out.println("SETTING FIRST ADAPTER");
				gridView.setAdapter(new SeriesAdapter(this, seriesDataSource
						.getSeries(id), R.layout.grid_item));

				gridView.setOnItemClickListener(new OnSerieClickListener(this,
						categoryName, (ArrayList<Category>) extras
								.get("headerBar")));

			} else {
				logType = LoggedActivity.CATEGORY;
				System.out.println("CATEGORIES FOR "
						+ extras.getInt("categoryId") + ":");
				for (Category c : dataSource.getCategories(extras
						.getInt("categoryId"))) {
					System.out.println(c.getName());
				}
				System.out.println("SETTING SECOND ADAPTER");
				gridView.setAdapter(new CategoryAdapter(this, dataSource
						.getCategories(id), R.layout.grid_item));

				gridView.setOnItemClickListener(new OnSubCategoryClickListener(
						this, categoryName, seriesDataSource,
						(ArrayList<Category>) extras.get("headerBar")));

			}
			Utilities.addToLog(getApplicationContext(), id, logType);

		} else {
			System.out.println("EXTRAS ARE NULL");
		}
		categoriesForBar = (ArrayList<ProductsGroup>) extras.get("headerBar");
		System.out.println("CATEGORY ID IS : = " + id);
		categoriesForBar.size();
		if (extras.getBoolean("addToBar")) {
			categoriesForBar.add(dataSource.getCategory(id));
		}

		System.out.println("CATEGORIES FOR BAR: " + categoriesForBar.size());
		for (ProductsGroup c : categoriesForBar) {
			System.out.println(c.getName());
		}
		headerBar.setCategories(categoriesForBar);

		dataSource.close();
		seriesDataSource.close();
	}

	@Override
	protected void onPause() {
		super.onPause();
		dataSource.close();
		seriesDataSource.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
		dataSource.open();
		seriesDataSource.open();
	}

	public void onSmartSalesClick(View v) {
		Intent intent = new Intent(this, MainCategories.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

}
