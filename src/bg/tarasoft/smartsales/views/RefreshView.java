package bg.tarasoft.smartsales.views;

import bg.tarasoft.smartsales.SubCategoriesActivity;
import bg.tarasoft.smartsales.samsung.R;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class RefreshView extends LinearLayout {

	private Context mContext;
	private View view;

	public RefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		inflateView();

		view.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(mContext,
						SubCategoriesActivity.class);
				intent.putExtra("update", true);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				mContext.startActivity(intent);
			}
		});
	}

	public void inflateView() {
		LayoutInflater layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.refresh_view, this);
	}

}
