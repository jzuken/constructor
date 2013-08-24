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
import android.widget.ListView;
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

		View ordersInfoPage = inflater.inflate(R.layout.orders_info, null);
		setupOrdersInfoTable(ordersInfoPage);
		ordersInfoPrBar = (ProgressBar) ordersInfoPage.findViewById(R.id.progress_bar);
		pages.add(ordersInfoPage);

		View salesGrowthPage = inflater.inflate(R.layout.sales_growth, null);

		TabHost growthTabHost = (TabHost) salesGrowthPage.findViewById(android.R.id.tabhost);
		newPeriodTabHost(growthTabHost);

		growthTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
			}
		});

		// temporarily hidden
		// pages.add(page3);

		View topProductsPage = inflater.inflate(R.layout.top_products, null);

		topSellersPeriod = 1;
		TabHost sellersTabHost = (TabHost) topProductsPage.findViewById(android.R.id.tabhost);
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

		topSellersPrBar = (ProgressBar) topProductsPage.findViewById(R.id.progress_bar);
		noProducts = (TextView) topProductsPage.findViewById(R.id.no_products_sold);

		setupTopProductsListViewAdapter(topProductsPage);
		pages.add(topProductsPage);

		View topCategoriesPage = inflater.inflate(R.layout.top_categories, null);

		topCategoriesPeriod = 1;
		TabHost categoriesTabHost = (TabHost) topCategoriesPage.findViewById(android.R.id.tabhost);
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

		topCategoriesPrBar = (ProgressBar) topCategoriesPage.findViewById(R.id.progress_bar);
		noCategories = (TextView) topCategoriesPage.findViewById(R.id.no_categories);

		setupTopCategoriesListViewAdapter(topCategoriesPage);
		pages.add(topCategoriesPage);

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
					if (currentPage == 0) {
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
					if (currentPage == 1) {
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
				addProductToList(currentProduct.getString("product"), currentProduct.getString("productid"),
						currentProduct.getString("productcode"), currentProduct.getString("count"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void addProductToList(final String name, final String id, final String code, final String sold) {
		topProductsAdapter.add(new TopProduct(name, id, code, sold));
	}

	private void clearTopSellersTable() {
		topProductsAdapter.clear();
		noProducts.setText("");
	}

	private void setupTopProductsListViewAdapter(View page) {
		topProductsAdapter = new TopProductsListAdapter(this, R.layout.top_products_item, new ArrayList<TopProduct>());
		ListView topProductsListView = (ListView) page.findViewById(R.id.top_products_list);
		LayoutInflater inflater = getLayoutInflater();

		View listHeader = inflater.inflate(R.layout.top_products_header, null, false);
		topProductsListView.addHeaderView(listHeader, null, false);

		topProductsListView.setHeaderDividersEnabled(false);

		topProductsListView.setAdapter(topProductsAdapter);
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
					if (currentPage == 2) {
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
				addCategoryToList(currentCategory.getString("category"), currentCategory.getString("categoryid"),
						currentCategory.getString("count"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void addCategoryToList(final String name, final String id, final String sold) {
		topCategoriesAdapter.add(new Category(name, id, sold));
	}

	private void clearTopCategoriesTable() {
		topCategoriesAdapter.clear();
		noCategories.setText("");
	}

	private void setupTopCategoriesListViewAdapter(View page) {
		topCategoriesAdapter = new CategoriesListViewAdapter(this, R.layout.top_categories_item,
				new ArrayList<Category>());
		ListView topCategoriesListView = (ListView) page.findViewById(R.id.top_categories_list);
		LayoutInflater inflater = getLayoutInflater();

		View listHeader = inflater.inflate(R.layout.top_categories_header, null, false);
		topCategoriesListView.addHeaderView(listHeader, null, false);

		topCategoriesListView.setHeaderDividersEnabled(false);

		topCategoriesListView.setAdapter(topCategoriesAdapter);
	}

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
	private TopProductsListAdapter topProductsAdapter;
	private CategoriesListViewAdapter topCategoriesAdapter;
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