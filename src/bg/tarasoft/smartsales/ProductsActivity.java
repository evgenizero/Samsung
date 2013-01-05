package bg.tarasoft.smartsales;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bg.tarasoft.smartsales.R;
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
import bg.tarasoft.smartsales.utilities.Utilities;
import bg.tarasoft.smartsales.views.HeaderBar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
		updateText.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				ArrayList<ProductsGroup> categoriesForBar = ((ProductsActivity) mContext).getHeaderBar();
				Toast.makeText(mContext, "Updating all", Toast.LENGTH_SHORT).show();
				new GetChecksumRequest(mContext, products, categoriesForBar);
				SamsungRequests.getExecutor().execute();
	
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
				gridView.setAdapter(new ProductAdapterNew(this, products));
			} else {
				categoryId = extras.getInt("serieId");
				products = dataSource.getProductsBySerie(categoryId);
				gridView.setAdapter(new ProductAdapterNew(this, products));
				logType = LoggedActivity.SERIE;
				System.out.println("ELEMENTS: "
						+ dataSource.getProductsBySerie(categoryId));

			}
			// ADD ACTIVITY TO LOG
			Utilities.addToLog(getApplicationContext(), categoryId,logType);
			
			
			
			SeriesDataSource sDS = new SeriesDataSource(mContext);
			sDS.open();
			categoriesForBar = (ArrayList<ProductsGroup>) extras
					.get("headerBar");
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
			headerBar.setCategories(categoriesForBar);
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

	public ArrayList<ProductsGroup> getHeaderBar() {
		return (ArrayList<ProductsGroup>) extras.get("headerBar");
	}

	@Override
	protected void onPause() {
		super.onPause();
		dataSource.close();
	}
}
