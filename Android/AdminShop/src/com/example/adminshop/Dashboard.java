package com.example.adminshop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Dashboard extends PinSupportActivity {

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
		progressBar = (ProgressBar) page1.findViewById(R.id.progress_bar);
		pages.add(page1);
		View page2 = inflater.inflate(R.layout.orders_info, null);
		totalOrders = (TextView) page2.findViewById(R.id.total_orders);
		completeOrders = (TextView) page2.findViewById(R.id.complete_orders);
		inProcessOrders = (TextView) page2.findViewById(R.id.in_process_orders);
		totalPaid = (TextView) page2.findViewById(R.id.total_paid);

		TabHost ordersTabHost = (TabHost) page2.findViewById(android.R.id.tabhost);
		newPeriodTabHost(ordersTabHost);

		ordersTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
			}
		});

		pages.add(page2);

		View page3 = inflater.inflate(R.layout.sales_growth, null);

		TabHost growthTabHost = (TabHost) page3.findViewById(android.R.id.tabhost);
		newPeriodTabHost(growthTabHost);

		growthTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
			}
		});

		pages.add(page3);

		View page4 = inflater.inflate(R.layout.top_sellers, null);

		TabHost sellersTabHost = (TabHost) page4.findViewById(android.R.id.tabhost);
		newPeriodTabHost(sellersTabHost);

		sellersTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
			}
		});

		topSellersTable = (TableLayout) page4.findViewById(R.id.top_sellers_table);
		rowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		itemParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		itemParams.weight = 1;
		pages.add(page4);

		SwipingPagerAdapter pagerAdapter = new SwipingPagerAdapter(pages);
		ViewPager viewPager = (ViewPager) findViewById(R.id.dashbroad_view_pager);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(0);

		// example
		addPositionToTable("first", 300);
		addPositionToTable("second", 100);
	}

	@Override
	protected void withoutPinAction() {
		initLastOrderData();
	}

	private void addEmptyTab(TabHost tabHost, String tag, String indicator) {
		TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
		View indicatorView = getLayoutInflater().inflate(R.layout.tab_indicator, null);
		TextView tabIndicator = (TextView) indicatorView.findViewById(R.id.tab_text);
		tabIndicator.setText(indicator);
		tabIndicator.setTextColor(Color.WHITE);
		tabSpec.setIndicator(indicatorView);
		tabSpec.setContent(R.id.emptyContent);
		tabHost.addTab(tabSpec);
	}

	private void newPeriodTabHost(TabHost tabHost) {
		tabHost.setup();
		addEmptyTab(tabHost, "tag1", "Since last login");
		addEmptyTab(tabHost, "tag2", "Today");
		addEmptyTab(tabHost, "tag3", "This week");
		addEmptyTab(tabHost, "tag4", "This month");
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

	private enum StatusSymbols {
		N, Q, P, B, D, F, C
	}

	private void initLastOrderData() {
		progressBar.setVisibility(View.VISIBLE);
		GetRequester dataRequester = new GetRequester() {
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONArray array = new JSONArray(result);
						JSONObject obj = array.getJSONObject(0);
						final String orderId = obj.getString("orderid");

						final Long dateInSeconds = Long.parseLong(obj.getString("date"));
						final String total = obj.getString("total");
						final String userText = obj.getString("title") + " " + obj.getString("firstname") + " "
								+ obj.getString("lastname");
						final StatusSymbols statusSymbol = StatusSymbols.valueOf(obj.getString("status"));

						GetRequester detailsRequester = new GetRequester() {
							protected void onPostExecute(String result) {
								if (result != null) {
									try {
										JSONArray detailsArray = new JSONArray(result);
										JSONObject detailsObj = detailsArray.getJSONObject(0);

										product.setText(detailsObj.getString("product"));
										quantity.setText(detailsObj.getString("amount"));
										id.setText(orderId);
										date.setText(getFormatDate(dateInSeconds));
										totalPrice.setText("$" + total);
										user.setText(userText);

										switch (statusSymbol) {
										case N:
											status.setText("Not finished");
											break;
										case Q:
											status.setText("Queued");
											break;
										case P:
											status.setText("Processed");
											break;
										case B:
											status.setText("Backordered");
											break;
										case D:
											status.setText("Declined");
											break;
										case F:
											status.setText("Failed");
											break;
										case C:
											status.setText("Comlete");
											break;
										default:
											break;
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

						detailsRequester
								.execute("http://54.213.38.9/xcart/api.php?request=order_details&id=" + orderId);

					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					progressBar.setVisibility(View.GONE);
					showConnectionErrorMessage();
				}
			};
		};
		dataRequester.execute("http://54.213.38.9/xcart/api.php?request=last_order");
	}

	private void showConnectionErrorMessage() {
		Toast.makeText(getBaseContext(),
				"Sorry, unable to connect to server. Cannot update data. Please check your internet connection",
				Toast.LENGTH_LONG).show();
	}

	private String getFormatDate(Long seconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(seconds * 1000L);
		return dateNumber(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + dateNumber(calendar.get(Calendar.MONTH)) + "/"
				+ calendar.get(Calendar.YEAR) + " " + dateNumber(calendar.get(Calendar.HOUR_OF_DAY)) + ":"
				+ dateNumber(calendar.get(Calendar.MINUTE));
	}

	private String dateNumber(int number) {
		if (number < 10) {
			return "0" + String.valueOf(number);
		} else {
			return String.valueOf(number);
		}
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
	private TableLayout topSellersTable;
	private LayoutParams rowParams;
	private TableRow.LayoutParams itemParams;
	private int position = 1;
	private ProgressBar progressBar;
}