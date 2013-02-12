package bg.tarasoft.smartsales.requests;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import bg.tarasoft.smartsales.HTMLPlayerActivity;
import bg.tarasoft.smartsales.bean.Product;
import bg.tarasoft.smartsales.bean.ProductsGroup;
import bg.tarasoft.smartsales.parser.ParseXml;
import bg.tarasoft.smartsales.utilities.Decompress;
import bg.tarasoft.smartsales.utilities.Utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class GetProductHTML extends AsyncTask<String, String, String> {
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog progress;

	// private String filePath = "/mnt/sdcard/file.zip";

	private static final String BASE_FOLDER = "smartsales";
	private static final String PRODUCTS_FOLDER = "products";
	private int productId;
	private Context context;
	private String filePath;
	private ArrayList<ProductsGroup> categoriesForBar;
	private boolean toOpen;
	private Context mContext;
	private List<Product> products;

	public GetProductHTML(int productId, Context context,
			ArrayList<ProductsGroup> categoriesForBar, boolean toOpen, List<Product> products) {
		this.toOpen = toOpen;
		this.products = products;
		this.mContext = context;
		progress = new ProgressDialog(context);
		this.context = context;
		this.categoriesForBar = categoriesForBar;
		this.productId = productId;
		filePath = Environment.getExternalStorageDirectory() + "/"
				+ BASE_FOLDER + "/" + productId + ".zip";
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		createFolder();
		progress.setMessage("Downloading...");
		progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progress.setCancelable(false);
		progress.show();
	}

	private void createFolder() {
		File folder = new File(Environment.getExternalStorageDirectory() + "/"
				+ BASE_FOLDER);

		folder.mkdir();
		File folder2 = new File(Environment.getExternalStorageDirectory() + "/"
				+ BASE_FOLDER + "/" + PRODUCTS_FOLDER);
		folder2.mkdir();
	}

	@Override
	protected String doInBackground(String... aurl) {
		int count;

		try {

			String zipUrl = getZipUrl(aurl);

			URL zipUurl = new URL(zipUrl);
			URLConnection zipConn = zipUurl.openConnection();
			zipConn.connect();

			int zipLenghtOfFile = zipConn.getContentLength();
			Log.d("ANDRO_ASYNC", "Lenght of file: " + zipLenghtOfFile);

			InputStream zipInput = new BufferedInputStream(zipUurl.openStream());

			OutputStream output = new FileOutputStream(filePath);
			byte data[] = new byte[1024];

			long total = 0;

			while ((count = zipInput.read(data)) != -1) {
				total += count;
				progress.setProgress((int) ((total * 100) / zipLenghtOfFile));

				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			zipInput.close();

		} catch (Exception e) {
		} finally {

			String zipFile = filePath;
			String unzipLocation = Environment.getExternalStorageDirectory()
					+ "/" + BASE_FOLDER + "/" + PRODUCTS_FOLDER + "/";
			System.out.println(unzipLocation);
			// String unzipLocation = "/mnt/sdcard/";
			Decompress d = new Decompress(zipFile, unzipLocation);
			d.unzip();
		}
		return null;

	}

	private String getZipUrl(String... aurl) throws MalformedURLException,
			IOException {
		URL checksuUurl = new URL(aurl[0]);
		URLConnection checksuConn = checksuUurl.openConnection();
		checksuConn.connect();

		int checksumLenghtOfFile = checksuConn.getContentLength();
		Log.d("ANDRO_ASYNC", "Lenght of file: " + checksumLenghtOfFile);

		InputStream checksumInput = new BufferedInputStream(
				checksuUurl.openStream());

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Document doc = null;
		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(checksumInput);

			doc.getDocumentElement().normalize();
			System.out.println("Root element of the doc is "
					+ doc.getDocumentElement().getNodeName());

		} catch (Exception e) {
			e.printStackTrace();
		}
		String dlUrl = ParseXml.parseChecksum(doc);
		return dlUrl;
	}

	@Override
	protected void onPostExecute(String unused) {
		// dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
		progress.dismiss();
		
		if (toOpen) {
			Utilities.openProduct(context, productId, categoriesForBar);
		} else if(products != null && products.size()>0) {
			Log.d("TROLL","NEXT PRODUCT DL");
			
			new GetChecksumRequest(mContext, products, categoriesForBar);
			SamsungRequests.getExecutor().execute();
			
			
		}
	}
}
