package bg.tarasoft.smartsales;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import bg.tarasoft.smartsales.adapters.CategoryAdapter;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.Checksum;
import bg.tarasoft.smartsales.bean.LocationLog;
import bg.tarasoft.smartsales.bean.LoggedActivity;
import bg.tarasoft.smartsales.database.CategoryDataSource;
import bg.tarasoft.smartsales.database.ChecksumDataSource;
import bg.tarasoft.smartsales.database.MySQLiteOpenHelper;
import bg.tarasoft.smartsales.listeners.OnCategoryListItemClickListener;
import bg.tarasoft.smartsales.listeners.OnSettingsButtonClick;
import bg.tarasoft.smartsales.requests.*;
import bg.tarasoft.smartsales.samsung.R;
import bg.tarasoft.smartsales.utilities.Utilities;
import bg.tarasoft.smartsales.views.HeaderBar;
import bg.tarasoft.smartsales.views.HeaderLabel;

public class MainCategories extends Activity {

	private ListView listCategories;
	private CategoryDataSource dataSource;
	private HeaderBar headerBar;

	private Button settingsButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_categories);

		if (getIntent().getExtras() != null
				&& getIntent().getExtras().getBoolean("toReleseLock")) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			WakeLock mWakeLock = pm
					.newWakeLock(
							(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP),
							"YourServie");
			if (mWakeLock.isHeld()) {
				mWakeLock.release();
			}
			KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
			KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
			keyguardLock.reenableKeyguard();
		}
		// Keep screen on
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);

		// LoggedActivity.sendLog(this);
		headerBar = (HeaderBar) findViewById(R.id.header_bar);
		Category cat = new Category();
		cat.setName("first");

		settingsButton = (Button) findViewById(R.id.settingsButton);
		settingsButton.setOnTouchListener(new OnSettingsButtonClick(this));

		listCategories = (ListView) findViewById(R.id.list_categories);

		dataSource = new CategoryDataSource(this);
		dataSource.open();

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

	}

	public void processData() {
		dataSource.open();
		List<Category> categories = dataSource.getCategories(0);
		CategoryAdapter adapter = new CategoryAdapter(this, categories);
		listCategories.setAdapter(adapter);
		listCategories
				.setOnItemClickListener(new OnCategoryListItemClickListener(
						this, headerBar));
	}

	@Override
	protected void onResume() {
		super.onResume();
		dataSource.open();
		List<Category> categories = dataSource.getCategories(0);
		CategoryAdapter adapter = new CategoryAdapter(this, categories);
		listCategories.setAdapter(adapter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		dataSource.close();
	}

	public void onSmartSalesClick(View v) {

	}
	
}
