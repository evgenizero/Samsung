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

public class MenuButton extends LinearLayout implements OnClickListener {

	private Context mContext;
	private View view;
	private TextView label;
	private HeaderBar headerBar;
	private Category category;

	public MenuButton(final Context context, Category category, HeaderBar headerBar) {
		super(context);
		inflateView(context);
		this.headerBar = headerBar;
		this.category = category;
		mContext = context;
		label = (TextView) view.findViewById(R.id.h_label);
		label.setText(category.getName());
		label.setOnClickListener(this);

	}

	private void inflateView(final Context context) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.header_label, this);
		// TODO VVV tva e zashtoto neshto ne se zimat parametrite ot XML - da se
		// opravi
		float width = getResources().getDimension(R.dimen.bottom_bar_cell_width);
		view.setLayoutParams(new LayoutParams((int) width, LayoutParams.MATCH_PARENT, 1));
	}

	

	public void onClick(View v) {
		Intent intent = new Intent(mContext,SubCategoriesActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		intent.putExtra("parentId", category.getId());
		intent.putExtra("categoryName", category.getName());
		intent.putExtra("noSeries", true);
		intent.putExtra("headerBar", headerBar.getLabelsCategories());
		//intent.putExtra("addToBar", true);
				mContext.startActivity(intent);
	}

	
}