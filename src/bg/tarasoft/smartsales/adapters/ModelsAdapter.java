package bg.tarasoft.smartsales.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import bg.tarasoft.smartsales.bean.Model;
import bg.tarasoft.smartsales.bean.Serie;
import bg.tarasoft.smartsales.cache.Cache;
import bg.tarasoft.smartsales.requests.ImageDownloader;
import bg.tarasoft.smartsales.samsung.R;

public class ModelsAdapter extends BaseAdapter {

	private Context context;
	private List<Model> models;
	private int layoutId;

	private class ViewHolder {
		ImageView image;
		TextView text;
	}

	public ModelsAdapter(Context context, List<Model> models, int layoutId) {
		this(context, models);
		this.layoutId = layoutId;
	}

	public ModelsAdapter(Context context, List<Model> models) {
		this.context = context;
		this.models = models;
	}

	public int getCount() {
		return models.size();
	}

	public Object getItem(int position) {
		return models.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null || convertView.getTag() == null) {
			convertView = View.inflate(context, layoutId, null);

			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.text = (TextView) convertView.findViewById(R.id.text);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Model item = (Model) getItem(position);

		String url = item.getModelPicUrl();

		if (url != null) {
			Bitmap bm = Cache.getCacheFile(url);
			if (bm == null) {
				holder.image.setTag(url);
				new ImageDownloader().downloadImage(holder.image);
			} else {
				holder.image.setImageBitmap(bm);
			}
		} else {
			holder.image.setImageResource(R.drawable.no_photo);
			holder.image.setTag("true");

		}

		holder.text.setText(item.getModelName());

		convertView.requestLayout();
		return convertView;
	}
}
