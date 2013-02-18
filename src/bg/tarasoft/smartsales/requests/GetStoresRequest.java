package bg.tarasoft.smartsales.requests;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import bg.tarasoft.smartsales.bean.Store;
import bg.tarasoft.smartsales.bean.StoreType;
import bg.tarasoft.smartsales.database.StoreRetailDataSource;
import bg.tarasoft.smartsales.database.StoresDataSource;
import bg.tarasoft.smartsales.parser.ParseXml;

import android.content.Context;

public class GetStoresRequest extends SamsungGetRequest{

	private List<StoreType> storesRetails;
	private StoreRetailDataSource dataSource;
	
	public GetStoresRequest(Context context) {
		super(context, null, "http://system.smartsales.bg/android_html/xml_files/xml_stores.xml");
		dataSource = new StoreRetailDataSource(context);
		dataSource.open();
		storesRetails = new ArrayList<StoreType>();
	}
	
	@Override
	protected void processStream(Document doc) {
		ParseXml.parseStores(doc, storesRetails, context);
		
		dataSource.insertStoreRetails(storesRetails);
		dataSource.close();
	}

}
