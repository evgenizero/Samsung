package bg.tarasoft.smartsales.requests;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import bg.tarasoft.smartsales.bean.Store;
import bg.tarasoft.smartsales.database.StoresDataSource;
import bg.tarasoft.smartsales.parser.ParseXml;

import android.content.Context;

public class GetStoresRequest extends SamsungGetRequest{

	private List<Store> stores;
	private StoresDataSource dataSource;
	
	public GetStoresRequest(Context context) {
		super(context, null, "http://system.smartsales.bg/android_html/xml_files/xml_stores.xml");
		dataSource = new StoresDataSource(context);
		dataSource.open();
		stores = new ArrayList<Store>();
	}
	
	@Override
	protected void processStream(Document doc) {
		ParseXml.parseStores(doc, stores, context);
		
		for(Store store : stores) {
			System.out.println("NAME: " + store.getStoreName() + "   ID: " +  store.getStoreID());
		}
		
		dataSource.insertStores(stores);
		dataSource.close();
	}

}
