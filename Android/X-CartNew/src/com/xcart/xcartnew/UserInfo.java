package com.xcart.xcartnew;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class UserInfo extends PinSupportNetworkActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);
		progressBar = (ProgressBar) findViewById(R.id.user_info_progress_bar);
		adapter = new OrdersListAdapter(this, R.layout.order_item, new ArrayList<Order>());
		ListView list = (ListView) findViewById(R.id.orders_list);
		LayoutInflater inflater = LayoutInflater.from(this);

		View header = inflater.inflate(R.layout.user_info_header, null, false);
		list.addHeaderView(header, null, false);

		list.setFooterDividersEnabled(false);
		list.setHeaderDividersEnabled(false);
		View listFooter = inflater.inflate(R.layout.on_demand_footer, null, false);
		ordersProgressBar = (ProgressBar) listFooter.findViewById(R.id.progress_bar);
		list.addFooterView(listFooter, null, false);

		/**list.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (totalItemCount > startItemCount && firstVisibleItem + visibleItemCount == totalItemCount
						&& !isDownloading && hasNext) {
					updateUsersList();
				}
			}
		});**/

		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			}
		});
		list.setAdapter(adapter);
		userName = (TextView) findViewById(R.id.user_title);
		userName.setText(getIntent().getStringExtra("userName"));

		// test
		addOrderToList("1000", "Smith, Michelle", "$460.99", "C", "JUN\n22");
		addOrderToList("999", "Smith, John", "$914.99", "C", "JUN\n22");

		firstName = (TextView) header.findViewById(R.id.first_name);
		lastName = (TextView) header.findViewById(R.id.last_name);
		email = (TextView) header.findViewById(R.id.email);
		address = (TextView) header.findViewById(R.id.address);
		phone = (TextView) header.findViewById(R.id.phone);
		fax = (TextView) header.findViewById(R.id.fax);

		id = getIntent().getStringExtra("userId");
	}

	@Override
	protected void withoutPinAction() {
		if (isNeedDownload()) {
			clearData();
			updateData();
		}
		super.withoutPinAction();
	}

	public void sendMessageClick(View v) {
		setNeedDownloadValue(false);
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email.getText().toString(), null));
		startActivityForResult(Intent.createChooser(emailIntent, "Send message..."), 3);
	}

	public void callClick(View v) {
		setNeedDownloadValue(false);
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:" + phone.getText()));
		startActivityForResult(callIntent, 1);
	}

	private void updateData() {
		progressBar.setVisibility(View.VISIBLE);

		GetRequester dataRequester = new GetRequester() {
			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONObject obj = new JSONObject(result);
						firstName.setText(obj.getString("firstname"));
						lastName.setText(obj.getString("lastname"));
						email.setText(obj.getString("login"));
						address.setText(obj.getString("address") + "\n" + obj.getString("city") + ", "
								+ obj.getString("state") + " " + obj.getString("zipcode") + "\n"
								+ obj.getString("country"));
						phone.setText(obj.getString("phone"));
						fax.setText(obj.getString("fax"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					showConnectionErrorMessage();
				}
				progressBar.setVisibility(View.GONE);
			}
		};

		SharedPreferences authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
		String sid = authorizationData.getString("sid", "");
		dataRequester.execute("https://54.213.38.9/api/api2.php?request=user_info&id=" + id + "&sid=" + sid);
	}

	private void addOrderToList(final String id, final String userName, final String paid, final String status,
			final String date) {
		adapter.add(new Order(id, userName, paid, status, date));
	}

	private void clearData() {
		firstName.setText("");
		lastName.setText("");
		email.setText("");
		address.setText("");
		phone.setText("");
		fax.setText("");
	}
	
	private void clearList() {
		adapter.clear();
	}

	private ProgressBar progressBar;
	private ProgressBar ordersProgressBar;
	private TextView userName;
	private TextView firstName;
	private TextView lastName;
	private TextView email;
	private TextView address;
	private TextView phone;
	private TextView fax;
	private OrdersListAdapter adapter;
	private String id;
}
