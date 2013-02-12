package bg.tarasoft.smartsales.views;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import bg.tarasoft.smartsales.ProductsActivity;
import bg.tarasoft.smartsales.bean.Product;
import bg.tarasoft.smartsales.bean.ProductsGroup;
import bg.tarasoft.smartsales.cache.Cache;
import bg.tarasoft.smartsales.requests.DownloadImagesTask;
import bg.tarasoft.smartsales.requests.GetCategoriesRequest;
import bg.tarasoft.smartsales.requests.GetChecksumRequest;
import bg.tarasoft.smartsales.requests.GetProductHTML;
import bg.tarasoft.smartsales.requests.SamsungRequests;
import bg.tarasoft.smartsales.samsung.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProductView extends LinearLayout implements OnClickListener{
	private Product product;
	private ImageView image;
	private View view;
	private TextView text;
	private TextView label;
	private Context mContext;
	private static final String BASE_DL_URL = "http://system.smartsales.bg/android_html/zip_files/";
	

	public ProductView(final Context context, AttributeSet attrs) {
		super(context, attrs);
		inflateView(context);
		this.mContext = context;
		text = (TextView)view.findViewById(R.id.text);
		image = (ImageView) view.findViewById(R.id.image);
		label = (TextView) view.findViewById(R.id.product_label);
		text.setTextColor(Color.WHITE);
		this.setOnClickListener(this);
		
	}

	public void setProduct(Product product){
		this.product = product;
		text.setText(product.getName());
		Log.d("kj", "Product STATUS: " + product.getLabel());
		switch(product.getLabel()){
		
		case Product.LABEL_NONE:
			label.setVisibility(View.INVISIBLE);
			break;
		case Product.LABEL_LAST:
			label.setVisibility(View.VISIBLE);
			label.setText(R.string.posledni_broiki);
			label.setBackgroundColor(Color.RED);
			break;
		
		case Product.LABEL_NEW:
			label.setVisibility(View.VISIBLE);
			
			label.setText(R.string.new_product);
			label.setBackgroundColor(Color.BLUE);
			Log.d("kjk", "Setting to new");
			break;
		case Product.LABEL_PROMO:
			label.setVisibility(View.VISIBLE);
			label.setText(R.string.promo);
			label.setBackgroundColor(Color.YELLOW);
			break;
		}
		String url = product.getImageUrl();
		if (url != null) {
			Bitmap bm = Cache.getCacheFile(url);
			if (bm == null) {
				image.setTag(url);
				new DownloadImagesTask().execute(image);
			} else {
				image.setImageBitmap(bm);
			}
		} else {
			image.setTag("true");

		}
	}

	private void inflateView(final Context context) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.single_product, this);
	}

	

	public void onClick(View v) {
		ArrayList<ProductsGroup> categoriesForBar = ((ProductsActivity) mContext).getHeaderBar();
		new GetChecksumRequest(mContext, product.getId(),categoriesForBar,true);
		SamsungRequests.getExecutor().execute();
		
	}

	
}