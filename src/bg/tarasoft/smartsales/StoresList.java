package bg.tarasoft.smartsales;

import java.util.List;

import bg.tarasoft.smartsales.adapters.StoreRetailsBaseAdapter;
import bg.tarasoft.smartsales.adapters.StoresAdapter;
import bg.tarasoft.smartsales.bean.Store;
import bg.tarasoft.smartsales.database.StoreRetailDataSource;
import bg.tarasoft.smartsales.database.StoresDataSource;
import bg.tarasoft.smartsales.samsung.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class StoresList extends Activity{
	
	private ExpandableListView storesListView;
	private StoreRetailDataSource dataSource;
	private StoreRetailsBaseAdapter adapter;
	
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stores_list);
	
		storesListView = (ExpandableListView) findViewById(R.id.listView1);
		
		storesListView.setCacheColorHint(Color.WHITE);
		
		dataSource = new StoreRetailDataSource(this);
		dataSource.open();
		
		adapter = new StoreRetailsBaseAdapter(this, dataSource.getStoreTypes());
		
		storesListView.setAdapter(adapter);
		
		preferences = getSharedPreferences("settings", 0);
		editor = preferences.edit();
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		dataSource.close();
		List<Store> data = adapter.getData();
		for(Store store : data) {
			if(store.isChosen()) {
				editor.putInt("store_id", store.getStoreID());
				editor.apply();
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		dataSource.open();
	}
}
