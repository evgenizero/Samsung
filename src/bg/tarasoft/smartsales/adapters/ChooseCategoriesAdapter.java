package bg.tarasoft.smartsales.adapters;

import java.util.List;

import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.CategoryParent;
import bg.tarasoft.smartsales.database.CategoryDataSource;
import bg.tarasoft.smartsales.listeners.OnChooseCategoryListener;
import bg.tarasoft.smartsales.samsung.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseCategoriesAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<CategoryParent> parentCategories;
	private CategoryDataSource dataSource;

	private class ViewHolder {
		TextView categoryName;
		CheckBox checkBox;
	}

	public ChooseCategoriesAdapter(Context context,
			List<CategoryParent> parentCategories, CategoryDataSource dataSource) {
		this.context = context;
		this.parentCategories = parentCategories;
		this.dataSource = dataSource;
	}

	public Object getChild(int arg0, int arg1) {
		return parentCategories.get(arg0).getCategories().get(arg1);
	}

	public long getChildId(int arg0, int arg1) {
		return arg1;
	}

	public View getChildView(int arg0, int arg1, boolean arg2,
			View convertView, ViewGroup arg4) {
		final Category item = (Category) getChild(arg0, arg1);
		ViewHolder holder;
		// if (convertView == null || convertView.getTag() == null) {
		convertView = View
				.inflate(context, R.layout.show_categories_item, null);
		holder = new ViewHolder();
		holder.categoryName = (TextView) convertView
				.findViewById(R.id.category_name);
		holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

		convertView.setTag(holder);

		// } else {
		// holder = (ViewHolder) convertView.getTag();
		// }

		holder.checkBox
				.setOnCheckedChangeListener(new OnChooseCategoryListener(context,item,
						dataSource));

		holder.categoryName.setText(item.getName());

		if (item.getIsShown() == 1) {
			holder.checkBox.setChecked(true);
		} else {
			holder.checkBox.setChecked(false);
		}

		// holder.checkBox.setOnCheckedChangeListener(new
		// OnChooseCategoryListener(item));

		return convertView;
	}

	public int getChildrenCount(int arg0) {
		return parentCategories.get(arg0).getCategories().size();
	}

	public Object getGroup(int arg0) {
		return parentCategories.get(arg0);
	}

	public int getGroupCount() {
		return parentCategories.size();
	}

	public long getGroupId(int arg0) {
		return arg0;
	}

	public View getGroupView(int arg0, boolean arg1, View convertView,
			ViewGroup arg3) {
		CategoryParent parent = (CategoryParent) getGroup(arg0);
		Category item = parent.getCategory();
		ViewHolder holder;
		// if (convertView == null || convertView.getTag() == null) {
		convertView = View
				.inflate(context, R.layout.show_categories_item, null);
		holder = new ViewHolder();
		holder.categoryName = (TextView) convertView
				.findViewById(R.id.category_name);
		holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

		convertView.setTag(holder);

		// } else {
		// holder = (ViewHolder) convertView.getTag();
		// }

		holder.checkBox
				.setOnCheckedChangeListener(new OnChooseCategoryListener(context,item,
						dataSource));

		holder.categoryName.setText(item.getName());

		if (item.getIsShown() == 1) {
			holder.checkBox.setChecked(true);
		} else {
			holder.checkBox.setChecked(false);
		}

		// holder.checkBox.setOnCheckedChangeListener(new
		// OnChooseCategoryListener(item));

		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

	public void setData(List<CategoryParent> parentCategories2) {
		this.parentCategories.clear();
		this.parentCategories = parentCategories2;
		notifyDataSetChanged();
	}

}
