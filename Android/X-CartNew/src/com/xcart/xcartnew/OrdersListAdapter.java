package com.xcart.xcartnew;

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

		holder.userName = (TextView) row.findViewById(R.id.user_name);
		holder.paid = (TextView) row.findViewById(R.id.paid);
		holder.status = (TextView) row.findViewById(R.id.order_status);
		holder.date = (TextView) row.findViewById(R.id.order_date);
		row.setTag(holder.order);
		setupItem(holder);
		return row;
	}

	private void setupItem(OrderHolder holder) {
		holder.userName.setText(holder.order.getUserName() + " (#" + holder.order.getId() + ")");
		holder.paid.setText(holder.order.getPaid());
		holder.status.setText(holder.order.getStatus());
		holder.date.setText(holder.order.getDate());
	}

	private static class OrderHolder {
		Order order;
		TextView userName;
		TextView paid;
		TextView status;
		TextView date;;
	}

	private List<Order> items;
	private int layoutResourceId;
	private Context context;
}
