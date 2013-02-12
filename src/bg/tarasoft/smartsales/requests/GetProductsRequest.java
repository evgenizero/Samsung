package bg.tarasoft.smartsales.requests;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import bg.tarasoft.smartsales.MainCategories;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.Product;
import bg.tarasoft.smartsales.database.CategoryDataSource;
import bg.tarasoft.smartsales.database.ProductDataSource;
import bg.tarasoft.smartsales.parser.ParseXml;
import bg.tarasoft.smartsales.utilities.Utilities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;

public class GetProductsRequest extends SamsungGetRequest {

	private List<Product> products;
	private ProductDataSource dataSource;

	public GetProductsRequest(Context context, InputStream in) {
		this(context);
		this.in = in;
	}
	
	public GetProductsRequest(Context context) {
		super(context, "Updating indexes/Getting products",
				"http://system.smartsales.bg/product/android_request_info/?request_type=xml_products&answer_type=download");
		
		dataSource = new ProductDataSource(context);
		dataSource.open();

//		if (!Utilities.isOnline(context)) {
//			executor.clear();
//			dataSource.close();
//		} else {
			products = new ArrayList<Product>();
		//}
	}
	
	@Override
	protected void processStream(Document doc) {
		ParseXml.parseProducts(doc, products, context, progress);
		dataSource.insertProducts(products, progress);
		dataSource.close();
	}
}