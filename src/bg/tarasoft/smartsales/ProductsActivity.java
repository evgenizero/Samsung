package bg.tarasoft.smartsales;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bg.tarasoft.smartsales.adapters.ProductAdapterNew;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.LoggedActivity;
import bg.tarasoft.smartsales.bean.Product;
import bg.tarasoft.smartsales.bean.ProductsGroup;
import bg.tarasoft.smartsales.bean.Serie;
import bg.tarasoft.smartsales.database.CategoryDataSource;
import bg.tarasoft.smartsales.database.LogsDataSource;
import bg.tarasoft.smartsales.database.ProductDataSource;
import bg.tarasoft.smartsales.database.SeriesDataSource;
import bg.tarasoft.smartsales.requests.GetChecksumRequest;
import bg.tarasoft.smartsales.requests.SamsungRequests;
import bg.tarasoft.smartsales.samsung.R;
import bg.tarasoft.smartsales.utilities.Utilities;
import bg.tarasoft.smartsales.views.HeaderBar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductsActivity extends Activity {

	private ProductDataSource dataSource;

	private GridView gridView;
	private Context mContext;

	private HeaderBar headerBar;

	private ArrayList<ProductsGroup> categoriesForBar;

	private Bundle extras;

	private List<Product> products;

	private TextView updateText;

	private int logType;
	
	private ProductAdapterNew adapter;
	private View compareButton;
	
	public Integer checksNum = new Integer(0);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Keep screen on
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);

		dataSource = new ProductDataSource(this);
		dataSource.open();
		mContext = this;
		headerBar = (HeaderBar) findViewById(R.id.header_bar);
		updateText = (TextView) findViewById(R.id.update_all_text);
		compareButton = findViewById(R.id.compare_button);
		compareButton.setVisibility(View.GONE);
		categoriesForBar = Utilities.getHistory(this);

		SharedPreferences preferences = getSharedPreferences("settings", 0);
		if (preferences.getInt("updateAll", 0) == 0) {
			updateText.setVisibility(View.GONE);
		}

		updateText.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (products.size() != 0) {
					
					Toast.makeText(mContext, "Updating all", Toast.LENGTH_SHORT)
							.show();
					new GetChecksumRequest(mContext, products);
					SamsungRequests.getExecutor().execute();
				}

			}
		});

		// pgv = (ProductGridView) findViewById(R.id.productsGrid);

		gridView = (GridView) findViewById(R.id.productsGrid);

		extras = getIntent().getExtras();
		if (extras != null) {
			int categoryId = 0;
			Object o = extras.get("categoryId");
			if (o != null) {
				categoryId = (Integer) o;
				logType = LoggedActivity.CATEGORY;

				products = dataSource.getProducts(categoryId);
				adapter = new ProductAdapterNew(this, products);
				gridView.setAdapter(adapter);
			} else {
				categoryId = extras.getInt("serieId");
				products = dataSource.getProductsBySerie(categoryId);
				adapter = new ProductAdapterNew(this, products);
				gridView.setAdapter(adapter);
				logType = LoggedActivity.SERIE;
				System.out.println("ELEMENTS: "
						+ dataSource.getProductsBySerie(categoryId));

			}
			// ADD ACTIVITY TO LOG
			Utilities.addToLog(getApplicationContext(), categoryId, logType);

			SeriesDataSource sDS = new SeriesDataSource(mContext);
			sDS.open();
			System.out.println("RETRIEVING SERIE WITH ID: "
					+ extras.getInt("serieId"));
			Serie serie = sDS.getSerie(extras.getInt("serieId"));
			sDS.close();
			if (serie != null) {
				System.out.println("SERIE: " + serie.getName());
				if (extras.getBoolean("addToBar")) {
					categoriesForBar.add(serie);
				}
			}
			Utilities.addHistoryToBar(this, headerBar);
			dataSource.close();
		}
	}

	public void onSmartSalesClick(View v) {
		Intent intent = new Intent(this,
				bg.tarasoft.smartsales.MainCategories.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		dataSource.open();
	}

	@Override
	protected void onPause() {
		super.onPause();
		dataSource.close();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Utilities.performBack(this);
	}
	
	public List<Integer> getCompareIds() {
		return adapter == null ? null : adapter.getSelectedIds();
	}
	
	public void setCompareButtonVisibility(int visibility) {
		compareButton.setVisibility(visibility);
	}
}
