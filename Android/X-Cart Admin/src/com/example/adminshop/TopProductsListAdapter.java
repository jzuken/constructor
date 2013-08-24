package com.example.adminshop;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TopProductsListAdapter extends ArrayAdapter<TopProduct> {

	public TopProductsListAdapter(Context context, int resource, List<TopProduct> items) {
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

		ProductHolder holder = new ProductHolder();
		holder.product = items.get(position);

		holder.name = (TextView) row.findViewById(R.id.product_name);
		holder.id = (TextView) row.findViewById(R.id.product_id);
		holder.code = (TextView) row.findViewById(R.id.product_code);
		holder.sold = (TextView) row.findViewById(R.id.product_sold);
		
		if (position % 2 == 1) {
		    row.setBackgroundColor(context.getResources().getColor(R.color.ghostwhite));  
		}

		row.setTag(holder.product);
		setupItem(holder, position);
		return row;
	}

	private void setupItem(ProductHolder holder, final int position) {
		holder.name.setText(String.valueOf(position + 1) + ". " + holder.product.getName());
		holder.id.setText("Id: " + holder.product.getId());
		holder.code.setText("Code: " + holder.product.getCode());
		holder.sold.setText(holder.product.getSold());
	}

	private static class ProductHolder {
		TopProduct product;
		TextView name;
		TextView id;
		TextView code;
		TextView sold;
	}

	private List<TopProduct> items;
	private int layoutResourceId;
	private Context context;
}
