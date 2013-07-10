package com.example.adminshop;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Dashboard extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);

		LayoutInflater inflater = LayoutInflater.from(this);
		List<View> pages = new ArrayList<View>();

		View page1 = inflater.inflate(R.layout.last_order, null);
		id = (TextView) page1.findViewById(R.id.last_order_id);
		date = (TextView) page1.findViewById(R.id.last_order_date);
		product = (TextView) page1.findViewById(R.id.last_order_product);
		quantity = (TextView) page1.findViewById(R.id.last_order_quantity);
		totalPrice = (TextView) page1.findViewById(R.id.last_order_price);
		user = (TextView) page1.findViewById(R.id.last_order_user);
		status = (TextView) page1.findViewById(R.id.last_order_status);
		pages.add(page1);
		View page2 = inflater.inflate(R.layout.orders_info, null);
		totalOrders = (TextView) page2.findViewById(R.id.total_orders);
		completeOrders = (TextView) page2.findViewById(R.id.complete_orders);
		inProcessOrders = (TextView) page2.findViewById(R.id.in_process_orders);
		totalPaid = (TextView) page2.findViewById(R.id.total_paid);
		datePeriodSpinner = (Spinner) page2.findViewById(R.id.date_period_spinner);

		String[] data = { "All dates", "This month", "This week", "Today" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		datePeriodSpinner.setAdapter(adapter);
		datePeriodSpinner.setPrompt("Title");
		datePeriodSpinner.setSelection(0);
		datePeriodSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				datePeriod = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		pages.add(page2);
		View page3 = inflater.inflate(R.layout.sales_growth, null);
		pages.add(page3);
		View page4 = inflater.inflate(R.layout.top_sellers, null);
		pages.add(page4);

		SwipingPagerAdapter pagerAdapter = new SwipingPagerAdapter(pages);
		ViewPager viewPager = (ViewPager) findViewById(R.id.dashbroad_view_pager);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(0);
	}

	public void settingsClick(View v) {
		Intent intent = new Intent(this, Settings.class);
		startActivity(intent);
	}

	public void backClick(View v) {
		this.finish();
	}

	TextView id;
	TextView date;
	TextView product;
	TextView quantity;
	TextView totalPrice;
	TextView user;
	TextView status;
	TextView totalOrders;
	TextView completeOrders;
	TextView inProcessOrders;
	TextView totalPaid;
	Spinner datePeriodSpinner;
	int datePeriod;
}