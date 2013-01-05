package bg.tarasoft.smartsales.listeners;

import bg.tarasoft.smartsales.bean.Category;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class OnChooseCategoryListener implements OnCheckedChangeListener{

	private Category category;
	
	public OnChooseCategoryListener(Category category) {
		this.category = category;
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		if(isChecked) {
			//buttonView.setChecked(false);
			category.setIsShown(1);
		} else {
			category.setIsShown(0);
			//buttonView.setChecked(true);
		}
		
		System.out.println("CATEGORY: " + category.getIsShown());
	}

}
