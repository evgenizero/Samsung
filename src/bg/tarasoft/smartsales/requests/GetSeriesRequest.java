package bg.tarasoft.smartsales.requests;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import bg.tarasoft.smartsales.MainCategories;
import bg.tarasoft.smartsales.SubCategoriesActivity;
import bg.tarasoft.smartsales.bean.Serie;
import bg.tarasoft.smartsales.database.SeriesDataSource;
import bg.tarasoft.smartsales.parser.ParseXml;
import bg.tarasoft.smartsales.utilities.Utilities;

import android.content.Context;

public class GetSeriesRequest extends SamsungGetRequest {

	private List<Serie> series;
	private SeriesDataSource dataSource;
	
	public GetSeriesRequest(Context context, InputStream in) {
		this(context);
		this.in = in;
	}
	
	protected GetSeriesRequest(Context context) {
		super(
				context,
				"Getting series",
				"http://system.smartsales.bg/product/android_request_info/?request_type=xml_series&answer_type=download");

		dataSource = new SeriesDataSource(context);
		dataSource.open();
		
//		if (!Utilities.isOnline(context)) {
//			executor.clear();
//			((MainCategories) context).processData();
//			dataSource.close();
//		} else {
			series = new ArrayList<Serie>();
		//}
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);

		if (!error) {

			((SubCategoriesActivity) context).processData();

		}
	}

	@Override
	protected void processStream(Document doc) {
		ParseXml.parseSeries(doc, series);
		dataSource.updateSeries(series, progress);
		dataSource.close();
	}

}
