package com.example.adminshop;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
		topSellersTable = (TableLayout) page4.findViewById(R.id.top_sellers_table);
		rowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		itemParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		itemParams.weight = 1;
		pages.add(page4);

		SwipingPagerAdapter pagerAdapter = new SwipingPagerAdapter(pages);
		ViewPager viewPager = (ViewPager) findViewById(R.id.dashbroad_view_pager);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(0);

		//example
		addPositionToTable("first", 300);
		addPositionToTable("second", 100);
	}

	private void addPositionToTable(String name, int quantity) {
		TableRow newRow = new TableRow(this);
		newRow.setLayoutParams(rowParams);
		TextView newItem = newTableTextView(String.valueOf(position) + ". " + name);
		TextView newItemQuantity = newTableTextView(String.valueOf(quantity));
		newRow.addView(newItem);
		newRow.addView(newItemQuantity);
		topSellersTable.addView(newRow);
		position++;
	}
	
	private TextView newTableTextView(CharSequence text) {
		TextView textView = new TextView(this);
		textView.setText(text);
		textView.setGravity(Gravity.CENTER);
		textView.setLayoutParams(itemParams);
		return textView;
	}
	
	private void clearTable() {
		topSellersTable.removeViews(1, position - 1);
		position = 1;
	}

	public void settingsClick(View v) {
		Intent intent = new Intent(this, Settings.class);
		startActivity(intent);
	}

	public void backClick(View v) {
		this.finish();
	}

	private TextView id;
	private TextView date;
	private TextView product;
	private TextView quantity;
	private TextView totalPrice;
	private TextView user;
	private TextView status;
	private TextView totalOrders;
	private TextView completeOrders;
	private TextView inProcessOrders;
	private TextView totalPaid;
	private Spinner datePeriodSpinner;
	private TableLayout topSellersTable;
	private LayoutParams rowParams;
	private TableRow.LayoutParams itemParams;
	private int datePeriod;
	private int position = 1;
}