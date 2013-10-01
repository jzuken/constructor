package com.xcart.xcartnew;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Orders extends PinSupportNetworkActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orders);
		setupListViewAdapter();

		// test
		addOrderToList("1000", "Smith, Michelle", "$460.99", "Complete", "6/22/2013", "2");
		addOrderToList("999", "Smith, John", "$914.99", "Complete", "6/22/2013", "3");
	}

	private void addOrderToList(final String id, final String userName, final String paid, final String status,
			final String date, final String itemsCount) {
		adapter.add(new Order(id, userName, paid, status, date, itemsCount));
	}

	private void setupListViewAdapter() {
		adapter = new OrdersListAdapter(this, R.layout.order_item, new ArrayList<Order>());
		ordersListView = (ListView) findViewById(R.id.orders_list);

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

	private OrdersListAdapter adapter;
	private ListView ordersListView;
}
