package bg.tarasoft.smartsales.views;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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
	private LinearLayout ll;

	public HeaderBar(final Context context, AttributeSet attrs) {
		super(context, attrs);
		inflateView(context);
		mContext = context;
		ll = (LinearLayout) view.findViewById(R.id.labels_container);
	}

	// private void addLabeltoHeader(HeaderLabel label) {
	// labels.add(label);
	//
	// ll.addView(label);
	// System.out.println("ADDING TO CONTAINER: "
	// + label.getCategory().getName());
	//
	// }

	private void addLabeltList(HeaderLabel label) {
		labels.add(label);

	}

	private void inflateView(final Context context) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.header, this);
	}

	public ArrayList<ProductsGroup> getLabelsCategories() {
		ArrayList<ProductsGroup> productsGroup = new ArrayList<ProductsGroup>();
		if (labels != null) {
			for (HeaderLabel h : labels) {
				productsGroup.add(h.getCategory());
			}
		}
		return productsGroup;
	}

	public void onClick(View v) {

	}

	public void setCategories(ArrayList<ProductsGroup> categoriesForBar) {
		if (categoriesForBar.size() >= 1) {
			HeaderLabel[] labelsArr = new HeaderLabel[categoriesForBar.size()];
			for (int i = 0; i < categoriesForBar.size(); i++) {
				HeaderLabel headerLabel = new HeaderLabel(mContext,
						categoriesForBar.get(i));
				headerLabel.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
				labelsArr[i] = headerLabel;
			}
			populateText(labelsArr);
			// for (ProductsGroup p : categoriesForBar) {
			// HeaderLabel headerLabel = new HeaderLabel(mContext, p);
			// headerLabel.setLayoutParams(new LayoutParams(
			// LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
			// }
		}

	}

	private void populateText(View[] views) {
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		ll.removeAllViews();
		int maxWidth = width - 20;

		LinearLayout.LayoutParams params;
		LinearLayout newLL = new LinearLayout(mContext);
		newLL.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		newLL.setGravity(Gravity.LEFT);
		newLL.setOrientation(LinearLayout.HORIZONTAL);

		int widthSoFar = 0;

		for (int i = 0; i < views.length; i++) {
			LinearLayout LL = new LinearLayout(mContext);
			LL.setOrientation(LinearLayout.HORIZONTAL);
			LL.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
			LL.setLayoutParams(new ListView.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			// my old code
			// TV = new TextView(mContext);
			// TV.setText(textArray[i]);
			// TV.setTextSize(size); <<<< SET TEXT SIZE
			// TV.measure(0, 0);
			views[i].measure(0, 0);
			params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			// params.setMargins(5, 0, 5, 0); // YOU CAN USE THIS
			// LL.addView(TV, params);
			LL.addView(views[i], params);
			LL.measure(0, 0);
			widthSoFar += views[i].getMeasuredWidth();// YOU MAY NEED TO ADD THE
														// MARGINS

			if (widthSoFar >= maxWidth) {
				ll.addView(newLL);

				newLL = new LinearLayout(mContext);
				newLL.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				newLL.setOrientation(LinearLayout.HORIZONTAL);
				newLL.setGravity(Gravity.LEFT);
				params = new LinearLayout.LayoutParams(LL.getMeasuredWidth(),
						LL.getMeasuredHeight());
				newLL.addView(LL, params);
				widthSoFar = LL.getMeasuredWidth();
			} else {
				newLL.addView(LL);
			}
		}
		ll.addView(newLL);

	}

}