package bg.tarasoft.smartsales.requests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import bg.tarasoft.smartsales.HTMLPlayerActivity;
import bg.tarasoft.smartsales.MainCategories;
import bg.tarasoft.smartsales.SubCategoriesActivity;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.Checksum;
import bg.tarasoft.smartsales.bean.Product;
import bg.tarasoft.smartsales.bean.ProductsGroup;
import bg.tarasoft.smartsales.database.CategoryDataSource;
import bg.tarasoft.smartsales.database.ChecksumDataSource;
import bg.tarasoft.smartsales.database.ProductDataSource;
import bg.tarasoft.smartsales.parser.ParseXml;
import bg.tarasoft.smartsales.utilities.Utilities;
import bg.tarasoft.smartsales.views.HeaderBar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GetChecksumRequest extends SamsungGetRequest {

	private ChecksumDataSource dataSource;
	private String newChecksum;
	private String type;

	private String dialogMessage;
	private boolean toOpen;
	private boolean areMultipleProducts = false;
	private List<Product> products;
	private Context mContext;
	private boolean downloadingHTML = false;

	public GetChecksumRequest(Context context, String checksumType) {
		super(context, null,
				"http://system.smartsales.bg/product/android_request_info/?request_type="
						+ checksumType + "&answer_type=checksum");

		this.dialogMessage = "Checking for updates";

		dataSource = new ChecksumDataSource(context);
		dataSource.open();
		type = checksumType;

		if (!Utilities.isOnline(context)) {
			if (type.equals("xml_categories")) {
				((SubCategoriesActivity) context).processData();
			}
			executor.clear();
			dataSource.close();
		}
	}

	public GetChecksumRequest(Context context, int productId, boolean toOpen) {
		super(
				context,
				null,
				"http://system.smartsales.bg/product/android_request_info/?request_type=product&answer_type=checksum&id="
						+ productId);
		this.toOpen = toOpen;
		this.dialogMessage = "Checking for updates";

		dataSource = new ChecksumDataSource(context);
		dataSource.open();
		type = productId + "";

		if (!Utilities.isOnline(context)) {
			if (Utilities.productExistOnSdCard(productId) && toOpen) {

				Utilities.openProduct(context, productId);
			} else {
				Toast toast = Toast.makeText(context, "Not downloaded",
						Toast.LENGTH_SHORT);
				toast.show();
			}
			executor.clear();
			dataSource.close();
		}
	}

	public GetChecksumRequest(Context context, List<Product> products) {
		super(
				context,
				null,
				"http://system.smartsales.bg/product/android_request_info/?request_type=product&answer_type=checksum&id="
						+ products.get(0).getId());
		int productId = products.get(0).getId();

		this.products = Utilities.cloneList(products);
		this.products.remove(0);
		this.mContext = context;
		this.dialogMessage = "Checking for updates";
		this.areMultipleProducts = true;
		dataSource = new ChecksumDataSource(context);
		dataSource.open();
		type = productId + "";

		if (!Utilities.isOnline(context)) {
			if (Utilities.productExistOnSdCard(productId) && toOpen) {

				Utilities.openProduct(context, productId);
			} else {
				Toast toast = Toast.makeText(context, "Not downloaded",
						Toast.LENGTH_SHORT);
				toast.show();
			}
			executor.clear();
			dataSource.close();
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		dialog = new ProgressDialog(context);
		dialog.setMessage(dialogMessage);
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);

		if (!error) {
			Checksum oldChecksum = dataSource.getChecksum(type);

			System.out.println("NEW CHECKSUM = " + newChecksum);

			if (oldChecksum != null) {
				System.out.println("OLD CHECKSUM = " + oldChecksum.getValue());
			}
			if (!"No product.".equals(newChecksum)) {
				if (oldChecksum != null
						&& oldChecksum.getValue().equals(newChecksum)) {

					if (type.equals("xml_categories")) {
						((SubCategoriesActivity) context).processData();
					} else
					// contains number
					if (type.matches(".*\\d.*") && toOpen) {
						// is a product

						Utilities.openProduct(context, Integer.parseInt(type));

					}
				} else {
					dataSource.insertChecksum(new Checksum(type, newChecksum));
					getData();
				}

			} else {
				Toast toast = Toast.makeText(context,
						"No such product no server", Toast.LENGTH_SHORT);
				toast.show();

			}

			Log.d("TROLL", "DOWNLOADING HTML IS " + downloadingHTML);
			if (!downloadingHTML && areMultipleProducts && products.size() > 0) {
				Log.d("TROLL", "GET CHECKSUM REQ " + downloadingHTML);
				new GetChecksumRequest(mContext, products);
				SamsungRequests.getExecutor().execute();
			}

			dataSource.close();
		}
	}

	private void getData() {
		if (type.equals("common_files")) {
			new GetCommonFiles(context)
					.execute("http://system.smartsales.bg/product/android_request_info/?request_type=common_files&answer_type=download");

		} else if (type.equals("xml_categories")) {
			new GetStoresRequest(context);
			new GetCategoriesRequest(context);
			new GetProductsRequest(context);
			new GetModelsRequest(context);
			new GetSeriesRequest(context);
			executor.execute();
		} else {
			// is a product
			downloadingHTML = true;
			Log.d("TROLL", "DOWNLOADINGHTML SET TO " + downloadingHTML);
			if (areMultipleProducts && products.size() > 0) {

				new GetProductHTML(Integer.parseInt(type), context, toOpen,
						products)
						.execute("http://system.smartsales.bg/product/android_request_info/?request_type=product&answer_type=download&id="
								+ type);

			} else {
				new GetProductHTML(Integer.parseInt(type), context, toOpen,
						null)
						.execute("http://system.smartsales.bg/product/android_request_info/?request_type=product&answer_type=download&id="
								+ type);
			}
		}
	}

	@Override
	protected void processStream(Document doc) {
		newChecksum = ParseXml.parseChecksum(doc);
		System.out.println("PROCCESSED STREAM");
	}
}