package bg.tarasoft.smartsales.adapters;

import java.util.ArrayList;
import java.util.List;

import bg.tarasoft.smartsales.bean.Store;
import bg.tarasoft.smartsales.bean.StoreType;
import android.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class StoreRetailsBaseAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<StoreType> storeRetails;
	private SharedPreferences preferences;
	private int storeId = -1;

	private class ViewHolder {
		TextView storeName;
		RadioButton radioButton;
	}

	public StoreRetailsBaseAdapter(Context context, List<StoreType> storeRetails) {
		this.context = context;
		this.storeRetails = storeRetails;
		preferences = context.getSharedPreferences("settings", 0);
		storeId = preferences.getInt("store_id", -1);
	}

	public Object getChild(int arg0, int arg1) {
		return storeRetails.get(arg0).getStores().get(arg1);
	}

	public long getChildId(int arg0, int arg1) {
		return arg1;
	}

	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		final Store store = (Store) getChild(arg0, arg1);
		final int groupPosition = arg0;
		ViewHolder holder;
		if (arg3 == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			arg3 = infalInflater.inflate(
					bg.tarasoft.smartsales.samsung.R.layout.store_list_item,
					null);
		}
		holder = new ViewHolder();
		holder.storeName = (TextView) arg3
				.findViewById(bg.tarasoft.smartsales.samsung.R.id.store_name);
		holder.radioButton = (RadioButton) arg3
				.findViewById(bg.tarasoft.smartsales.samsung.R.id.radioButton1);

		holder.radioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					store.setIsChosen(true);
					storeId = store.getStoreID();
					for(StoreType storeRet : storeRetails) {
						for(Store store : storeRet.getStores()) {
							if(store.getStoreID() != store.getStoreID()) {
								store.setIsChosen(false);
							}
						}
					}
					notifyDataSetChanged();
				} 
				
			}
		});
		
		holder.storeName.setText(store.getStoreName());

		if(store.getStoreID() == storeId) {
			holder.radioButton.setChecked(true);
		} else {
			holder.radioButton.setChecked(false);
		}
		
		return arg3;

	}

	public int getChildrenCount(int arg0) {
		return storeRetails.get(arg0).getStores().size();
	}

	public Object getGroup(int arg0) {
		return storeRetails.get(arg0);
	}

	public int getGroupCount() {
		return storeRetails.size();
	}

	public long getGroupId(int arg0) {
		return arg0;
	}

	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		StoreType storeRetail = (StoreType) getGroup(arg0);
		if (arg2 == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			arg2 = infalInflater
					.inflate(
							bg.tarasoft.smartsales.samsung.R.layout.store_list_item_parent,
							null);
		}
		TextView tv = (TextView) arg2
				.findViewById(bg.tarasoft.smartsales.samsung.R.id.store_name_parent);
		tv.setText(storeRetail.getName());
		// TODO Auto-generated method stub
		return arg2;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	public List<Store> getData() {
		List<Store> stores = new ArrayList<Store>();
		for(StoreType storeRet : storeRetails) {
			stores.addAll(storeRet.getStores());
		}
		return stores;
	}

}
