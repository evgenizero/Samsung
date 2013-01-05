package bg.tarasoft.smartsales.adapters;

import java.util.List;

import bg.tarasoft.smartsales.R;
import bg.tarasoft.smartsales.bean.Category;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {
	private Context context;
	private List<Category> categories;

	private class ViewHolder {
		ImageView image;
		TextView text;
	}

	public GridAdapter(Context context, List<Category> categories) {
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
		ViewHolder holder;
		if (convertView == null || convertView.getTag() == null) {
			convertView = View.inflate(context, R.layout.grid_item, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.text);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Category item = (Category) getItem(position);
		
		holder.text.setText(item.getName());
		
		return convertView;
	}
}