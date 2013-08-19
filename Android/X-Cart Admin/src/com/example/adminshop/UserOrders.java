package com.example.adminshop;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class UserOrders extends PinSupportNetworkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orders_list);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		setupListViewAdapter();
	}

	@Override
	protected void withoutPinAction() {
		if (isNeedDownload()) {
			clearList();
			updateOrdersList();
		}
	}

	private enum StatusSymbols {
		I, Q, P, X, D, F, C
	}

	private void updateOrdersList() {
		progressBar.setVisibility(View.VISIBLE);
		synchronized (lock) {
			isDownloading = true;
		}
		hasNext = false;
		GetRequester dataRequester = new GetRequester() {
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONArray array = new JSONArray(result);
						int length = array.length();
						if (length == packAmount) {
							hasNext = true;
						}
						for (int i = 0; i < length; i++) {
							addOrder(array.getJSONObject(i));
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					showConnectionErrorMessage();
				}
				progressBar.setVisibility(View.GONE);
				ordersListView.onRefreshComplete();
				synchronized (lock) {
					isDownloading = false;
				}
			}
		};

		dataRequester.execute("http://54.213.38.9/xcart/api.php?request=user_orders&user_id="
				+ getIntent().getStringExtra("userId") + "&from=" + String.valueOf(currentAmount) + "&size="
				+ String.valueOf(packAmount));
	}

	private void addOrder(JSONObject obj) {
		try {
			String id = obj.getString("orderid");
			String date = obj.getString("date");

			StatusSymbols statusSymbol = StatusSymbols.valueOf(obj.getString("status"));
			String status = getStatusBySymbol(statusSymbol);
			String totalPrice = "$" + obj.getString("total");

			JSONArray detailsArray = obj.getJSONArray("details");
			StringBuilder productsInfo = new StringBuilder();
			int arrayLenght = detailsArray.length();

			for (int i = 0; i < arrayLenght - 1; i++) {
				JSONObject detailsObj = detailsArray.getJSONObject(i);
				productsInfo.append(detailsObj.getString("product") + ", " + "Quantity: "
						+ detailsObj.getString("amount") + "\n\n");
			}

			JSONObject detailsObj = detailsArray.getJSONObject(arrayLenght - 1);
			productsInfo.append(detailsObj.getString("product") + ", " + "Quantity: " + detailsObj.getString("amount"));

			String products = productsInfo.toString();

			addOrderToList(id, date, products, status, totalPrice);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private String getStatusBySymbol(StatusSymbols symbol) {
		switch (symbol) {
		case I:
			return "Not finished";
		case Q:
			return "Queued";
		case P:
			return "Processed";
		case X:
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

	private void addOrderToList(final String id, final String date, final String products, final String status,
			final String totalPrice) {
		adapter.add(new Order(id, "Order " + String.valueOf(position), date, products, status, totalPrice));
		position++;
	}

	private void setupListViewAdapter() {
		adapter = new OrdersListAdapter(this, R.layout.user_orders_item, new ArrayList<Order>());
		ordersListView = (PullToRefreshListView) findViewById(R.id.user_orders_list);

		LayoutInflater inflater = getLayoutInflater();
		View listFooter = inflater.inflate(R.layout.on_demand_footer, null, false);
		progressBar = (ProgressBar) listFooter.findViewById(R.id.progress_bar);
		ordersListView.getRefreshableView().addFooterView(listFooter, null, false);

		ordersListView.getRefreshableView().setFooterDividersEnabled(false);

		ordersListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				Log.d("test", String.valueOf(totalItemCount));
				if (totalItemCount > startItemCount && firstVisibleItem + visibleItemCount == totalItemCount
						&& !isDownloading && hasNext) {
					updateOrdersList();
				}
			}
		});

		ordersListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				clearList();
				updateOrdersList();
			}
		});

		ordersListView.setAdapter(adapter);
	}

	private void clearList() {
		adapter.clear();
		position = 1;
		currentAmount = 0;
	}

	private int position = 1;
	private ProgressBar progressBar;
	private OrdersListAdapter adapter;
	private int currentAmount;
	private boolean isDownloading;
	private boolean hasNext;
	private int packAmount = 10;
	private final int startItemCount = 3;
	PullToRefreshListView ordersListView;
	private Object lock = new Object();
}