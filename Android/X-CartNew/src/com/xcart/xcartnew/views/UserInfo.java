package com.xcart.xcartnew.views;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xcart.xcartnew.R;
import com.xcart.xcartnew.managers.network.HttpManager;
import com.xcart.xcartnew.managers.network.Requester;
import com.xcart.xcartnew.model.Order;
import com.xcart.xcartnew.views.adapters.OrdersListAdapter;

public class UserInfo extends PinSupportNetworkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);
		progressBar = (ProgressBar) findViewById(R.id.user_info_progress_bar);
		adapter = new OrdersListAdapter(this, R.layout.order_item, new ArrayList<Order>());
		ordersListView = (ListView) findViewById(R.id.orders_list);
		LayoutInflater inflater = LayoutInflater.from(this);

		View header = inflater.inflate(R.layout.user_info_header, null, false);
		ordersListView.addHeaderView(header, null, false);

		View listFooter = inflater.inflate(R.layout.on_demand_footer, null, false);
		ordersProgressBar = (ProgressBar) listFooter.findViewById(R.id.progress_bar);
		ordersListView.addFooterView(listFooter, null, false);

		ordersListView.setFooterDividersEnabled(false);
		ordersListView.setHeaderDividersEnabled(false);

		ordersListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (visibleItemCount > 0 && firstVisibleItem + visibleItemCount == totalItemCount && !isDownloading
						&& hasNext) {
					updateOrdersList();
				}
			}
		});

		ordersListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				setNeedDownloadValue(false);
				lastPositionClicked = position;
				Intent intent = new Intent(getBaseContext(), OrderInfo.class);
				intent.putExtra("orderId", ((OrdersListAdapter.OrderHolder) view.getTag()).getOrder().getId());
				startActivityForResult(intent, 1);
			}
		});

		ordersListView.setAdapter(adapter);
		userName = (TextView) findViewById(R.id.user_title);
		userName.setText(getIntent().getStringExtra("userName"));
		firstName = (TextView) header.findViewById(R.id.first_name);
		lastName = (TextView) header.findViewById(R.id.last_name);
		email = (TextView) header.findViewById(R.id.email);
		address = (TextView) header.findViewById(R.id.address);
		phone = (TextView) header.findViewById(R.id.phone);
		fax = (TextView) header.findViewById(R.id.fax);
		id = getIntent().getStringExtra("userId");
		sendMessageButton = (Button) header.findViewById(R.id.send_message_button);
		callButton = (Button) header.findViewById(R.id.call_button);
		settingsData = PreferenceManager.getDefaultSharedPreferences(this);

	}

	@Override
	protected void withoutPinAction() {
		packAmount = Integer.parseInt(settingsData.getString("orders_amount", "10"));
		if (isNeedDownload()) {
			clearData();
			clearList();
			updateData();
			updateOrdersList();
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

		requester = new Requester() {
			@Override
			protected String doInBackground(Void... params) {
				return new HttpManager(getBaseContext()).getUserInfo(id);
			}

			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONObject obj = new JSONObject(result);
						firstName.setText(obj.getString("firstname"));
						lastName.setText(obj.getString("lastname"));
						String emailValue = obj.getString("login");
						if (!isEmpty(emailValue)) {
							sendMessageButton.setClickable(true);
							email.setText(emailValue);
						}
						JSONObject addressObj = obj.getJSONObject("address");
						JSONObject shippingObj = addressObj.getJSONObject("S");
						address.setText(shippingObj.get("address") + "\n" + shippingObj.getString("city") + ", "
								+ shippingObj.getString("state") + " " + shippingObj.getString("zipcode") + "\n"
								+ shippingObj.getString("country"));
						String phoneValue = shippingObj.getString("phone");
						if (!isEmpty(phoneValue)) {
							callButton.setClickable(true);
							phone.setText(phoneValue);
						}
						fax.setText(shippingObj.getString("fax"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					showConnectionErrorMessage();
				}
				progressBar.setVisibility(View.INVISIBLE);
			}
		};

		requester.execute();
	}

	private void updateOrdersList() {
		ordersProgressBar.setVisibility(View.VISIBLE);
		synchronized (lock) {
			isDownloading = true;
		}
		hasNext = false;
		final String from = String.valueOf(currentAmount);
		requester = new Requester() {
			@Override
			protected String doInBackground(Void... params) {
				return new HttpManager(getBaseContext()).getUserOrders(from, String.valueOf(packAmount), id);
			}

			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONArray array = new JSONArray(result);
						int length = array.length();
						if (length == packAmount) {
							hasNext = true;
						}
						for (int i = 0; i < length; i++) {
							JSONObject obj = array.getJSONObject(i);
							String id = obj.getString("orderid");
							String title = obj.getString("title");
							if (!isEmpty(title)) {
								title += " ";
							}
							String name = title + obj.getString("firstname") + " " + obj.getString("lastname");
							String status = obj.getString("status");
							String date = obj.getString("month") + "\n" + obj.getString("day");
							String paid = obj.getString("total");
							addOrderToList(id, name, paid, status, date);
						}
						currentAmount += packAmount;
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					showConnectionErrorMessage();
				}
				ordersProgressBar.setVisibility(View.GONE);
				synchronized (lock) {
					isDownloading = false;
				}
			}
		};

		requester.execute();
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
		sendMessageButton.setClickable(false);
		callButton.setClickable(false);
		currentAmount = 0;
	}

	private boolean isEmpty(String string) {
		return string.equals("");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == ChangeStatus.changeStatusResultCode) {
			OrdersListAdapter.OrderHolder orderHolder = ((OrdersListAdapter.OrderHolder) ordersListView.getChildAt(
					lastPositionClicked).getTag());
			orderHolder.getOrder().setStatus(data.getStringExtra("status"));
			adapter.notifyDataSetChanged();
		}
		super.onActivityResult(requestCode, resultCode, data);
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
	private Button sendMessageButton;
	private Button callButton;
	private OrdersListAdapter adapter;
	private String id;
	private Object lock = new Object();
	private int lastPositionClicked;
	private SharedPreferences settingsData;
	private boolean isDownloading;
	private boolean hasNext;
	private int packAmount;
	private int currentAmount;
	ListView ordersListView;
}
