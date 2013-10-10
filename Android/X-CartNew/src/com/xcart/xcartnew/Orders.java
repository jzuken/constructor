package com.xcart.xcartnew;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabWidget;
import android.widget.TextView;

public class Orders extends PinSupportNetworkActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orders);
		setupListViewAdapter();
		setupPeriodTabs();

		// test
		addOrderToList("1000", "Smith, Michelle", "$460.99", "Complete", "JUN\n22");
		addOrderToList("999", "Smith, John", "$914.99", "Complete", "JUN\n22");
	}

	private void addOrderToList(final String id, final String userName, final String paid, final String status,
			final String date) {
		adapter.add(new Order(id, userName, paid, status, date));
	}

	private void setupListViewAdapter() {
		adapter = new OrdersListAdapter(this, R.layout.order_item, new ArrayList<Order>());
		ordersListView = (ListView) findViewById(R.id.orders_list);
		
		ordersListView.setFooterDividersEnabled(false);

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
		    LinearLayout.LayoutParams currentLayout =
		        (LinearLayout.LayoutParams) currentView.getLayoutParams();
		    currentLayout.setMargins(0, 0, 1, 0);
		}
		tabWidget.requestLayout();
		
		periodTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				period = tabId;
			}
		});
	}

	private OrdersListAdapter adapter;
	private ListView ordersListView;
	private CustomTabHost periodTabHost;
	private String period;
}
