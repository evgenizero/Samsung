package bg.tarasoft.smartsales.views;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import bg.tarasoft.smartsales.SubCategoriesActivity;
import bg.tarasoft.smartsales.SubSubCategoriesActivity;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.ProductsGroup;
import bg.tarasoft.smartsales.bean.Serie;
import bg.tarasoft.smartsales.database.CategoryDataSource;
import bg.tarasoft.smartsales.database.SeriesDataSource;
import bg.tarasoft.smartsales.samsung.R;

public class HeaderLabel extends LinearLayout implements OnClickListener {

	private Context mContext;
	private View view;
	private TextView label;
	private String text;
	private ProductsGroup productsGroup;
	private HeaderBar headerBar;
	private ArrayList<ProductsGroup> labelsCategories;
	private SeriesDataSource dataSource;

	public HeaderLabel(final Context context, ProductsGroup productsGroup) {
		super(context);
		inflateView(context);
		mContext = context;
		label = (TextView) view.findViewById(R.id.h_label);
		this.productsGroup = productsGroup;
		this.dataSource = new SeriesDataSource(context);
		label.setText(productsGroup.getName());
		label.setOnClickListener(this);

	}

	private void inflateView(final Context context) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.header_label, this);
		// TODO VVV tva e zashtoto neshto ne se zimat parametrite ot XML - da se
		// opravi
		view.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
	}

	public ProductsGroup getCategory() {
		return productsGroup;
	}

	public void onClick(View v) {
		boolean toDelete = false;
		boolean isLast = false;
		if (labelsCategories.size() != 1) {

			for (int i = 0; i < labelsCategories.size(); ++i) {
				ProductsGroup pg = labelsCategories.get(i);
				if (labelsCategories.size() > 3
						&& labelsCategories.indexOf(productsGroup) == 1) {
					labelsCategories.remove(labelsCategories
							.get(labelsCategories.size() - 1));
					System.out.println("TUKA SME W DEEBA");
				}

				if (pg.getName().equals(productsGroup.getName())) {
					System.out.println("CLICKEED FROM HEADER: " + pg.getName());
					if (i == labelsCategories.size() - 1) {
						isLast = true;
					}
					toDelete = true;
				} else if (toDelete) {
					System.out.println("REMOVING FROM HEADER: " + pg.getName());
					labelsCategories.remove(i);
				} else {
					System.out.println("NOT REMOVED: " + pg.getName());
				}

			}
			// if (!isLast) {
			if (productsGroup instanceof Category) {
				// your code
				Intent intent;
				boolean shouldFinish = true;
				System.out.println("ITSA A CATEGORYYYYYYYYYYY");
				if (labelsCategories.indexOf(productsGroup) >= 2) {
					intent = new Intent(mContext,
							SubSubCategoriesActivity.class);
					shouldFinish = false;
				} else {
					intent = new Intent(mContext, SubCategoriesActivity.class);
				}
				CategoryDataSource ds = new CategoryDataSource(mContext);
				ds.open();

				Category category = ds.getCategory(productsGroup.getName());
				ds.close();
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("headerBar", labelsCategories);
				intent.putExtra("categoryId", category.getId());

				System.out.println("INDEX OF PROD: "
						+ labelsCategories.indexOf(productsGroup));
				if (labelsCategories.indexOf(productsGroup) == 1) {
					shouldFinish = false;
				}
				System.out.println("LABELS CAT SIZE: "
						+ labelsCategories.size());
				if (labelsCategories.size() > 1
						&& productsGroup instanceof Category) {
					// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
					// Intent.FLAG_ACTIVITY_SINGLE_TOP);
				}

				intent.putExtra("shouldFinish", shouldFinish);
				intent.putExtra("parentId", category.getId());
				//
				// intent.putExtra("headerBar", labelsCategories);
				dataSource.open();
				intent.putExtra("noSeries",
						!dataSource.containsSeries(category.getId()));
				intent.putExtra("shouldFinish", shouldFinish);
				dataSource.close();

				System.out.println("CATEGORY: " + category.getId()
						+ category.getName());

				System.out.println("CATEGOTY: " + category.getId()
						+ category.getName());
				mContext.startActivity(intent);
			} else if (productsGroup instanceof Serie) {
				System.out.println("ITS A SERIEEEEEEEEEEE");
				dataSource.open();
				Serie serie = dataSource.getSerie(((Serie) productsGroup)
						.getId());
				dataSource.close();

				Intent intent = new Intent(mContext,
						bg.tarasoft.smartsales.ProductsActivity.class);
				intent.putExtra("serieId", serie.getId());

				CategoryDataSource ds = new CategoryDataSource(mContext);
				ds.open();
				Category category = ds.getCategory(serie.getCategoryId());
				ds.close();
				System.out.println("SERIEIERISERISEIRS IDDD: "
						+ String.valueOf(serie.getId()));
				intent.putExtra("subCatName", category.getName());
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("serieName", serie.getName());
				// intent.putExtra("categoryName", parentName);
				intent.putExtra("headerBar", labelsCategories);
				mContext.startActivity(intent);
			} else {

				System.out.println("NE E INSTANCEE :(");
			}
		}
		// }
	}

	public void setCategory(ProductsGroup group) {
		this.productsGroup = group;

	}

	public void setHeaderBar(HeaderBar headerBar) {
		this.headerBar = headerBar;
	}

	public void setCategoriesForBar(ArrayList<ProductsGroup> labelsCategories) {
		this.labelsCategories = labelsCategories;
	}

}