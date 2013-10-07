package com.xcart.xcartnew;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UserInfo extends PinSupportNetworkActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		adapter = new OrdersListAdapter(this, R.layout.order_item, new ArrayList<Order>());
		ListView list = (ListView) findViewById(R.id.orders_list);
		LayoutInflater inflater = getLayoutInflater();

		View header = inflater.inflate(R.layout.user_info_header, null, false);
		list.addHeaderView(header, null, false);

		list.setFooterDividersEnabled(false);
		list.setHeaderDividersEnabled(false);
		list.setAdapter(adapter);
		userName = (TextView) findViewById(R.id.user_title);
		userName.setText(getIntent().getStringExtra("userName"));

		// test
		addOrderToList("1000", "Smith, Michelle", "$460.99", "Complete", "JUN\n22");
		addOrderToList("999", "Smith, John", "$914.99", "Complete", "JUN\n22");

		firstName = (TextView) header.findViewById(R.id.first_name);
		lastName = (TextView) header.findViewById(R.id.last_name);
		email = (TextView) header.findViewById(R.id.email);
		address = (TextView) header.findViewById(R.id.address);
		phone = (TextView) header.findViewById(R.id.phone);
		fax = (TextView) header.findViewById(R.id.fax);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send_message_button:
			break;
		case R.id.call_button:
			break;
		}
	}

	private void addOrderToList(final String id, final String userName, final String paid, final String status,
			final String date) {
		adapter.add(new Order(id, userName, paid, status, date));
	}

	ProgressBar progressBar;
	Button sendMessageButton;
	Button callButton;
	TextView userName;
	TextView firstName;
	TextView lastName;
	TextView email;
	TextView address;
	TextView phone;
	TextView fax;
	OrdersListAdapter adapter;
}
