package com.example.adminshop;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OrderProductsListAdapter extends ArrayAdapter<OrderProduct> {

	public OrderProductsListAdapter(Context context, int resource, List<OrderProduct> items) {
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
		holder.quantity = (TextView) row.findViewById(R.id.product_quantity);
		holder.price = (TextView) row.findViewById(R.id.product_price);
		row.setTag(holder.product);
		setupItem(holder);
		return row;
	}

	private void setupItem(ProductHolder holder) {
		holder.name.setText(holder.product.getName());
		holder.quantity.setText(holder.product.getQuantity());
		holder.price.setText(holder.product.getPrice());
	}

	private static class ProductHolder {
		OrderProduct product;
		TextView name;
		TextView quantity;
		TextView price;
	}

	private List<OrderProduct> items;
	private int layoutResourceId;
	private Context context;
}
