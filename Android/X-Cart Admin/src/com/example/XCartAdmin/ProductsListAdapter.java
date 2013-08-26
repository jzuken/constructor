package com.example.XCartAdmin;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProductsListAdapter extends ArrayAdapter<Product> {
	
	public ProductsListAdapter(Context context, int resource, List<Product> items) {
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
		holder.available = (TextView) row.findViewById(R.id.product_available);
		holder.sold = (TextView) row.findViewById(R.id.product_sold);
		holder.freeShipping = (TextView) row.findViewById(R.id.product_free_shipping);
		holder.price = (TextView) row.findViewById(R.id.product_price);
		row.setTag(holder.product);
		setupItem(holder);
		return row;
	}

	private void setupItem(ProductHolder holder) {
		holder.name.setText(holder.product.getName());
		holder.available.setText(holder.product.getAvailable());
		holder.sold.setText(holder.product.getSold());
		holder.freeShipping.setText(holder.product.getFreeShippingValue());
		holder.price.setText(holder.product.getPrice());
	}

	private static class ProductHolder {
		Product product;
		TextView name;
		TextView available;
		TextView sold;
		TextView freeShipping;
		TextView price;
	}
	
	private List<Product> items;
	private int layoutResourceId;
	private Context context;
}
