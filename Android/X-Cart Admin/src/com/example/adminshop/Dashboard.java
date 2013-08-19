package com.example.adminshop;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TableRow;
import android.widget.TextView;

public class Dashboard extends PinSupportNetworkActivity {

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
		setupOrdersInfoTable(page2);
		ordersInfoPrBar = (ProgressBar) page2.findViewById(R.id.progress_bar);
		pages.add(page2);

		View page3 = inflater.inflate(R.layout.sales_growth, null);

		TabHost growthTabHost = (TabHost) page3.findViewById(android.R.id.tabhost);
		newPeriodTabHost(growthTabHost);

		growthTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
			}
		});

		// temporarily hidden
		// pages.add(page3);

		View page4 = inflater.inflate(R.layout.top_sellers, null);

		topSellersPeriod = 1;
		TabHost sellersTabHost = (TabHost) page4.findViewById(android.R.id.tabhost);
		newPeriodTabHost(sellersTabHost);

		sellersTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				topSellersPeriod = periodIdByTag(tabId);
				if (!isTopProductsDownloading) {
					clearTopSellersTable();
					setProductsToTable();
				}
			}
		});

		topSellersPrBar = (ProgressBar) page4.findViewById(R.id.progress_bar);
		noProducts = (TextView) page4.findViewById(R.id.no_products_sold);

		topSellersTable = (StatisticTable) page4.findViewById(R.id.top_sellers_table);
		pages.add(page4);

		View page5 = inflater.inflate(R.layout.top_categories, null);

		topCategoriesPeriod = 1;
		TabHost categoriesTabHost = (TabHost) page5.findViewById(android.R.id.tabhost);
		newPeriodTabHost(categoriesTabHost);

		categoriesTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				topCategoriesPeriod = periodIdByTag(tabId);
				if (!isTopCategoriesDownloading) {
					clearTopCategoriesTable();
					setCategoriesToTable();
				}
			}
		});

		topCategoriesPrBar = (ProgressBar) page5.findViewById(R.id.progress_bar);
		noCategories = (TextView) page5.findViewById(R.id.no_categories);

		topCategoriesTable = (StatisticTable) page5.findViewById(R.id.top_categories_table);
		pages.add(page5);

		SwipingPagerAdapter pagerAdapter = new SwipingPagerAdapter(pages);
		ViewPager viewPager = (ViewPager) findViewById(R.id.dashbroad_view_pager);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(0);

		currentPage = 0;

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				currentPage = position;
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}

	private void setupOrdersInfoTable(final View page) {
		totalOrders = (TableRow) page.findViewById(R.id.total_orders_row);
		completeOrders = (TableRow) page.findViewById(R.id.complete_orders_row);
		notFinishedOrders = (TableRow) page.findViewById(R.id.not_finished_row);
		queuedOrders = (TableRow) page.findViewById(R.id.queued_row);
		processedOrders = (TableRow) page.findViewById(R.id.processed_row);
		backorderedOrders = (TableRow) page.findViewById(R.id.backordered_row);
		declinedOrders = (TableRow) page.findViewById(R.id.declined_row);
		failedOrders = (TableRow) page.findViewById(R.id.failed_row);
		grossTotal = (TableRow) page.findViewById(R.id.gross_total_row);
		totalPaid = (TableRow) page.findViewById(R.id.total_paid_row);
	}

	@Override
	protected void withoutPinAction() {
		if (isNeedDownload()) {
			updateLastOrderData();
			updateOrdersInfoData();
			updateTopSellersData();
			updateTopCategoriesData();
		}
		super.withoutPinAction();
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

	private enum StatusSymbols {
		I, Q, P, X, D, F, C
	}

	private void updateLastOrderData() {
		clearLastOrderInfo();
		lastOrderPrBar.setVisibility(View.VISIBLE);
		GetRequester dataRequester = new GetRequester() {
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONObject obj = new JSONObject(result);
						id.setText(obj.getString("orderid"));
						date.setText(obj.getString("date"));
						
						totalPrice.setText("$" + obj.getString("total"));
						user.setText(obj.getString("title") + " " + obj.getString("firstname") + " "
								+ obj.getString("lastname"));
						StatusSymbols statusSymbol = StatusSymbols.valueOf(obj.getString("status"));
						status.setText(getStatusBySymbol(statusSymbol));

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
					if (currentPage == 0) {
						showConnectionErrorMessage();
					}
				}
				lastOrderPrBar.setVisibility(View.GONE);
			}
		};

		dataRequester.execute("http://54.213.38.9/xcart/api.php?request=last_order");
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

	private void clearLastOrderInfo() {
		id.setText("");
		date.setText("");
		product.setText("");
		totalPrice.setText("");
		user.setText("");
		status.setText("");
	}

	private void updateOrdersInfoData() {
		clearOrdersInfo();
		ordersInfoPrBar.setVisibility(View.VISIBLE);
		GetRequester dataRequester = new GetRequester() {
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONObject obj = new JSONObject(result);
						JSONObject currentPeriodObj = null;
						;
						JSONObject lastLoginObj = obj.getJSONObject("last_login");
						JSONObject todayObj = obj.getJSONObject("today");
						JSONObject weekObj = obj.getJSONObject("week");
						JSONObject monthObj = obj.getJSONObject("month");

						for (int i = 1; i < ordersInfoCloumnCount; i++) {
							switch (i) {
							case 1:
								currentPeriodObj = lastLoginObj;
								break;

							case 2:
								currentPeriodObj = todayObj;
								break;
							case 3:
								currentPeriodObj = weekObj;
								break;

							case 4:
								currentPeriodObj = monthObj;
								break;
							}
							((TextView) totalOrders.getChildAt(i)).setText(currentPeriodObj.getString("Total"));
							((TextView) completeOrders.getChildAt(i)).setText(currentPeriodObj.getString("C"));
							((TextView) notFinishedOrders.getChildAt(i)).setText(currentPeriodObj.getString("I"));
							((TextView) queuedOrders.getChildAt(i)).setText(currentPeriodObj.getString("Q"));
							((TextView) processedOrders.getChildAt(i)).setText(currentPeriodObj.getString("P"));
							((TextView) backorderedOrders.getChildAt(i)).setText(currentPeriodObj.getString("X"));
							((TextView) declinedOrders.getChildAt(i)).setText(currentPeriodObj.getString("D"));
							((TextView) failedOrders.getChildAt(i)).setText(currentPeriodObj.getString("F"));
							((TextView) totalPaid.getChildAt(i)).setText(currentPeriodObj.getString("total_paid"));
							((TextView) grossTotal.getChildAt(i)).setText(currentPeriodObj.getString("gross_total"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					if (currentPage == 1) {
						showConnectionErrorMessage();
					}
				}
				ordersInfoPrBar.setVisibility(View.GONE);
			}
		};

		dataRequester.execute("http://54.213.38.9/xcart/api.php?request=orders_statistic");
	}

	private void clearOrdersInfo() {
		for (int i = 1; i < ordersInfoCloumnCount; i++) {
			((TextView) completeOrders.getChildAt(i)).setText("");
			((TextView) notFinishedOrders.getChildAt(i)).setText("");
			((TextView) queuedOrders.getChildAt(i)).setText("");
			((TextView) processedOrders.getChildAt(i)).setText("");
			((TextView) backorderedOrders.getChildAt(i)).setText("");
			((TextView) declinedOrders.getChildAt(i)).setText("");
			((TextView) failedOrders.getChildAt(i)).setText("");
			((TextView) totalOrders.getChildAt(i)).setText("");
			((TextView) totalPaid.getChildAt(i)).setText("");
			((TextView) grossTotal.getChildAt(i)).setText("");
		}
	}

	private void updateTopSellersData() {
		synchronized (topProductsLock) {
			isTopProductsDownloading = true;
		}
		clearTopSellersTable();
		topSellersPrBar.setVisibility(View.VISIBLE);
		GetRequester dataRequester = new GetRequester() {
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						currentTopProductsData = new JSONObject(result);
						setProductsToTable();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					if (currentPage == 3) {
						showConnectionErrorMessage();
					}
				}
				topSellersPrBar.setVisibility(View.GONE);
				synchronized (topProductsLock) {
					isTopProductsDownloading = false;
				}
			}
		};

		dataRequester.execute("http://54.213.38.9/xcart/api.php?request=top_products");
	}

	private void setProductsToTable() {
		try {
			JSONArray currentPeriodArray = new JSONArray();
			switch (topSellersPeriod) {
			case 1:
				if (currentTopProductsData.optString("last_login").equals("none")) {
					noProducts.setText("No products sold during this period");
				} else {
					currentPeriodArray = currentTopProductsData.getJSONArray("last_login");
				}
				break;
			case 2:
				if (currentTopProductsData.optString("today").equals("none")) {
					noProducts.setText("No products sold during this period");
				} else {
					currentPeriodArray = currentTopProductsData.getJSONArray("today");
				}
				break;
			case 3:
				if (currentTopProductsData.optString("week").equals("none")) {
					noProducts.setText("No products sold during this period");
				} else {
					currentPeriodArray = currentTopProductsData.getJSONArray("week");
				}
				break;
			case 4:
				if (currentTopProductsData.optString("month").equals("none")) {
					noProducts.setText("No products sold during this period");
				} else {
					currentPeriodArray = currentTopProductsData.getJSONArray("month");
				}
				break;
			default:
				break;
			}

			for (int i = 0; i < currentPeriodArray.length(); i++) {
				JSONObject currentProduct = currentPeriodArray.getJSONObject(i);
				topSellersTable.addPositionToTable(currentProduct.getString("product"),
						currentProduct.getString("count"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void clearTopSellersTable() {
		topSellersTable.clearTable();
		noProducts.setText("");
	}

	private void updateTopCategoriesData() {
		synchronized (topCategoriesLock) {
			isTopCategoriesDownloading = true;
		}
		clearTopCategoriesTable();
		topCategoriesPrBar.setVisibility(View.VISIBLE);
		GetRequester dataRequester = new GetRequester() {
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						currentTopCategoriesData = new JSONObject(result);
						setCategoriesToTable();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					if (currentPage == 4) {
						showConnectionErrorMessage();
					}
				}
				topCategoriesPrBar.setVisibility(View.GONE);
				synchronized (topCategoriesLock) {
					isTopCategoriesDownloading = false;
				}
			}
		};

		dataRequester.execute("http://54.213.38.9/xcart/api.php?request=top_categories");
	}
	
	private void setCategoriesToTable() {
		try {
		JSONArray currentPeriodArray = new JSONArray();
		switch (topCategoriesPeriod) {
		case 1:
			if (currentTopCategoriesData.optString("last_login").equals("none")) {
				noCategories.setText("No categories statistic during this period");
			} else {
				currentPeriodArray = currentTopCategoriesData.getJSONArray("last_login");
			}
			break;
		case 2:
			if (currentTopCategoriesData.optString("today").equals("none")) {
				noCategories.setText("No categories statistic during this period");
			} else {
				currentPeriodArray = currentTopCategoriesData.getJSONArray("today");
			}
			break;
		case 3:
			if (currentTopCategoriesData.optString("week").equals("none")) {
				noCategories.setText("No categories statistic during this period");
			} else {
				currentPeriodArray = currentTopCategoriesData.getJSONArray("week");
			}
			break;
		case 4:
			if (currentTopCategoriesData.optString("month").equals("none")) {
				noCategories.setText("No categories statistic during this period");
			} else {
				currentPeriodArray = currentTopCategoriesData.getJSONArray("month");
			}
			break;
		default:
			break;
		}

		for (int i = 0; i < currentPeriodArray.length(); i++) {
			JSONObject currentCategory = currentPeriodArray.getJSONObject(i);
			topCategoriesTable.addPositionToTable(currentCategory.getString("category"),
					currentCategory.getString("count"));
		}} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void clearTopCategoriesTable() {
		topCategoriesTable.clearTable();
		noCategories.setText("");
	}

	private TextView id;
	private TextView date;
	private TextView product;
	private TextView totalPrice;
	private TextView user;
	private TextView status;

	private TableRow totalOrders;
	private TableRow completeOrders;
	private TableRow notFinishedOrders;
	private TableRow queuedOrders;
	private TableRow processedOrders;
	private TableRow backorderedOrders;
	private TableRow declinedOrders;
	private TableRow failedOrders;
	private TableRow totalPaid;
	private TableRow grossTotal;

	private TextView noProducts;
	private TextView noCategories;
	private StatisticTable topSellersTable;
	private StatisticTable topCategoriesTable;
	private ProgressBar lastOrderPrBar;
	private ProgressBar ordersInfoPrBar;
	private ProgressBar topSellersPrBar;
	private ProgressBar topCategoriesPrBar;
	private int topSellersPeriod;
	private int topCategoriesPeriod;
	private int currentPage;
	private final int ordersInfoCloumnCount = 5;
	private JSONObject currentTopProductsData = new JSONObject();
	private boolean isTopProductsDownloading;
	private Object topProductsLock = new Object();
	private JSONObject currentTopCategoriesData = new JSONObject();
	private boolean isTopCategoriesDownloading;
	private Object topCategoriesLock = new Object();
}