package bg.tarasoft.smartsales;

import java.util.List;

import bg.tarasoft.smartsales.adapters.ShownCategoriesAdapter;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.database.CategoryDataSource;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ChooseCategories extends Activity {

	private ListView categoriesList;
	private CategoryDataSource dataSource;
	private ShownCategoriesAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shown_categories_list);

		categoriesList = (ListView) findViewById(R.id.listView1);

		dataSource = new CategoryDataSource(this);
		dataSource.open();

		List<Category> categories = dataSource.getCategories(-1);
		adapter = new ShownCategoriesAdapter(this,
				categories);
		categoriesList.setAdapter(adapter);
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		dataSource.updateCategories(adapter.getCategories());
	}
}
