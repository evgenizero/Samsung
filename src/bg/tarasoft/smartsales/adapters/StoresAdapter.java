package bg.tarasoft.smartsales.adapters;

import java.util.List;

import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.Store;
import bg.tarasoft.smartsales.bean.StoreType;
import bg.tarasoft.smartsales.database.CategoryDataSource;
import bg.tarasoft.smartsales.database.StoresDataSource;
import bg.tarasoft.smartsales.listeners.OnChooseCategoryListener;
import bg.tarasoft.smartsales.samsung.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class StoresAdapter extends BaseAdapter {
	private Context context;
	private List<StoreType> storeRetails;
	private SharedPreferences preferences;
	private int storeId = -1;
	
	private class ViewHolder {
		TextView storeName;
		RadioButton radioButton;
	}

	public StoresAdapter(Context context, List<StoreType> storeRetails) {
		this.context = context;
		this.storeRetails = storeRetails;
		preferences = context.getSharedPreferences("settings", 0);
		storeId = preferences.getInt("store_id", -1);
	}

	public int getCount() {
		return storeRetails.size();
	}

	public Object getItem(int position) {
		return storeRetails.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final StoreType item = (StoreType) getItem(position);
		final ViewHolder holder;
		//if (convertView == null || convertView.getTag() == null) {
			convertView = View.inflate(context, R.layout.store_list_item, null);
			holder = new ViewHolder();
			holder.storeName = (TextView) convertView.findViewById(R.id.store_name);
			holder.radioButton = (RadioButton) convertView.findViewById(R.id.radioButton1);

//			holder.radioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//				
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//					if(isChecked) {
//						item.setIsChosen(true);
//						storeId = item.getStoreID();
//						for(Store store : stores) {
//							if(store.getStoreID() != item.getStoreID()) {
//								store.setIsChosen(false);
//							}
//						}
//						notifyDataSetChanged();
//					} 
//					
//				}
//			});
			
			convertView.setTag(holder);
			
			
			
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}

		
		holder.storeName.setText(item.getName());
		
		if(item.getId() == storeId) {
			holder.radioButton.setChecked(true);
		} else {
			holder.radioButton.setChecked(false);
		}
		
		return convertView;
	}
	
	public List<StoreType> getData() {
		return storeRetails;
	}
}