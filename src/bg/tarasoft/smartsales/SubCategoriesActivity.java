package bg.tarasoft.smartsales;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import bg.tarasoft.smartsales.adapters.CategoryAdapter;
import bg.tarasoft.smartsales.adapters.GridAdapter;
import bg.tarasoft.smartsales.adapters.ModelsAdapter;
import bg.tarasoft.smartsales.adapters.SeriesAdapter;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.LoggedActivity;
import bg.tarasoft.smartsales.bean.ProductsGroup;
import bg.tarasoft.smartsales.database.CategoryDataSource;
import bg.tarasoft.smartsales.database.ModelsDataSource;
import bg.tarasoft.smartsales.database.ProductDataSource;
import bg.tarasoft.smartsales.database.SeriesDataSource;
import bg.tarasoft.smartsales.listeners.OnAccountButtonClick;
import bg.tarasoft.smartsales.listeners.OnCategoryListItemClickListener;
import bg.tarasoft.smartsales.listeners.OnModelClickListener;
import bg.tarasoft.smartsales.listeners.OnSerieClickListener;
import bg.tarasoft.smartsales.listeners.OnSerieNewClickListener;
import bg.tarasoft.smartsales.listeners.OnSettingsButtonClick;
import bg.tarasoft.smartsales.listeners.OnSubCategoryClickListener;
import bg.tarasoft.smartsales.requests.GetCategoriesRequest;
import bg.tarasoft.smartsales.requests.GetChecksumRequest;
import bg.tarasoft.smartsales.requests.GetModelsRequest;
import bg.tarasoft.smartsales.requests.GetProductsRequest;
import bg.tarasoft.smartsales.requests.GetSeriesRequest;
import bg.tarasoft.smartsales.requests.GetStoresRequest;
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
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SubCategoriesActivity extends Activity {

	private GridView gridView;
	private CategoryDataSource dataSource;
	private SeriesDataSource seriesDataSource;
	private ProductDataSource productsDataSource;
	private ModelsDataSource modelsDataSource;
	private Context mContext;
	private HeaderBar headerBar;
	private int id;
	private int logType;
	private Category firstCategory;
	private LinearLayout buttonsContainer;
	private Integer currentCategory;
	private Button settingsButton;
	private ArrayList<ProductsGroup> categoriesForBar;
	private HorizontalScrollView scrollView;
	private TextView accountManage;
	private SharedPreferences settings;

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

		modelsDataSource = new ModelsDataSource(this);
		modelsDataSource.open();

		headerBar = (HeaderBar) findViewById(R.id.header_bar);
		scrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView1);
		accountManage = (TextView) findViewById(R.id.account_button);
		accountManage.setOnClickListener(new OnAccountButtonClick(this));

		buttonsContainer = (LinearLayout) findViewById(R.id.buttons_container);
		settingsButton = (Button) findViewById(R.id.settingsButton);
		settingsButton.setOnTouchListener(new OnSettingsButtonClick(this));
		// settingsButton.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		// Intent intent = new Intent(mContext,
		// bg.tarasoft.smartsales.EnterPassword.class);
		// mContext.startActivity(intent);
		// }
		// });

		mContext = this;

		setAccountInfo();

		final Bundle extras = getIntent().getExtras();

		if (extras != null) {

			int scrollPosition = extras.getInt("scrollPosition");

			System.out.println("SCROLLLL POSITION: " + scrollPosition);

			scrollView.scrollTo(scrollPosition, 0);

			if (extras.getBoolean("update")) {
				update();
			} else {

				String categoryName = extras.getString("categoryName");
				// id = 0;
				int id = extras.getInt("parentId");
				logType = LoggedActivity.CATEGORY;
				currentCategory = id;

				//
				Object master = extras.get("masterParentId");
				if (master != null) {
					currentCategory = (Integer) master;
				}

				if (extras.getBoolean("models")) {
					gridView.setAdapter(new ModelsAdapter(this,
							modelsDataSource.getModelsBySerie(id),
							R.layout.grid_item));
					gridView.setOnItemClickListener(new OnModelClickListener(
							this, categoryName, (ArrayList<Category>) extras
									.get("headerBar")));

				} else if (!extras.getBoolean("noSeries")) {
					logType = LoggedActivity.CATEGORY;
					id = extras.getInt("categoryId");

					System.out.println("SETTING FIRST ADAPTER");
					gridView.setAdapter(new SeriesAdapter(this,
							seriesDataSource.getSeries(id), R.layout.grid_item));

					gridView.setOnItemClickListener(new OnSerieNewClickListener(
							this, categoryName, (ArrayList<Category>) extras
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

					gridView.setAdapter(new CategoryAdapter(this, cats,
							R.layout.grid_item, productsDataSource
									.getNumberOfProducts(cats)));

					gridView.setOnItemClickListener(new OnSubCategoryClickListener(
							this, categoryName, seriesDataSource,
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

			}
			Utilities.addHistoryToBar(this, headerBar);

		} else {

			System.out.println("EXTRAS ARE NULL");

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
				update();
				reloadBottomButtons();
				Utilities.getHistory(mContext).clear();
				Utilities.addToHistoryPath(this, firstCategory);
				Utilities.addHistoryToBar(this, headerBar);

			}
		}
		/*
		 * if (extras != null) { categoriesForBar = (ArrayList<ProductsGroup>)
		 * extras .get("headerBar"); if (extras.getBoolean("addToBar")) {
		 * categoriesForBar.add(dataSource.getCategory(id)); } } else { Category
		 * cat = new Category(); reloadBottomButtons(); firstCategory.getId();
		 * Utilities.addToHistoryPath(this, firstCategory);
		 * Utilities.addHistoryToBar(this, headerBar);
		 * 
		 * }
		 */
		dataSource.close();
		seriesDataSource.close();
		productsDataSource.close();
	}

	private void setAccountInfo() {
		settings = getSharedPreferences("settings", 0);
		if (settings.getBoolean("guest", true)) {
			accountManage.setText("Влизане");
		} else {
			accountManage.setText("Излизане");
		}
	}

	private void update() {
		new SendLocationLogRequest(this);
		new GetChecksumRequest(this, "common_files");
		new GetChecksumRequest(this, "xml_categories");
//		new GetChecksumRequest(this, "xml_series");
//		new GetChecksumRequest(this, "xml_models");
//		new GetChecksumRequest(this, "xml_products");
		SamsungRequests.getExecutor().execute();
	}

	public void processData() {
		loadView();
	}

	private void loadView() {
		if (reloadBottomButtons()) {
			List<Category> cats = dataSource.getCategories(firstCategory
					.getId());
			gridView.setAdapter(new CategoryAdapter(this, cats,
					R.layout.grid_item, productsDataSource
							.getNumberOfProducts(cats)));
			gridView.setOnItemClickListener(new OnSubCategoryClickListener(
					this, firstCategory.getName(), seriesDataSource,
					currentCategory));

		}
		// List<Category> categories = dataSource.getCategories(0);
		// if (categories.size() != 0) {
		// firstCategory = categories.get(0);
		// // System.out.println();
		//
		// List<Category> cats = dataSource.getCategories(firstCategory
		// .getId());
		// gridView.setAdapter(new CategoryAdapter(this, cats,
		// R.layout.grid_item, productsDataSource
		// .getNumberOfProducts(cats)));
		// if (currentCategory == null) {
		// if (categories.size() != 0) {
		// currentCategory = categories.get(0).getId();
		// }
		// }
		// gridView.setOnItemClickListener(new OnSubCategoryClickListener(
		// this, firstCategory.getName(), seriesDataSource,
		// new ArrayList<Category>(), currentCategory));
		//
		// for (Category c : categories) {
		// if (c.getId() == currentCategory) {
		// buttonsContainer.addView(new MenuButton(mContext, c,
		// headerBar, true));
		// } else {
		// buttonsContainer.addView(new MenuButton(mContext, c,
		// headerBar));
		// }
		// }
		// }
	}

	@Override
	protected void onPause() {
		super.onPause();
		dataSource.close();
		seriesDataSource.close();
		productsDataSource.close();
		modelsDataSource.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
		dataSource.open();
		seriesDataSource.open();
		productsDataSource.open();
		modelsDataSource.open();

		reloadBottomButtons();
	}

	private boolean reloadBottomButtons() {
		List<Category> categories = dataSource.getCategories(0);
		if (categories.size() != 0) {
			if (firstCategory == null) {
				firstCategory = categories.get(0);
			}

			if (currentCategory == null) {
				currentCategory = firstCategory.getId();
			}

			buttonsContainer.removeAllViews();
			for (Category c : categories) {
				if (c.getId() == currentCategory) {
					buttonsContainer.addView(new MenuButton(mContext, c,
							headerBar, true));
				} else {
					buttonsContainer.addView(new MenuButton(mContext, c,
							headerBar));
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_categories, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			SharedPreferences preferences = getSharedPreferences("settings", 0);
			SharedPreferences.Editor edit = preferences.edit();
			edit.putBoolean("remember", false);
			edit.apply();

			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (!Utilities.performBack(this)) {
			super.onBackPressed();
			Utilities.getHistory(mContext).clear();
		}
	}
}
