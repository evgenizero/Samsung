package bg.tarasoft.smartsales.views;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.ProductsGroup;
import bg.tarasoft.smartsales.samsung.R;

public class HeaderBar extends LinearLayout implements OnClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4527914456722298966L;
	private Context mContext;
	private View view;
	private List<HeaderLabel> labels;
	private LinearLayout labelsContainer;

	public HeaderBar(final Context context, AttributeSet attrs) {
		super(context, attrs);
		inflateView(context);
		mContext = context;
//		labelsContainer = (LinearLayout) view
//				.findViewById(R.id.labels_container);
//		labels = new ArrayList<HeaderLabel>();
//		for (HeaderLabel h : labels) {
//			h.setCategoriesForBar(getLabelsCategories());
//			addLabeltoHeader(h);
//			h.setHeaderBar(this);
//		}
//		this.setOnClickListener(this);

	}

	public void addLabeltoHeader(HeaderLabel label) {
		// / labels.add(label);

		labelsContainer.addView(label);
		label.setCategoriesForBar(getLabelsCategories());
		System.out.println("ADDING TO CONTAINER: "
				+ label.getCategory().getName());

	}

	public void addLabeltList(HeaderLabel label) {
		labels.add(label);
		label.setCategoriesForBar(getLabelsCategories());

	}

	private void inflateView(final Context context) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.header, this);
	}

	public ArrayList<ProductsGroup> getLabelsCategories() {
		ArrayList<ProductsGroup> productsGroup = new ArrayList<ProductsGroup>();
		if(labels != null) {
			for (HeaderLabel h : labels) {
				productsGroup.add(h.getCategory());
			}
		}
		return productsGroup;
	}

	public void onClick(View v) {

	}

	public void setCategories(ArrayList<ProductsGroup> categoriesForBar) {
//		// TODO optimize for loop
//		for (int i = 0; i < categoriesForBar.size(); ++i) {
//			labels.add(new HeaderLabel(mContext, categoriesForBar.get(i)));
//			
//		}
//		// labelsContainer.removeAllViews();
//		for (HeaderLabel h : labels) {
//			System.out.println("ADDING LABELS: " + h.getCategory().getName());
//			addLabeltoHeader(h);
//		}
//
	}

}