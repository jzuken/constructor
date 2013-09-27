package com.xcart.xcartnew;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Dashboard extends PinSupportNetworkActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		initTodaySales();
		initLowStock();
		initVisitorsToday();
		initProductsSold();
		initReviewsToday();
		initLastOrders();
	}

	private void initTodaySales() {
		todaySalesLayout = (LinearLayout) findViewById(R.id.today_sales_layout);
		todaySales = (TextView) findViewById(R.id.today_sales);

		todaySalesLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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
				Intent todayReviews = new Intent(getBaseContext(), Reviews.class);
				todayReviews.putExtra("sortOption", "today");
				startActivityForResult(todayReviews, 1);
			}
		});
	}

	private void initLastOrders() {
		lastOrdersLayout = (LinearLayout) findViewById(R.id.last_orders_layout);
		lastOrdersCount = (TextView) findViewById(R.id.last_orders_count);

		lastOrdersLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent lastOrders = new Intent(getBaseContext(), Orders.class);
				lastOrders.putExtra("sortOption", "all");
				startActivityForResult(lastOrders, 1);
			}
		});

		firstCustomer = (TextView) findViewById(R.id.first_customer);
		firstCustomerPaid = (TextView) findViewById(R.id.first_customer_paid);
		secondCustomer = (TextView) findViewById(R.id.second_customer);
		secondCustomerPaid = (TextView) findViewById(R.id.second_customer_paid);
		thirdCustomer = (TextView) findViewById(R.id.third_customer);
		thirdCustomerPaid = (TextView) findViewById(R.id.third_customer_paid);
	}

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
	private LinearLayout lastOrdersLayout;
	private TextView lastOrdersCount;
	private TextView firstCustomer;
	private TextView firstCustomerPaid;
	private TextView secondCustomer;
	private TextView secondCustomerPaid;
	private TextView thirdCustomer;
	private TextView thirdCustomerPaid;
}
