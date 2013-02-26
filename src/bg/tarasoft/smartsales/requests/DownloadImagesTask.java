package bg.tarasoft.smartsales.requests;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import bg.tarasoft.smartsales.cache.Cache;
import bg.tarasoft.smartsales.samsung.R;

public class DownloadImagesTask extends AsyncTask<ImageView, Void, Bitmap> {

	private ImageView imageView = null;

	@Override
	protected Bitmap doInBackground(ImageView... imageViews) {
		this.imageView = imageViews[0];
		return downloadImage((String) imageView.getTag());
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (result != null) {
			imageView.setImageBitmap(result);
			Cache.saveCacheFile((String) imageView.getTag(), result);
		} else {
			imageView.setImageResource(R.drawable.no_photo);
		}
	}

	private synchronized Bitmap downloadImage(String url) {

		Bitmap bitmap = null;
		InputStream in = null;
		try {
			in = OpenHttpConnection(url);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 16;
			bitmap = BitmapFactory.decodeStream(in, null, options);
			in.close();
		} catch (Exception e) {
			Log.v("Error", "error");
			return null;
		}

		return bitmap;
	}

	private InputStream OpenHttpConnection(String urlString) throws IOException {
		InputStream in = null;
		int response = -1;

		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection)) {
			return null;
		}

		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			}
		} catch (Exception ex) {
			return null;
		}
		return in;
	}
}
