package bg.tarasoft.smartsales.adapters;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.Serie;
import bg.tarasoft.smartsales.cache.Cache;
import bg.tarasoft.smartsales.listeners.OnCategoryListItemClickListener;
import bg.tarasoft.smartsales.requests.DownloadImagesTask;
import bg.tarasoft.smartsales.requests.ImageDownloader;
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

public class SeriesAdapter extends BaseAdapter {

	private Context context;
	private List<Serie> series;
	private int layoutId;

	private class ViewHolder {
		ImageView image;
		TextView text;
	}

	public SeriesAdapter(Context context, List<Serie> series, int layoutId) {
		this(context, series);
		this.layoutId = layoutId;
	}

	public SeriesAdapter(Context context, List<Serie> series) {
		this.context = context;
		this.series = series;
	}

	public int getCount() {
		return series.size();
	}

	public Object getItem(int position) {
		return series.get(position);
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

		Serie item = (Serie) getItem(position);

		String url = item.getPicUrl();

		if (url != null) {
			Bitmap bm = Cache.getCacheFile(url);
			if (bm == null) {
				holder.image.setTag(url);
				//new DownloadImagesTask().execute(holder.image);
				//downloader.download(url, holder.image);
				new ImageDownloader().downloadImage(holder.image);
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
