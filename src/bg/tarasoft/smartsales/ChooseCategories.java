package bg.tarasoft.smartsales;

import java.util.List;

import bg.tarasoft.smartsales.adapters.ShownCategoriesAdapter;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.database.CategoryDataSource;
import bg.tarasoft.smartsales.samsung.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ChooseCategories extends Activity {

	private ListView categoriesList;
	private CategoryDataSource dataSource;
	private ShownCategoriesAdapter adapter;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shown_categories_list);

		context = this;

		categoriesList = (ListView) findViewById(R.id.listView1);

		dataSource = new CategoryDataSource(this);
		dataSource.open();

		List<Category> categories = dataSource.getAllHiddenCategories(0);
		adapter = new ShownCategoriesAdapter(this, categories, dataSource);
		categoriesList.setAdapter(adapter);

	}

	@Override
	public void onBackPressed() {
		Category data = (Category) categoriesList.getAdapter().getItem(0);
		if (data.getParentId() == 0) {
			super.onBackPressed();
		} else {

			int counter = 0;
			List<Category> catToBeUpdated = ((ShownCategoriesAdapter) categoriesList
					.getAdapter()).getCategories();

			for (Category cat : catToBeUpdated) {
				if (cat.getIsShown() == 0) {
					counter++;
				}

				System.out.println("CAT NAME: " + cat.getName() + "  "
						+ String.valueOf(cat.getIsShown()));

			}

			Category parentCat = dataSource.getCategory(data.getParentId());

			if (counter == adapter.getCategories().size()) {
				parentCat.setIsShown(0);
			} else {
				parentCat.setIsShown(1);
			}

			catToBeUpdated.add(parentCat);

			dataSource.updateCategories(catToBeUpdated);

			categoriesList.setAdapter(new ShownCategoriesAdapter(this,
					dataSource.getAllHiddenCategories(0), dataSource));
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		dataSource.updateCategories(((ShownCategoriesAdapter) categoriesList
				.getAdapter()).getCategories());
		dataSource.close();
	}
}
