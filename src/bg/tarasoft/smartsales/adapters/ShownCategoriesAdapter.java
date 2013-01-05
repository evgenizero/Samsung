package bg.tarasoft.smartsales.adapters;

import java.util.List;

import bg.tarasoft.smartsales.R;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.listeners.OnChooseCategoryListener;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ShownCategoriesAdapter extends BaseAdapter {
	private Context context;
	private List<Category> categories;

	private class ViewHolder {
		TextView categoryName;
		CheckBox checkBox;
	}

	public ShownCategoriesAdapter(Context context, List<Category> categories) {
		this.context = context;
		this.categories = categories;
	}

	public int getCount() {
		return categories.size();
	}

	public Object getItem(int position) {
		return categories.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Category item = (Category) getItem(position);
		ViewHolder holder;
		//if (convertView == null || convertView.getTag() == null) {
			convertView = View.inflate(context, R.layout.show_categories_item, null);
			holder = new ViewHolder();
			holder.categoryName = (TextView) convertView.findViewById(R.id.category_name);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

			convertView.setTag(holder);
			
			
			
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}

		
		holder.categoryName.setText(item.getName());
		
		System.out.println(String.valueOf(item.getIsShown()));
		
		if(item.getIsShown() == 1) {
			holder.checkBox.setChecked(true);
		} else {
			holder.checkBox.setChecked(false);
		}
		
		holder.checkBox.setOnCheckedChangeListener(new OnChooseCategoryListener(item));

		return convertView;
	}
	
	public List<Category> getCategories() {
		
		for(Category c : categories) {
			System.out.println("ITEM: " + c.getIsShown());
		}
		
		return categories;
	}
}