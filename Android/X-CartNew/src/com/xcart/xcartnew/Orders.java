package com.xcart.xcartnew;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabWidget;
import android.widget.TextView;

public class Orders extends PinSupportNetworkActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orders);
		setupListViewAdapter();
		setupPeriodTabs();
		setupSearchLine();
		period = "today";
		settingsData = PreferenceManager.getDefaultSharedPreferences(this);
		authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
	}

	@Override
	protected void withoutPinAction() {
		packAmount = Integer.parseInt(settingsData.getString("orders_amount", "10"));
		if (isNeedDownload()) {
			clearList();
			updateOrdersList();
		}
		super.withoutPinAction();
	}

	private void updateOrdersList() {
		progressBar.setVisibility(View.VISIBLE);
		synchronized (lock) {
			isDownloading = true;
		}
		hasNext = false;
		GetRequester dataRequester = new GetRequester() {
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
							if (!title.equals("")) {
								title += " ";
							}
							String name = title + obj.getString("firstname") + " "
									+ obj.getString("lastname");
							String status = obj.getString("status");
							String date = obj.getString("month") + "\n" + obj.getString("day");
							;
							String paid = obj.getString("total");
							addOrderToList(id, name, paid, status, date);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					showConnectionErrorMessage();
				}
				progressBar.setVisibility(View.GONE);
				synchronized (lock) {
					isDownloading = false;
				}
			}
		};

		setRequester(dataRequester);
		dataRequester.execute("https://54.213.38.9/api/api2.php?request=last_orders&from="
				+ String.valueOf(currentAmount) + "&size=" + String.valueOf(packAmount) + "&status=&date=" + period
				+ "&sid=" + authorizationData.getString("sid", "") + "&search=" + searchWord);
		currentAmount += packAmount;
	}

	private void addOrderToList(final String id, final String userName, final String paid, final String status,
			final String date) {
		adapter.add(new Order(id, userName, paid, status, date));
	}

	private void setupListViewAdapter() {
		adapter = new OrdersListAdapter(this, R.layout.order_item, new ArrayList<Order>());
		ordersListView = (ListView) findViewById(R.id.orders_list);

		LayoutInflater inflater = getLayoutInflater();
		View listFooter = inflater.inflate(R.layout.on_demand_footer, null, false);
		progressBar = (ProgressBar) listFooter.findViewById(R.id.progress_bar);
		ordersListView.addFooterView(listFooter, null, false);

		ordersListView.setFooterDividersEnabled(false);

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
				showActionDialog(((Order) view.getTag()));
			}
		});

		ordersListView.setAdapter(adapter);
	}

	private void showActionDialog(final Order item) {
		LinearLayout action_view = (LinearLayout) getLayoutInflater().inflate(R.layout.action_dialog, null);
		final CustomDialog dialog = new CustomDialog(this, action_view);

		ListView actionList = (ListView) action_view.findViewById(R.id.action_list);

		String[] actions = { "Change status", "Full info", "User info", "Delete", "Cancel" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.action_item, R.id.textItem, actions);

		actionList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					changeStatusClick(item.getId());
					dialog.dismiss();
					break;
				case 1:
					fullInfoClick(item.getId());
					dialog.dismiss();
					break;
				case 2:
					dialog.dismiss();
					break;
				case 3:
					deleteClick(item.getId());
					dialog.dismiss();
					break;
				case 4:
					dialog.dismiss();
					break;
				default:
					break;
				}

			}
		});

		actionList.setAdapter(adapter);

		dialog.show();
	}

	private void changeStatusClick(final String id) {
		Intent intent = new Intent(this, ChangeStatus.class);
		intent.putExtra("orderId", id);
		startActivityForResult(intent, 1);
	}

	private void fullInfoClick(final String id) {
		Intent intent = new Intent(this, OrderInfo.class);
		intent.putExtra("orderId", id);
		startActivityForResult(intent, 1);
	}

	private void deleteClick(final String id) {
		LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.confirmation_dialog, null);
		((TextView) view.findViewById(R.id.confirm_question)).setText("Are you sure you want to delete this order?");
		final CustomDialog dialog = new CustomDialog(this, view);

		ImageButton noButton = (ImageButton) view.findViewById(R.id.dialog_no_button);
		noButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		ImageButton yesButton = (ImageButton) view.findViewById(R.id.dialog_yes_button);
		yesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				deleteOrder(id);
			}
		});

		dialog.show();
	}

	private void deleteOrder(String id) {

	}

	private void setupPeriodTabs() {
		periodTabHost = (CustomTabHost) findViewById(android.R.id.tabhost);
		periodTabHost.setup();
		periodTabHost.addEmptyTab("today", getResources().getString(R.string.today), -1);
		periodTabHost.addEmptyTab("week", getResources().getString(R.string.this_week), 0);
		periodTabHost.addEmptyTab("month", getResources().getString(R.string.this_month), 0);
		periodTabHost.addEmptyTab("all", getResources().getString(R.string.all), 1);

		TabWidget tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		final int tabChildrenCount = tabWidget.getChildCount();
		View currentView;
		for (int i = 0; i < tabChildrenCount - 1; i++) {
			currentView = tabWidget.getChildAt(i);
			LinearLayout.LayoutParams currentLayout = (LinearLayout.LayoutParams) currentView.getLayoutParams();
			currentLayout.setMargins(0, 0, 1, 0);
		}
		tabWidget.requestLayout();

		periodTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				period = tabId;
				cancelRequest();
				clearList();
				updateOrdersList();
			}
		});
	}

	private void setupSearchLine() {
		ordersSearchLine = (EditText) findViewById(R.id.search_line);
		ordersSearchLine.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					searchWord = ordersSearchLine.getText().toString();
					hideKeyboard(ordersSearchLine);
					clearList();
					updateOrdersList();
					return true;
				}
				return false;
			}
		});
	}

	private void hideKeyboard(EditText edit) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
	}

	private void clearList() {
		adapter.clear();
		currentAmount = 0;
	}

	private ProgressBar progressBar;
	private OrdersListAdapter adapter;
	private ListView ordersListView;
	private CustomTabHost periodTabHost;
	private String period;
	private Object lock = new Object();
	private int currentAmount;
	private boolean isDownloading;
	private boolean hasNext;
	private int packAmount;
	private SharedPreferences settingsData;
	private SharedPreferences authorizationData;
	private String searchWord = "";
	private EditText ordersSearchLine;
}
