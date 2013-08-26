package com.example.XCartAdmin;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CategoriesListViewAdapter extends ArrayAdapter<Category> {

	public CategoriesListViewAdapter(Context context, int resource, List<Category> items) {
		super(context, resource, items);
		this.context = context;
		this.layoutResourceId = resource;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);

		CategoryHolder holder = new CategoryHolder();
		holder.category = items.get(position);

		holder.name = (TextView) row.findViewById(R.id.category_name);
		holder.id = (TextView) row.findViewById(R.id.category_id);
		holder.sold = (TextView) row.findViewById(R.id.category_sold);
		
		if (position % 2 == 1) {
		    row.setBackgroundColor(context.getResources().getColor(R.color.ghostwhite));  
		}

		row.setTag(holder.category);
		setupItem(holder, position);
		return row;
	}

	private void setupItem(CategoryHolder holder, final int position) {
		holder.name.setText(String.valueOf(position + 1) + ". " + holder.category.getName());
		holder.id.setText("Id: " + holder.category.getId());
		holder.sold.setText(holder.category.getSold());
	}

	private static class CategoryHolder {
		Category category;
		TextView name;
		TextView id;
		TextView sold;
	}

	private List<Category> items;
	private int layoutResourceId;
	private Context context;
}
