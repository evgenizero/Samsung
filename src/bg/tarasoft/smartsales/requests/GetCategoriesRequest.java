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
import bg.tarasoft.smartsales.database.CategoryDataSource;
import bg.tarasoft.smartsales.parser.ParseXml;
import bg.tarasoft.smartsales.utilities.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;

public class GetCategoriesRequest extends SamsungGetRequest {

	private List<Category> categories;
	private CategoryDataSource dataSource;

	public GetCategoriesRequest(Context context, InputStream in) {
		this(context);
		this.in = in;
	}
	
	public GetCategoriesRequest(Context context) {
		super(
				context,
				"Getting categories",
				"http://system.smartsales.bg/product/android_request_info/?request_type=xml_categories&answer_type=download");

		dataSource = new CategoryDataSource(context);
		dataSource.open();

		//TODO EHRHEHRHEHREHREKJEHRKLSEJHR:SKDJFH:SDGHJ:SDLKGHJ:SDLGHJS:DJKGHSD:KHJ
		
//		if (!Utilities.isOnline(context)) {
//			executor.clear();
//			((MainCategories) context).processData();
//			dataSource.close();
//		} else {
			categories = new ArrayList<Category>();
		//}
	}

	@Override
	protected void processStream(Document doc) {
		ParseXml.parseCategories(doc, categories, context);
		dataSource.insertCategories(categories, progress);
		dataSource.close();
	}
}