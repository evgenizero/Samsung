package bg.tarasoft.smartsales.requests;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import bg.tarasoft.smartsales.bean.Model;
import bg.tarasoft.smartsales.database.ModelsDataSource;
import bg.tarasoft.smartsales.database.SeriesDataSource;
import bg.tarasoft.smartsales.parser.ParseXml;

import android.content.Context;

public class GetModelsRequest extends SamsungGetRequest {

	private List<Model> models;
	private ModelsDataSource dataSource;

	public GetModelsRequest(Context context) {
		super(
				context,
				"Getting models",
				"http://system.smartsales.bg/product/android_request_info/?request_type=xml_models&answer_type=download");
		models = new ArrayList<Model>();
		dataSource = new ModelsDataSource(context);
		dataSource.open();
	}

	@Override
	protected void processStream(Document doc) {
		ParseXml.parseModels(context, doc, models, progress);
		dataSource.insertModels(models, progress);
		dataSource.close();
	}

}
