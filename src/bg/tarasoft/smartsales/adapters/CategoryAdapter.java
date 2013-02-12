package bg.tarasoft.smartsales.adapters;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.cache.Cache;
import bg.tarasoft.smartsales.listeners.OnCategoryListItemClickListener;
import bg.tarasoft.smartsales.requests.DownloadImagesTask;
import bg.tarasoft.smartsales.samsung.R;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryAdapter extends BaseAdapter {

	private Context context;
	private List<Category> categories;
	private int layoutId;
	
	private static final int LIST_ITEM_LEFT = 0;
	private static final int LIST_ITEM_RIGHT = 1;
	private static final int LAYOUTS_MAX_COUNT = 2;

	private class CategoryHolder {
		ImageView image;
		TextView text;
	}

	public CategoryAdapter(Context context, List<Category> categories,
			int layoutId) {
		this(context, categories);
		this.layoutId = layoutId;
	}

	public CategoryAdapter(Context context, List<Category> categories) {
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

	@Override
	public int getItemViewType(int position) {
		if (position % 2 != 0) {
			return LIST_ITEM_LEFT;
		} else {
			return LIST_ITEM_RIGHT;
		}
	}

	@Override
	public int getViewTypeCount() {
		return LAYOUTS_MAX_COUNT;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		CategoryHolder holder;
		if (convertView == null || convertView.getTag() == null) {
			if (layoutId != 0) {
				convertView = View.inflate(context, layoutId, null);
			} else {
				int layout = getItemViewType(position);
				if(layout == LIST_ITEM_LEFT) {
					convertView = View.inflate(context, R.layout.list_item_left, null);
				} else {
					convertView = View.inflate(context, R.layout.list_item, null);
				}
			}
			holder = new CategoryHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.text = (TextView) convertView.findViewById(R.id.text);

			convertView.setTag(holder);
		} else {
			holder = (CategoryHolder) convertView.getTag();
		}

		Category item = (Category) getItem(position);

		String url = item.getImageUrl();

		if (url != null) {
			Bitmap bm = Cache.getCacheFile(url);
			if (bm == null) {
				holder.image.setTag(url);
				new DownloadImagesTask().execute(holder.image);
			} else {
				holder.image.setImageBitmap(bm);
			}
		} else {
			holder.image.setImageResource(R.drawable.no_photo);
			holder.image.setTag("true");

		}

		holder.text.setText(item.getName());

		convertView.requestLayout();
		return convertView;
	}
}
