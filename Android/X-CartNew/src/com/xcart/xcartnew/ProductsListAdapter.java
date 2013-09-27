package com.xcart.xcartnew;

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
		holder.sku = (TextView) row.findViewById(R.id.sku);
		holder.inStock = (TextView) row.findViewById(R.id.in_stock);
		holder.price = (TextView) row.findViewById(R.id.price);
		row.setTag(holder.product);
		setupItem(holder);
		return row;
	}

	private void setupItem(ProductHolder holder) {
		holder.name.setText(holder.product.getName());
		holder.sku.setText(holder.product.getSku());
		holder.inStock.setText(holder.product.getInStock());
		holder.price.setText(holder.product.getPrice());
	}

	private static class ProductHolder {
		Product product;
		TextView name;
		TextView sku;
		TextView inStock;
		TextView price;
	}

	private List<Product> items;
	private int layoutResourceId;
	private Context context;
}
