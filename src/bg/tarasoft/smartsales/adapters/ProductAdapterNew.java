package bg.tarasoft.smartsales.adapters;

import java.util.List;

import bg.tarasoft.smartsales.bean.Product;
import bg.tarasoft.smartsales.samsung.R;
import bg.tarasoft.smartsales.views.ProductView;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductAdapterNew extends BaseAdapter {
	private Context context;
	private List<Product> products;

	private class ProductHolder {
		//TextView textView1;
		//ImageView imageView1;
		ProductView pv;
	}

	public ProductAdapterNew(Context context, List<Product> products) {
		this.context = context;
		this.products = products;
	}

	public int getCount() {
		return products.size();
	}

	public Object getItem(int position) {
		return products.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ProductHolder holder;
		if (convertView == null || convertView.getTag() == null) {
			convertView = View.inflate(context, R.layout.product_grid_item, null);
			holder = new ProductHolder();

			holder.pv = (ProductView) convertView.findViewById(R.id.pv1);
			
			convertView.setTag(holder);
		} else {
			holder = (ProductHolder) convertView.getTag();
		}

        
        Product product = (Product) getItem(position);
        
        holder.pv.setProduct(product);
        
		return convertView;
	}
}