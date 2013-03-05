package bg.tarasoft.smartsales.views;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import bg.tarasoft.smartsales.SubCategoriesActivity;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.ProductsGroup;
import bg.tarasoft.smartsales.bean.Serie;
import bg.tarasoft.smartsales.database.CategoryDataSource;
import bg.tarasoft.smartsales.database.SeriesDataSource;
import bg.tarasoft.smartsales.samsung.R;
import bg.tarasoft.smartsales.utilities.Utilities;

public class MenuButton extends LinearLayout implements OnClickListener {

	private Context mContext;
	private View view;
	private TextView label;
	private HeaderBar headerBar;
	private Category category;

	public MenuButton(final Context context, Category category,
			HeaderBar headerBar) {
		super(context);
		inflateView(context);
		this.headerBar = headerBar;
		this.category = category;
		mContext = context;
		label = (TextView) view.findViewById(R.id.h_label);
		label.setText(category.getName());
		label.setOnClickListener(this);
	}

	public MenuButton(Context context, Category c, HeaderBar headerBar2,
			boolean current) {
		super(context);
		inflateView(context);
		this.headerBar = headerBar2;
		this.category = c;
		mContext = context;
		label = (TextView) view.findViewById(R.id.h_label);
		label.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.main_back));
		label.setTextColor(getResources().getColor(R.color.text_color));
		label.setText(category.getName());
		label.setOnClickListener(this);

	}

	private void inflateView(final Context context) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.bottom_menu_button, this);
		// TODO VVV tva e zashtoto neshto ne se zimat parametrite ot XML - da se
		// opravi
		float width = getResources()
				.getDimension(R.dimen.bottom_bar_cell_width);
		view.setLayoutParams(new LayoutParams((int) width,
				LayoutParams.MATCH_PARENT, 1));
	}

	public void onClick(View v) {
		Intent intent = new Intent(mContext, SubCategoriesActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("parentId", category.getId());
		// intent.putExtra("categoryName", category.getName());
		intent.putExtra("noSeries", true);
		ArrayList<ProductsGroup> history = Utilities
				.getHistory((Activity) mContext);
		history.clear();
		history.add(category);
		// intent.putExtra("headerBar", headerBar.getLabelsCategories());
		// intent.putExtra("addToBar", true);
		mContext.startActivity(intent);
	}

}