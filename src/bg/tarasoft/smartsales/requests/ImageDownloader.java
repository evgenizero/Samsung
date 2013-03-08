package bg.tarasoft.smartsales.requests;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import bg.tarasoft.smartsales.cache.Cache;
import bg.tarasoft.smartsales.samsung.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

public class ImageDownloader {
	
	private ImageView image;
	
	public void downloadImage(ImageView imageView) {
		this.image = imageView;
		new Thread(new Runnable() {
			public void run() {
				//final Bitmap b = fetchImage((String) image.getTag());
				fetchImage((String) image.getTag());
				image.post(new Runnable() {
					public void run() {
//						if (b != null) {
//							image.setImageBitmap(b);
//							Cache.saveCacheFile((String) image.getTag(), b);
//						} else {
//							image.setImageResource(R.drawable.no_photo);
//						}
						Bitmap b = Cache.getCacheFile((String)image.getTag());
						if(b != null) {
							image.setImageBitmap(b);
						} else {
							image.setImageResource(R.drawable.no_photo);
						}

					}
				});
			}
		}).start();
	}

	private synchronized Bitmap fetchImage(String url) {

		Bitmap bitmap = null;
		InputStream in = null;
		try {
			in = OpenHttpConnection(url);
			
//			BitmapFactory.Options options = new BitmapFactory.Options();
//			options.inJustDecodeBounds = true;
//			BitmapFactory.decodeStream(in, null, options);
//			options.inSampleSize = calculateInSampleSize(options, 93, 130);
//			options.inJustDecodeBounds = false;
//			
			//bitmap = BitmapFactory.decodeStream(in, null, options);
			
			Cache.saveCacheFile(url, in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return bitmap;
	}

	private InputStream OpenHttpConnection(String urlString) throws IOException {
		InputStream in = null;
		int response = -1;

		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();
		conn.setUseCaches(true);

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
	
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		
		System.out.println("HEIGHT: " + height);
		System.out.println("WIDTH: " + width);
		
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
}