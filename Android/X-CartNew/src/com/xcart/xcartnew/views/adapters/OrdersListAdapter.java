package com.xcart.xcartnew.views.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xcart.xcartnew.R;
import com.xcart.xcartnew.model.Order;

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

	private enum StatusSymbols {
		I, Q, P, B, D, F, C
	}

	private void setupItem(OrderHolder holder) {
		holder.userName.setText(holder.order.getUserName() + " (#" + holder.order.getId() + ")");
		holder.paid.setText("$" + holder.order.getPaid());
		StatusSymbols statusSymbol = StatusSymbols.valueOf(holder.order.getStatus());
		holder.status.setText(getStatusBySymbol(statusSymbol));
		holder.status.setTextColor(getColorResourceBySymbol(statusSymbol));
		holder.date.setText(holder.order.getDate());
	}

	private String getStatusBySymbol(StatusSymbols symbol) {
		switch (symbol) {
		case I:
			return "Not finished";
		case Q:
			return "Queued";
		case P:
			return "Processed";
		case B:
			return "Backordered";
		case D:
			return "Declined";
		case F:
			return "Failed";
		case C:
			return "Complete";
		default:
			return "";
		}
	}

	private int getColorResourceBySymbol(StatusSymbols symbol) {
		Resources resources = context.getResources();
		switch (symbol) {
		case I:
		case D:
		case F:
			return resources.getColor(R.color.red_status);
		case Q:
		case B:
			return resources.getColor(R.color.dark_blue_status);
		case P:
			return resources.getColor(R.color.light_blue_status);
		case C:
			return resources.getColor(R.color.green_status);
		default:
			return resources.getColor(R.color.black);
		}
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
