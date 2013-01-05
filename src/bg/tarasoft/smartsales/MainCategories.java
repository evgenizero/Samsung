package bg.tarasoft.smartsales;

import java.util.List;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import bg.tarasoft.smartsales.R;
import bg.tarasoft.smartsales.adapters.CategoryAdapter;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.Checksum;
import bg.tarasoft.smartsales.bean.LoggedActivity;
import bg.tarasoft.smartsales.database.CategoryDataSource;
import bg.tarasoft.smartsales.database.ChecksumDataSource;
import bg.tarasoft.smartsales.database.MySQLiteOpenHelper;
import bg.tarasoft.smartsales.listeners.OnCategoryListItemClickListener;
import bg.tarasoft.smartsales.listeners.OnSettingsButtonClick;
import bg.tarasoft.smartsales.requests.*;
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

		//LoggedActivity.sendLog(this);
		headerBar = (HeaderBar) findViewById(R.id.header_bar);
		Category cat = new Category();
		cat.setName("first");

		settingsButton = (Button) findViewById(R.id.settingsButton);
		settingsButton.setOnTouchListener(new OnSettingsButtonClick(this));
		
		listCategories = (ListView) findViewById(R.id.list_categories);

		dataSource = new CategoryDataSource(this);
		dataSource.open();
		new GetChecksumRequest(this, "common_files");
		new GetChecksumRequest(this, "xml_categories");
		SamsungRequests.getExecutor().execute();
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
