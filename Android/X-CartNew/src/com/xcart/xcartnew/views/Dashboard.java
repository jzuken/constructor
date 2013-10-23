package com.xcart.xcartnew.views;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xcart.xcartnew.R;
import com.xcart.xcartnew.managers.network.GetRequester;
import com.xcart.xcartnew.managers.network.HttpManager;

public class Dashboard extends PinSupportNetworkActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		initTodaySales();
		initLowStock();
		initVisitorsToday();
		initProductsSold();
		initReviewsToday();
		initLastOrders();
		authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
	}

	@Override
	protected void withoutPinAction() {
		if (isNeedDownload()) {
			clearData();
			updateData();
		}
		super.withoutPinAction();
	}

	private void updateData() {
		progressBar.setVisibility(View.VISIBLE);
		GetRequester dataRequester = new GetRequester() {

            @Override
            protected String doInBackground(Void... voids) {
                return new HttpManager(getBaseContext(),authorizationData.getString("sid", "")).getDashboard();
            }

			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONObject obj = new JSONObject(result);
						todaySales.setText(obj.getString("today_sales"));
						lowStock.setText(obj.getString("low_stock"));
						visitorsToday.setText(obj.getString("today_visitors"));
						reviewsToday.setText(obj.getString("reviews_today"));
						productsSold.setText(obj.getString("today_sold"));
						lastOrdersCount.setText("(" + obj.getString("today_orders_count") + ")");
						JSONArray orders = obj.getJSONArray("today_orders");
						for (int i = 0; i < orders.length(); i++) {
							JSONObject order = orders.getJSONObject(i);
							String title = order.getString("title");
							if (!title.equals("")) {
								title += " ";
							}
							String name = title + order.getString("firstname") + " " + order.getString("lastname");
							customers[i].setText(name);
							customersPaid[i].setText("$" + order.getString("total"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					showConnectionErrorMessage();
				}
				progressBar.setVisibility(View.GONE);
			}
		};

		setRequester(dataRequester);
		dataRequester.execute();
	}

	private void clearData() {
		todaySales.setText("");
		lowStock.setText("");
		visitorsToday.setText("");
		productsSold.setText("");
		reviewsToday.setText("");
		lastOrdersCount.setText("");
		for (int i = 0; i < 3; i++) {
			customers[i].setText("");
			customersPaid[i].setText("");
		}
	}

	private void initTodaySales() {
		todaySalesLayout = (LinearLayout) findViewById(R.id.today_sales_layout);
		todaySales = (TextView) findViewById(R.id.today_sales);

		todaySalesLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNeedDownloadValue(false);
				Intent completeOrders = new Intent(getBaseContext(), Orders.class);
				completeOrders.putExtra("sortOption", "complete");
				startActivityForResult(completeOrders, 1);
			}
		});
	}

	private void initLowStock() {
		lowStockLayout = (LinearLayout) findViewById(R.id.low_stock_layout);
		lowStock = (TextView) findViewById(R.id.low_stock);

		lowStockLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNeedDownloadValue(false);
				Intent lowStockProducts = new Intent(getBaseContext(), Products.class);
				lowStockProducts.putExtra("sortOption", "lowStock");
				startActivityForResult(lowStockProducts, 1);
			}
		});
	}

	private void initVisitorsToday() {
		visitorsTodayLayout = (LinearLayout) findViewById(R.id.visitors_today_layout);
		visitorsToday = (TextView) findViewById(R.id.visitors_today);

		visitorsTodayLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNeedDownloadValue(false);
				Intent todayUsers = new Intent(getBaseContext(), Users.class);
				todayUsers.putExtra("sortOption", "today");
				startActivityForResult(todayUsers, 1);
			}
		});
	}

	private void initProductsSold() {
		productsSoldLayout = (LinearLayout) findViewById(R.id.products_sold_today_layout);
		productsSold = (TextView) findViewById(R.id.products_sold_today);

		productsSoldLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNeedDownloadValue(false);
				Intent todayProducts = new Intent(getBaseContext(), Products.class);
				todayProducts.putExtra("sortOption", "all");
				startActivityForResult(todayProducts, 1);
			}
		});
	}

	private void initReviewsToday() {
		reviewsTodayLayout = (LinearLayout) findViewById(R.id.reviews_today_layout);
		reviewsToday = (TextView) findViewById(R.id.reviews_today);

		reviewsTodayLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNeedDownloadValue(false);
				Intent todayReviews = new Intent(getBaseContext(), Reviews.class);
				todayReviews.putExtra("sortOption", "today");
				startActivityForResult(todayReviews, 1);
			}
		});
	}

	private void initLastOrders() {
		lastOrdersLayout = (RelativeLayout) findViewById(R.id.last_orders_layout);
		lastOrdersCount = (TextView) findViewById(R.id.last_orders_count);

		lastOrdersLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNeedDownloadValue(false);
				Intent lastOrders = new Intent(getBaseContext(), Orders.class);
				lastOrders.putExtra("sortOption", "all");
				startActivityForResult(lastOrders, 1);
			}
		});

		customers[0] = (TextView) findViewById(R.id.first_customer);
		customersPaid[0] = (TextView) findViewById(R.id.first_customer_paid);
		customers[1] = (TextView) findViewById(R.id.second_customer);
		customersPaid[1] = (TextView) findViewById(R.id.second_customer_paid);
		customers[2] = (TextView) findViewById(R.id.third_customer);
		customersPaid[2] = (TextView) findViewById(R.id.third_customer_paid);
	}

	private ProgressBar progressBar;
	private LinearLayout todaySalesLayout;
	private TextView todaySales;
	private LinearLayout lowStockLayout;
	private TextView lowStock;
	private LinearLayout visitorsTodayLayout;
	private TextView visitorsToday;
	private LinearLayout productsSoldLayout;
	private TextView productsSold;
	private LinearLayout reviewsTodayLayout;
	private TextView reviewsToday;
	private RelativeLayout lastOrdersLayout;
	private TextView lastOrdersCount;
	private TextView[] customers = new TextView[3];
	private TextView[] customersPaid = new TextView[3];
	private SharedPreferences authorizationData;
}
