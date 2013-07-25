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
import android.util.TypedValue;
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
		totalPrice = (TextView) page1.findViewById(R.id.last_order_price);
		user = (TextView) page1.findViewById(R.id.last_order_user);
		status = (TextView) page1.findViewById(R.id.last_order_status);
		lastOrderPrBar = (ProgressBar) page1.findViewById(R.id.progress_bar);
		pages.add(page1);

		View page2 = inflater.inflate(R.layout.orders_info, null);
		totalOrders = (TextView) page2.findViewById(R.id.total_orders);
		completeOrders = (TextView) page2.findViewById(R.id.complete_orders);
		notFinishedOrders = (TextView) page2.findViewById(R.id.not_finished_orders);
		queuedOrders = (TextView) page2.findViewById(R.id.queued_orders);
		processedOrders = (TextView) page2.findViewById(R.id.processed_orders);
		backorderedOrders = (TextView) page2.findViewById(R.id.backordered_orders);
		declinedOrders = (TextView) page2.findViewById(R.id.declined_orders);
		failedOrders = (TextView) page2.findViewById(R.id.failed_orders);
		totalPaid = (TextView) page2.findViewById(R.id.total_paid);

		ordersInfoPeriod = 1;
		TabHost ordersTabHost = (TabHost) page2.findViewById(android.R.id.tabhost);
		newPeriodTabHost(ordersTabHost);

		ordersTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				ordersInfoPeriod = periodIdByTag(tabId);
				updateOrdersInfoData();
			}
		});

		ordersInfoPrBar = (ProgressBar) page2.findViewById(R.id.progress_bar);
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

		topSellersPeriod = 1;
		TabHost sellersTabHost = (TabHost) page4.findViewById(android.R.id.tabhost);
		newPeriodTabHost(sellersTabHost);

		sellersTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				topSellersPeriod = periodIdByTag(tabId);
				updateTopSellersData();
			}
		});

		topSellersPrBar = (ProgressBar) page4.findViewById(R.id.progress_bar);
		noProducts = (TextView) page4.findViewById(R.id.no_products_sold);

		topSellersTable = (TableLayout) page4.findViewById(R.id.top_sellers_table);
		rowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		itemParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		itemParams.weight = 1;
		pages.add(page4);

		SwipingPagerAdapter pagerAdapter = new SwipingPagerAdapter(pages);
		ViewPager viewPager = (ViewPager) findViewById(R.id.dashbroad_view_pager);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(0);
	}

	@Override
	protected void withoutPinAction() {
		updateLastOrderData();
		updateOrdersInfoData();
		updateTopSellersData();
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

	private int periodIdByTag(String tag) {
		if (tag.equals("tag1")) {
			return 1;
		}
		if (tag.equals("tag2")) {
			return 2;
		}
		if (tag.equals("tag3")) {
			return 3;
		}
		return 4;
	}

	private void addPositionToTable(String name, String quantity) {
		TableRow newRow = new TableRow(this);
		newRow.setLayoutParams(rowParams);
		TextView newItem = newTableTextView(String.valueOf(position) + ". " + name, Gravity.LEFT, 10);
		TextView newItemQuantity = newTableTextView(quantity, Gravity.CENTER, 0);
		newRow.addView(newItem);
		newRow.addView(newItemQuantity);
		topSellersTable.addView(newRow);
		position++;
	}

	private TextView newTableTextView(CharSequence text, int gravity, int paddingLeft) {
		TextView textView = new TextView(this);
		textView.setText(text);
		textView.setMaxWidth(convertPixelsToDip(100));
		textView.setGravity(gravity);
		textView.setLayoutParams(itemParams);
		textView.setPadding(paddingLeft, 0, 0, 0);
		return textView;
	}

	private int convertPixelsToDip(int px) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
	}

	private void clearTable() {
		topSellersTable.removeViews(1, position - 1);
		position = 1;
	}

	private enum StatusSymbols {
		I, Q, P, X, D, F, C
	}

	private void updateLastOrderData() {
		lastOrderPrBar.setVisibility(View.VISIBLE);
		GetRequester dataRequester = new GetRequester() {
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONObject obj = new JSONObject(result);
						id.setText(obj.getString("orderid"));
						// date.setText(getFormatDate(Long.parseLong(obj.getString("date"))));
						totalPrice.setText("$" + obj.getString("total"));
						user.setText(obj.getString("title") + " " + obj.getString("firstname") + " "
								+ obj.getString("lastname"));
						StatusSymbols statusSymbol = StatusSymbols.valueOf(obj.getString("status"));

						switch (statusSymbol) {
						case I:
							status.setText("Not finished");
							break;
						case Q:
							status.setText("Queued");
							break;
						case P:
							status.setText("Processed");
							break;
						case X:
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

						JSONArray detailsArray = obj.getJSONArray("details");
						StringBuilder productsInfo = new StringBuilder();
						int arrayLenght = detailsArray.length();

						for (int i = 0; i < arrayLenght - 1; i++) {
							JSONObject detailsObj = detailsArray.getJSONObject(i);
							productsInfo.append(detailsObj.getString("product") + ", " + "Quantity: "
									+ detailsObj.getString("amount") + "\n\n");
						}

						JSONObject detailsObj = detailsArray.getJSONObject(arrayLenght - 1);
						productsInfo.append(detailsObj.getString("product") + ", " + "Quantity: "
								+ detailsObj.getString("amount"));

						product.setText(productsInfo.toString());

					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					showConnectionErrorMessage();
				}
				lastOrderPrBar.setVisibility(View.GONE);
			}
		};

		dataRequester.execute("http://54.213.38.9/xcart/api.php?request=last_order");
	}

	private void updateOrdersInfoData() {
		ordersInfoPrBar.setVisibility(View.VISIBLE);
		GetRequester dataRequester = new GetRequester() {
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONObject obj = new JSONObject(result);
						JSONObject currentPeriodObj;
						switch (ordersInfoPeriod) {
						case 1:
							currentPeriodObj = obj.getJSONObject("last_login");
							break;
						case 2:
							currentPeriodObj = obj.getJSONObject("today");
							break;
						case 3:
							currentPeriodObj = obj.getJSONObject("week");
							break;
						case 4:
							currentPeriodObj = obj.getJSONObject("month");
							break;
						default:
							currentPeriodObj = null;
							break;
						}

						completeOrders.setText(currentPeriodObj.getString("C"));
						notFinishedOrders.setText(currentPeriodObj.getString("I"));
						queuedOrders.setText(currentPeriodObj.getString("Q"));
						processedOrders.setText(currentPeriodObj.getString("P"));
						backorderedOrders.setText(currentPeriodObj.getString("X"));
						declinedOrders.setText(currentPeriodObj.getString("D"));
						failedOrders.setText(currentPeriodObj.getString("F"));
						totalOrders.setText(currentPeriodObj.getString("Total"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					showConnectionErrorMessage();
				}
				ordersInfoPrBar.setVisibility(View.GONE);
			}
		};

		dataRequester.execute("http://54.213.38.9/xcart/api.php?request=orders_statistic");
	}

	private void updateTopSellersData() {
		clearTable();
		noProducts.setText("");
		topSellersPrBar.setVisibility(View.VISIBLE);
		GetRequester dataRequester = new GetRequester() {
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONObject obj = new JSONObject(result);
						JSONArray currentPeriodArray = new JSONArray();
						switch (topSellersPeriod) {
						case 1:
							if (obj.optBoolean("last_login", true)) {
								currentPeriodArray = obj.getJSONArray("last_login");
							} else {
								noProducts.setText("No products sold during this period");
							}
							break;
						case 2:
							if (obj.optBoolean("today", true)) {
								currentPeriodArray = obj.getJSONArray("today");
							} else {
								noProducts.setText("No products sold during this period");
							}
							break;
						case 3:
							if (obj.optBoolean("week", true)) {
								currentPeriodArray = obj.getJSONArray("week");
							} else {
								noProducts.setText("No products sold during this period");
							}
							break;
						case 4:
							if (obj.optBoolean("month", true)) {
								currentPeriodArray = obj.getJSONArray("month");
							} else {
								noProducts.setText("No products sold during this period");
							}
							break;
						default:
							break;
						}

						for (int i = 0; i < currentPeriodArray.length(); i++) {
							JSONObject currentProduct = currentPeriodArray.getJSONObject(i);
							addPositionToTable(currentProduct.getString("product"), currentProduct.getString("count"));
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					showConnectionErrorMessage();
				}
				topSellersPrBar.setVisibility(View.GONE);
			}
		};

		dataRequester.execute("http://54.213.38.9/xcart/api.php?request=top_products");
	}

	private void showConnectionErrorMessage() {
		Toast.makeText(getBaseContext(),
				"Sorry, unable to connect to server. Cannot update data. Please check your internet connection",
				Toast.LENGTH_LONG).show();
	}

	private String getFormatDate(Long seconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(seconds * 1000L);
		return dateNumber(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + dateNumber(calendar.get(Calendar.MONTH) + 1)
				+ "/" + calendar.get(Calendar.YEAR) + " " + dateNumber(calendar.get(Calendar.HOUR_OF_DAY)) + ":"
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
	private TextView totalPrice;
	private TextView user;
	private TextView status;
	private TextView totalOrders;
	private TextView completeOrders;
	private TextView notFinishedOrders;
	private TextView queuedOrders;
	private TextView processedOrders;
	private TextView backorderedOrders;
	private TextView declinedOrders;
	private TextView failedOrders;
	private TextView totalPaid;
	private TextView noProducts;
	private TableLayout topSellersTable;
	private LayoutParams rowParams;
	private TableRow.LayoutParams itemParams;
	private int position = 1;
	private ProgressBar lastOrderPrBar;
	private ProgressBar ordersInfoPrBar;
	private ProgressBar topSellersPrBar;
	private int ordersInfoPeriod;
	private int topSellersPeriod;
}