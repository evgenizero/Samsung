package bg.tarasoft.smartsales.views;

import java.util.List;

import bg.tarasoft.smartsales.ProductsActivity;
import bg.tarasoft.smartsales.samsung.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CompareView extends LinearLayout{

	private Context mContext;
	private View view;
	
	public CompareView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		inflateView();

//		List<Integer> ids = ((ProductsActivity)context).getCompareIds();
//		for(Integer id : ids) {
//			Toast.makeText(context, String.valueOf(id), Toast.LENGTH_SHORT).show();
//		}
	}

	public void inflateView() {
		LayoutInflater layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.compare_layout, this);
	}
}
