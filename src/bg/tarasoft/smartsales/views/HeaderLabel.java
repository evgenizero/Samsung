package bg.tarasoft.smartsales.views;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class HeaderLabel extends LinearLayout implements OnClickListener {

	private Context mContext;
	private View view;
	private TextView label;
	private String text;
	private ProductsGroup productsGroup;
	private HeaderBar headerBar;
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
		Utilities.clickedHeaderLabel(mContext,Utilities.getHistory((Activity) mContext), productsGroup);
		
	}

	

	public void setCategory(ProductsGroup group) {
		this.productsGroup = group;

	}

	public void setHeaderBar(HeaderBar headerBar) {
		this.headerBar = headerBar;
	}


}