package bg.tarasoft.smartsales;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import bg.tarasoft.smartsales.adapters.CategoryAdapter;
import bg.tarasoft.smartsales.adapters.GridAdapter;
import bg.tarasoft.smartsales.adapters.SeriesAdapter;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.LoggedActivity;
import bg.tarasoft.smartsales.bean.ProductsGroup;
import bg.tarasoft.smartsales.database.CategoryDataSource;
import bg.tarasoft.smartsales.database.ProductDataSource;
import bg.tarasoft.smartsales.database.SeriesDataSource;
import bg.tarasoft.smartsales.listeners.OnCategoryListItemClickListener;
import bg.tarasoft.smartsales.listeners.OnSerieClickListener;
import bg.tarasoft.smartsales.listeners.OnSettingsButtonClick;
import bg.tarasoft.smartsales.listeners.OnSubCategoryClickListener;
import bg.tarasoft.smartsales.requests.GetCategoriesRequest;
import bg.tarasoft.smartsales.requests.GetChecksumRequest;
import bg.tarasoft.smartsales.requests.GetProductsRequest;
import bg.tarasoft.smartsales.requests.GetSeriesRequest;
import bg.tarasoft.smartsales.requests.SamsungRequests;
import bg.tarasoft.smartsales.requests.SendLocationLogRequest;
import bg.tarasoft.smartsales.samsung.R;
import bg.tarasoft.smartsales.utilities.Utilities;
import bg.tarasoft.smartsales.views.HeaderBar;
import bg.tarasoft.smartsales.views.HeaderLabel;
import bg.tarasoft.smartsales.views.MenuButton;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SubCategoriesActivity extends Activity {

	private GridView gridView;
	private CategoryDataSource dataSource;
	private SeriesDataSource seriesDataSource;
	private ProductDataSource productsDataSource;
	private Context mContext;
	private HeaderBar headerBar;
	private ArrayList<ProductsGroup> categoriesForBar;
	private int id;
	private int logType;
	private Category firstCategory;
	private LinearLayout buttonsContainer;
	private Integer currentCategory;
	private Button settingsButton;

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

		productsDataSource = new ProductDataSource(this);
		productsDataSource.open();

		headerBar = (HeaderBar) findViewById(R.id.header_bar);
		buttonsContainer = (LinearLayout) findViewById(R.id.buttons_container);
		settingsButton = (Button) findViewById(R.id.settingsButton);
		settingsButton.setOnTouchListener(new OnSettingsButtonClick(this));

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
				currentCategory = id;
			}
			Object master = extras.get("masterParentId");
			if (master != null) {
				currentCategory = (Integer) master;
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
				List<Category> cats = dataSource.getCategories(id);
				System.out.println("SETTING SECOND ADAPTER");
				gridView.setAdapter(new CategoryAdapter(this, cats,
						R.layout.grid_item, productsDataSource
								.getNumberOfProducts(cats)));

				gridView.setOnItemClickListener(new OnSubCategoryClickListener(
						this, categoryName, seriesDataSource,
						(ArrayList<Category>) extras.get("headerBar"),
						currentCategory));

			}
			Utilities.addToLog(getApplicationContext(), id, logType);
			List<Category> categories = dataSource.getCategories(0);

			if (currentCategory == null) {
				currentCategory = id;
			}

			for (Category c : categories) {
				if (c.getId() == currentCategory) {
					buttonsContainer.addView(new MenuButton(mContext, c,
							headerBar, true));
				} else {
					buttonsContainer.addView(new MenuButton(mContext, c,
							headerBar));
				}
			}

		} else {

			System.out.println("EXTRAS ARE NULL");
			// new shit

			if (dataSource.isEmpty() && !Utilities.isOnline(this)) {
				AssetManager assets = getAssets();
				try {
					InputStream in1 = assets.open("categories.xml");
					InputStream in2 = assets.open("products.xml");
					InputStream in3 = assets.open("series.xml");
					new GetCategoriesRequest(this, in1);
					new GetProductsRequest(this, in2);
					new GetSeriesRequest(this, in3);
					SamsungRequests.getExecutor().execute();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				new SendLocationLogRequest(this);
				new GetChecksumRequest(this, "common_files");
				new GetChecksumRequest(this, "xml_categories");
				SamsungRequests.getExecutor().execute();
			}

			/*
			 * List<Category> categories = dataSource.getCategories(0);
			 * firstCategory = categories.get(0); gridView.setAdapter(new
			 * CategoryAdapter(this, dataSource
			 * .getCategories(firstCategory.getId()), R.layout.grid_item));
			 * 
			 * gridView.setOnItemClickListener(new OnSubCategoryClickListener(
			 * this, firstCategory.getName(), seriesDataSource, new
			 * ArrayList<Category>()));
			 */
		}

		if (extras != null) {
			categoriesForBar = (ArrayList<ProductsGroup>) extras
					.get("headerBar");
			if (extras.getBoolean("addToBar")) {
				categoriesForBar.add(dataSource.getCategory(id));
			}
		} else {
			categoriesForBar = new ArrayList<ProductsGroup>();
			categoriesForBar.add(firstCategory);

		}

		// headerBar.setCategories(categoriesForBar);

		dataSource.close();
		seriesDataSource.close();
		productsDataSource.close();
	}

	public void processData() {
		List<Category> categories = dataSource.getCategories(0);
		firstCategory = categories.get(0);
		List<Category> cats = dataSource.getCategories(firstCategory.getId());
		gridView.setAdapter(new CategoryAdapter(this, cats, R.layout.grid_item,
				productsDataSource.getNumberOfProducts(cats)));
		if (currentCategory == null) {
			currentCategory = categories.get(0).getId();
		}
		gridView.setOnItemClickListener(new OnSubCategoryClickListener(this,
				firstCategory.getName(), seriesDataSource,
				new ArrayList<Category>(), currentCategory));

		for (Category c : categories) {
			if (c.getId() == currentCategory) {
				buttonsContainer.addView(new MenuButton(mContext, c, headerBar,
						true));
			} else {
				buttonsContainer
						.addView(new MenuButton(mContext, c, headerBar));
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		dataSource.close();
		seriesDataSource.close();
		productsDataSource.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
		dataSource.open();
		seriesDataSource.open();
		productsDataSource.open();
	}

	// public void onSmartSalesClick(View v) {
	// Intent intent = new Intent(this, MainCategories.class);
	// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	// startActivity(intent);
	// }

}
