package bg.tarasoft.smartsales.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class CacheStore {
	private static CacheStore INSTANCE = null;
	private HashMap<String, String> cacheMap;
	private HashMap<String, SoftReference<Bitmap>> bitmapMap;
	private static final String cacheDir = "/Android/data/bg.tarasoft.samsung_app/cache/";
	private static final String CACHE_FILENAME = ".cache";
	// private static final int MAX_CACHE_SIZE = 15728640;
	private static final int MAX_CACHE_SIZE = 31457280;

	@SuppressWarnings("unchecked")
	private CacheStore() {
		cacheMap = new HashMap<String, String>();
		bitmapMap = new HashMap<String, SoftReference<Bitmap>>();
		File fullCacheDir = new File(Environment.getExternalStorageDirectory()
				.toString(), cacheDir);
		if (!fullCacheDir.exists()) {
			Log.i("CACHE", "Directory doesn't exist");
			cleanCacheStart();
			return;
		}
		try {
			ObjectInputStream is = new ObjectInputStream(
					new BufferedInputStream(new FileInputStream(new File(
							fullCacheDir.toString(), CACHE_FILENAME))));

			cacheMap = (HashMap<String, String>) is.readObject();
			is.close();
		} catch (StreamCorruptedException e) {
			Log.i("CACHE", "Corrupted stream");
			cleanCacheStart();
		} catch (FileNotFoundException e) {
			Log.i("CACHE", "File not found");
			cleanCacheStart();
		} catch (IOException e) {
			Log.i("CACHE", "Input/Output error");
			cleanCacheStart();
		} catch (ClassNotFoundException e) {
			Log.i("CACHE", "Class not found");
			cleanCacheStart();
		}
	}

	private void cleanCacheStart() {
		cacheMap = new HashMap<String, String>();
		File fullCacheDir = new File(Environment.getExternalStorageDirectory()
				.toString(), cacheDir);
		fullCacheDir.mkdirs();
		createNoMedia(fullCacheDir);
	}

	private void createNoMedia(File fullCacheDir) {
		File noMedia = new File(fullCacheDir.toString(), ".nomedia");
		try {
			noMedia.createNewFile();
			Log.i("CACHE", "Cache created");
		} catch (IOException e) {
			Log.i("CACHE", "Couldn't create .nomedia file");
			e.printStackTrace();
		}
	}

	private synchronized static void createInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CacheStore();
		}
	}

	public static CacheStore getInstance() {
		if (INSTANCE == null)
			createInstance();
		return INSTANCE;
	}

	private void saveFileFromStream(InputStream input, FileOutputStream output) {
		try {
			try {
				final byte[] buffer = new byte[1024];
				int read;
				while ((read = input.read(buffer)) != -1)
					output.write(buffer, 0, read);

				output.flush();
			} finally {
				output.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void saveCacheFile(String cacheUri, InputStream input) {
		File fullCacheDir = new File(Environment.getExternalStorageDirectory()
				.toString(), cacheDir);

		File[] fileList = fullCacheDir.listFiles();

		int cacheSize = 0;
		for (File f : fileList) {
			cacheSize += f.length();
			if (cacheSize >= MAX_CACHE_SIZE) {
				freeCache(fileList);
				return;
			}
		}

		String fileLocalName = new SimpleDateFormat("ddMMyyhhmmssSSS")
				.format(new java.util.Date()) + ".PNG";
		File fileUri = new File(fullCacheDir.toString(), fileLocalName);
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(fileUri);
			// image.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			saveFileFromStream(input, outStream);
			outStream.flush();
			outStream.close();

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(fileUri.toString(), options);
			options.inSampleSize = calculateInSampleSize(options, 200, 200);
			options.inJustDecodeBounds = false;

			Bitmap bm = BitmapFactory.decodeFile(fileUri.toString(), options);
			saveFile(fileUri, bm, cacheUri, fileLocalName, fullCacheDir);

			cacheMap.put(cacheUri, fileLocalName);
			Log.i("CACHE", "Saved file " + cacheUri + " (which is now "
					+ fileUri.toString() + ") correctly");
			ObjectOutputStream os = new ObjectOutputStream(
					new BufferedOutputStream(new FileOutputStream(new File(
							fullCacheDir.toString(), CACHE_FILENAME))));
			os.writeObject(cacheMap);
			os.close();
		} catch (FileNotFoundException e) {
			Log.i("CACHE", "Error: File " + cacheUri + " was not found!");
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("CACHE", "Error: File could not be stuffed!");
			e.printStackTrace();
		} catch (Exception e) {
			Log.i("CACHE", "Error: unknown exception!");
		}

	}

	public synchronized void saveCacheFile(String cacheUri, Bitmap image) {
		File fullCacheDir = new File(Environment.getExternalStorageDirectory()
				.toString(), cacheDir);

		File[] fileList = fullCacheDir.listFiles();

		int cacheSize = 0;
		for (File f : fileList) {
			cacheSize += f.length();
			if (cacheSize >= MAX_CACHE_SIZE) {
				freeCache(fileList);
				return;
			}
		}

		String fileLocalName = new SimpleDateFormat("ddMMyyhhmmssSSS")
				.format(new java.util.Date()) + ".PNG";
		File fileUri = new File(fullCacheDir.toString(), fileLocalName);
		saveFile(fileUri, image, cacheUri, fileLocalName, fullCacheDir);
	}

	private void saveFile(File fileUri, Bitmap image, String cacheUri,
			String fileLocalName, File fullCacheDir) {
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(fileUri);
			image.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
			cacheMap.put(cacheUri, fileLocalName);
			Log.i("CACHE", "Saved file " + cacheUri + " (which is now "
					+ fileUri.toString() + ") correctly");
			bitmapMap.put(cacheUri, new SoftReference<Bitmap>(image));
			ObjectOutputStream os = new ObjectOutputStream(
					new BufferedOutputStream(new FileOutputStream(new File(
							fullCacheDir.toString(), CACHE_FILENAME))));
			os.writeObject(cacheMap);
			os.close();
		} catch (FileNotFoundException e) {
			Log.i("CACHE", "Error: File " + cacheUri + " was not found!");
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("CACHE", "Error: File could not be stuffed!");
			e.printStackTrace();
		} catch (Exception e) {
			Log.i("CACHE", "Error: unknown exception!");
		}
	}

	// TODO check for exceptions if any
	private void freeCache(File[] fileList) {
		if (fileList != null) {
			int deleteIndex = MAX_CACHE_SIZE / 2;
			for (int i = fileList.length - 1; i > 0; i--) {
				if (!(fileList[i].getName().equals(CACHE_FILENAME))
						&& !(fileList[i].getName().equals(".nomedia"))) {
					Log.v("FILE", fileList[i].getName());
					fileList[i].delete();
					int tmpSize = 0;
					for (File f : fileList) {
						tmpSize += f.length();
					}
					if (tmpSize <= deleteIndex) {
						break;
					}
				}
			}
		}
		int size = 0;
		for (File f : fileList) {
			size += f.length();
		}
		Log.i("DELETED", String.valueOf(size));
	}

	public Bitmap getCacheFile(String cacheUri) {
		if (bitmapMap.containsKey(cacheUri)) {
			SoftReference<Bitmap> softReference = bitmapMap.get(cacheUri);
			Log.i("cache getting", "da be ");
			return softReference.get();
		}

		if (!cacheMap.containsKey(cacheUri)) {
			Log.i("problem in cache", "no such uri in cache");
			return null;
		}
		String fileLocalName = cacheMap.get(cacheUri);
		File fullCacheDir = new File(Environment.getExternalStorageDirectory()
				.toString(), cacheDir);
		File fileUri = new File(fullCacheDir.toString(), fileLocalName);
		if (!fileUri.exists()) {
			Log.i("problem in cache", "no such file");
			return null;
		}

		Log.i("CACHE", "File " + cacheUri + " has been found in the Cache");

		Bitmap bm = BitmapFactory.decodeFile(fileUri.toString());
		bitmapMap.put(cacheUri, new SoftReference<Bitmap>(bm));

		return bm;
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