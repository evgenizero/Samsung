package bg.tarasoft.smartsales;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import bg.tarasoft.smartsales.R;
import bg.tarasoft.smartsales.adapters.ProductAdapterNew;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.LoggedActivity;
import bg.tarasoft.smartsales.bean.ProductsGroup;
import bg.tarasoft.smartsales.database.CategoryDataSource;
import bg.tarasoft.smartsales.utilities.Utilities;
import bg.tarasoft.smartsales.views.HeaderBar;

public class HTMLPlayerActivity extends Activity {
	WebView view;
	private Context mContext;
	private HeaderBar headerBar;
	private ArrayList<ProductsGroup> categoriesForBar;
	public static final String BASE_FOLDER = "smartsales";
	public static final String PRODUCTS_FOLDER = "products";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.html);
		
		// Keep screen on
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
				
		mContext = this;
		headerBar = (HeaderBar) findViewById(R.id.header_bar);
		view = (WebView) findViewById(R.id.webView);

		view = (WebView) findViewById(R.id.webView);
		view.getSettings().setJavaScriptEnabled(true);
		view.getSettings().setSupportZoom(true);
		view.setVerticalScrollBarEnabled(false);
		view.setHorizontalScrollBarEnabled(false);
		view.getSettings().setUseWideViewPort(false);
		view.getSettings().setLoadWithOverviewMode(true);
		view.getSettings().setUseWideViewPort(true);
		// view.loadUrl("http://cube.fb-software.com");

		final Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int categoryId = 0;
			Object o = extras.get("categoryId");
			if (o != null) {
				categoryId = (Integer) o;

			} else {
				categoryId = extras.getInt("serieId");

			}

			int value = extras.getInt("productId");

			categoriesForBar = (ArrayList<ProductsGroup>) extras
					.get("headerBar");
			// categoriesForBar.add(dataSource.getCategory(id));

			System.out
					.println("CATEGORIES FOR BAR: " + categoriesForBar.size());
			for (ProductsGroup c : categoriesForBar) {
				System.out.println(c.getName());
			}
			headerBar.setCategories(categoriesForBar);
			loadProduct(value);
			Utilities.addToLog(getApplicationContext(), value, LoggedActivity.PRODUCT);
			
		}
	}

	public void loadProduct(int productId) {
		// view.loadUrl("file:///mnt/sdcard/smartsales/products/682/682.html");
		view.loadUrl("file://" + Environment.getExternalStorageDirectory()
				+ "/" + BASE_FOLDER + "/" + PRODUCTS_FOLDER + "/" + productId
				+ "/" + productId + ".html");
	}

	public void onSmartSalesClick(View v) {
		Intent intent = new Intent(this, MainCategories.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}
