package com.example.adminshop;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OrdersListAdapter extends ArrayAdapter<Order> {

	public OrdersListAdapter(Context context, int resource, List<Order> items) {
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

		OrderHolder holder = new OrderHolder();
		holder.order = items.get(position);
		
		holder.date = (TextView) row.findViewById(R.id.order_date);
		holder.products = (TextView) row.findViewById(R.id.order_products);
		holder.status = (TextView) row.findViewById(R.id.order_status);
		holder.totalPrice = (TextView) row.findViewById(R.id.order_total);
		row.setTag(holder.order);
		setupItem(holder);
		return row;
	}

	private void setupItem(OrderHolder holder) {
		holder.date.setText(holder.order.getDate());
		holder.products.setText(holder.order.getProducts());
		holder.status.setText(holder.order.getStatus());
		holder.totalPrice.setText(holder.order.getTotalPrice());
	}

	private static class OrderHolder {
		Order order;
		TextView date;
		TextView products;
		TextView status;
		TextView totalPrice;
	}
	
	private List<Order> items;
	private int layoutResourceId;
	private Context context;
}
