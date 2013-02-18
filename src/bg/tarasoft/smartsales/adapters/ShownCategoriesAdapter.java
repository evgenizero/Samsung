package bg.tarasoft.smartsales.adapters;

import java.util.List;

import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.database.CategoryDataSource;
import bg.tarasoft.smartsales.listeners.OnChooseCategoryListener;
import bg.tarasoft.smartsales.samsung.R;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ShownCategoriesAdapter extends BaseAdapter {
	private Context context;
	private List<Category> categories;
	private CategoryDataSource dataSource;

	private class ViewHolder {
		TextView categoryName;
		CheckBox checkBox;
	}

	public ShownCategoriesAdapter(Context context, List<Category> categories, CategoryDataSource dataSource) {
		this.context = context;
		this.categories = categories;
		this.dataSource = dataSource;
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
		final Category item = (Category) getItem(position);
		ViewHolder holder;
		//if (convertView == null || convertView.getTag() == null) {
			convertView = View.inflate(context, R.layout.show_categories_item, null);
			holder = new ViewHolder();
			holder.categoryName = (TextView) convertView.findViewById(R.id.category_name);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

			holder.categoryName.setOnClickListener(new OnClickListener() {
				
				public void onClick(View arg0) {
					List<Category> newData = dataSource.getAllHiddenCategories(item.getId());
					if(newData.size() != 0) {
						categories = newData;
						notifyDataSetChanged();
					}
				}
			});
			
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
		
		holder.checkBox.setOnCheckedChangeListener(new OnChooseCategoryListener(context,item,dataSource));

		return convertView;
	}
	
	public List<Category> getCategories() {
		
		for(Category c : categories) {
			System.out.println("ITEM: " + c.getIsShown());
		}
		
		return categories;
	}
}