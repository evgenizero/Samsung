package bg.tarasoft.smartsales.listeners;

import java.util.ArrayList;
import java.util.List;

import bg.tarasoft.smartsales.ChooseCategories;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.database.CategoryDataSource;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class OnChooseCategoryListener implements OnCheckedChangeListener {

	private Context context;
	private Category category;
	private CategoryDataSource dataSource;

	public OnChooseCategoryListener(Context context, Category category,
			CategoryDataSource dataSource) {
		this.category = category;
		this.dataSource = dataSource;
		this.context = context;
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if (isChecked) {
			// buttonView.setChecked(false);
			category.setIsShown(1);
			if (category.getParentId() == 0) {
				handleParentCateogires(1);
			} else {
				Category parent = dataSource
						.getCategory(category.getParentId());
				parent.setIsShown(1);
				update(parent);
			}
		} else {
			category.setIsShown(0);
			// buttonView.setChecked(true);
			if (category.getParentId() == 0) {
				handleParentCateogires(0);
			} else {

				int countOfAll = dataSource.getCount(dataSource.getCategory(category
						.getParentId()));
				
				int countOfHidden = dataSource.getCount(dataSource.getCategory(category
						.getParentId()),0);
				if((countOfHidden + 1) == countOfAll) {
					Category category2 = dataSource.getCategory(category.getParentId());
					category2.setIsShown(0);
					update(category2);
				} else {
					update();
				}
			}
		}

		System.out.println(category.getName() + "   " + category.getIsShown());

	}

	private void update(Category parent) {
		List<Category> cats = new ArrayList<Category>();
		cats.add(category);
		cats.add(parent);
		dataSource.updateCategories(cats);
	}

	private void update() {
		List<Category> cats = new ArrayList<Category>();
		cats.add(category);
		dataSource.updateCategories(cats);
	}

	private void handleParentCateogires(int isShown) {

		System.out.println("HERHEHREHR");

		List<Category> allHiddenCategories = dataSource
				.getAllHiddenCategories(category.getId());
		for (Category cat : allHiddenCategories) {
			cat.setIsShown(isShown);
		}
		
		allHiddenCategories.add(category);
		dataSource.updateCategories(allHiddenCategories);
		// ((ChooseCategories) context).notifyDataChange();
	}

}
