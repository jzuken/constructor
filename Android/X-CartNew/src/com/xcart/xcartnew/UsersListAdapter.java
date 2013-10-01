package com.xcart.xcartnew;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UsersListAdapter extends ArrayAdapter<User> {

	public UsersListAdapter(Context context, int resource, List<User> items) {
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

		UserHolder holder = new UserHolder();
		holder.user = items.get(position);

		holder.name = (TextView) row.findViewById(R.id.user_name);
		if (position == 0) {
			holder.name.setBackgroundResource(R.drawable.top_rounded_blue);
		}
		holder.login = (TextView) row.findViewById(R.id.user_login);
		holder.type = (TextView) row.findViewById(R.id.user_type);
		holder.lastLogin = (TextView) row.findViewById(R.id.last_login);
		holder.totalOrders = (TextView) row.findViewById(R.id.total_orders);
		row.setTag(holder.user);
		setupItem(holder);
		return row;
	}

	private void setupItem(UserHolder holder) {
		holder.name.setText(holder.user.getName());
		holder.login.setText(holder.user.getLogin());
		holder.type.setText(holder.user.getType());
		holder.lastLogin.setText(holder.user.getLastLogin());
		holder.totalOrders.setText(holder.user.getTotalOrders());
	}

	private static class UserHolder {
		User user;
		TextView name;
		TextView login;
		TextView type;
		TextView lastLogin;
		TextView totalOrders;
	}

	private List<User> items;
	private int layoutResourceId;
	private Context context;
}
