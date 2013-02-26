package bg.tarasoft.smartsales.listeners;

import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.views.HeaderBar;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OnCategoryListItemClickListener implements OnItemClickListener {

	private Context context;
	private Intent intent;
	private HeaderBar headerBar;

	public OnCategoryListItemClickListener(Context context, HeaderBar headerBar) {
		this.context = context;
		this.headerBar = headerBar;
		intent = new Intent(context,
				bg.tarasoft.smartsales.SubCategoriesActivity.class);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		System.out.println("HERERERE");
		Category category = (Category) arg0.getItemAtPosition(arg2);
		intent.putExtra("parentId", category.getId());
		intent.putExtra("categoryName", category.getName());
		intent.putExtra("noSeries", true);
		intent.putExtra("addToBar", true);
//		System.out.println("SIZE BEFORE SEND"
//				+ headerBar.getLabelsCategories().size());
//		
		
		//TODO
		intent.putExtra("headerBar", headerBar.getLabelsCategories());
		context.startActivity(intent);

	}

}
