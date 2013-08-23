package com.example.adminshop;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class MainActivity extends PinSupportNetworkActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);setContentView(R.layout.main);setContentView(R.layout.main);

		LayoutInflater inflater = LayoutInflater.from(this);
		pages = new ArrayList<View>();

		menuPage = inflater.inflate(R.layout.menu, null);

		newsPage = inflater.inflate(R.layout.latest_info, null);
		date = (TextView) newsPage.findViewById(R.id.last_order_date);
		product = (TextView) newsPage.findViewById(R.id.last_order_product);
		product.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (details != null) {
					Intent intent = new Intent(getBaseContext(), OrderProducts.class);
					intent.putExtra("details", details);
					startActivityForResult(intent, 1);
				}
			}
		});

		totalPrice = (TextView) newsPage.findViewById(R.id.last_order_price);
		user = (TextView) newsPage.findViewById(R.id.last_order_user);
		status = (TextView) newsPage.findViewById(R.id.last_order_status);
		totalOrders = (TextView) newsPage.findViewById(R.id.total_orders);
		completeOrders = (TextView) newsPage.findViewById(R.id.complete_orders);
		notFinishedOrders = (TextView) newsPage.findViewById(R.id.not_finished_orders);
		queuedOrders = (TextView) newsPage.findViewById(R.id.queued_orders);
		processedOrders = (TextView) newsPage.findViewById(R.id.processed_orders);
		backorderedOrders = (TextView) newsPage.findViewById(R.id.backordered_orders);
		declinedOrders = (TextView) newsPage.findViewById(R.id.declined_orders);
		failedOrders = (TextView) newsPage.findViewById(R.id.failed_orders);
		totalPaid = (TextView) newsPage.findViewById(R.id.total_paid);
		grossTotal = (TextView) newsPage.findViewById(R.id.gross_total);
		progressBar = (ProgressBar) newsPage.findViewById(R.id.progress_bar);

		pullToRefreshView = (PullToRefreshScrollView) newsPage.findViewById(R.id.news_scroll_view);
		pullToRefreshView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				clearNewsData();
				updateLastOrderData();
				updateOrdersInfoData();
			}
		});

		SharedPreferences settingsData = PreferenceManager.getDefaultSharedPreferences(this);

		if (savedInstanceState == null) {
			firstPage = Integer.parseInt(settingsData.getString("screens_list", "0"));
		} else {
			firstPage = savedInstanceState.getInt("firstPage");
		}

		settingButton = (Button) menuPage.findViewById(R.id.settingsButton);
		menuArrow = (TextView) menuPage.findViewById(R.id.menu_arrow);
		TextView newsArrow = (TextView) newsPage.findViewById(R.id.news_arrow);

		if (firstPage == 0) {
			pages.add(menuPage);
			pages.add(newsPage);
			newsPageNumber = 1;

			viewToLeft(settingButton);
			menuArrow.setText(">>");
			menuArrow.setPadding(0, 0, 10, 0);
			viewToRight(menuArrow);

			newsArrow.setText("<<");
			newsArrow.setPadding(10, 0, 0, 0);
			viewToLeft(newsArrow);
		} else {
			pages.add(newsPage);
			pages.add(menuPage);
			newsPageNumber = 0;

			viewToRight(settingButton);
			menuArrow.setText("<<");
			menuArrow.setPadding(10, 0, 0, 0);
			viewToLeft(menuArrow);

			newsArrow.setText(">>");
			newsArrow.setPadding(0, 0, 10, 0);
			viewToRight(newsArrow);
		}

		pagerAdapter = new SwipingPagerAdapter(pages);
		viewPager = (ViewPager) findViewById(R.id.start_view_pager);
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		pages = new ArrayList<View>();

		LayoutInflater inflater = LayoutInflater.from(this);
		menuPage = inflater.inflate(R.layout.menu, null);

		settingButton = (Button) menuPage.findViewById(R.id.settingsButton);
		menuArrow = (TextView) menuPage.findViewById(R.id.menu_arrow);

		if (firstPage == 0) {
			pages.add(menuPage);
			pages.add(newsPage);
			newsPageNumber = 1;

			viewToLeft(settingButton);
			menuArrow.setText(">>");
			menuArrow.setPadding(0, 0, 10, 0);
			viewToRight(menuArrow);
		} else {
			pages.add(newsPage);
			pages.add(menuPage);
			newsPageNumber = 0;

			viewToRight(settingButton);
			menuArrow.setText("<<");
			menuArrow.setPadding(10, 0, 0, 0);
			viewToLeft(menuArrow);
		}

		pagerAdapter = new SwipingPagerAdapter(pages);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(currentPage);
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("firstPage", firstPage);
	}

	private void viewToRight(View view) {
		RelativeLayout.LayoutParams viewParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
		viewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		view.setLayoutParams(viewParams);
	}

	private void viewToLeft(View view) {
		RelativeLayout.LayoutParams viewParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
		viewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		view.setLayoutParams(viewParams);
	}

	@Override
	protected void withoutPinAction() {
		if (isNeedDownload()) {
			clearNewsData();
			updateLastOrderData();
			updateOrdersInfoData();
		}
		super.withoutPinAction();
	}

	private enum StatusSymbols {
		I, Q, P, X, D, F, C
	}

	private void updateLastOrderData() {
		clearLastOrderInfo();
		progressBar.setVisibility(View.VISIBLE);
		synchronized (lock) {
			isDownloading++;
		}
		GetRequester dataRequester = new GetRequester() {
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONObject obj = new JSONObject(result);
						date.setText(obj.getString("date"));

						totalPrice.setText("$" + obj.getString("total"));
						user.setText(obj.getString("title") + " " + obj.getString("firstname") + " "
								+ obj.getString("lastname"));
						StatusSymbols statusSymbol = StatusSymbols.valueOf(obj.getString("status"));
						status.setText(getStatusBySymbol(statusSymbol));

						JSONArray detailsArray = obj.getJSONArray("details");
						int productsCount = detailsArray.length();
						details = obj.getJSONArray("details").toString();
						if (productsCount == 1) {
							product.setText("1 item >");
						} else {
							product.setText(String.valueOf(productsCount) + " items >");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					if (currentPage == newsPageNumber) {
						showConnectionErrorMessage();
					}
				}
				synchronized (lock) {
					isDownloading--;
				}
				if (isDownloading == 0) {
					progressBar.setVisibility(View.GONE);
					pullToRefreshView.onRefreshComplete();
				}
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
		date.setText("");
		product.setText("");
		totalPrice.setText("");
		user.setText("");
		status.setText("");
		details = null;
	}

	private void updateOrdersInfoData() {
		clearOrdersInfo();
		progressBar.setVisibility(View.VISIBLE);
		synchronized (lock) {
			isDownloading++;
		}
		GetRequester dataRequester = new GetRequester() {
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONObject obj = new JSONObject(result);
						JSONObject currentPeriodObj = obj.getJSONObject("today");
						completeOrders.setText(currentPeriodObj.getString("C"));
						notFinishedOrders.setText(currentPeriodObj.getString("I"));
						queuedOrders.setText(currentPeriodObj.getString("Q"));
						processedOrders.setText(currentPeriodObj.getString("P"));
						backorderedOrders.setText(currentPeriodObj.getString("X"));
						declinedOrders.setText(currentPeriodObj.getString("D"));
						failedOrders.setText(currentPeriodObj.getString("F"));
						totalOrders.setText(currentPeriodObj.getString("Total"));
						totalPaid.setText(currentPeriodObj.getString("total_paid"));
						grossTotal.setText(currentPeriodObj.getString("gross_total"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					if (currentPage == newsPageNumber) {
						showConnectionErrorMessage();
					}
				}
				synchronized (lock) {
					isDownloading--;
				}
				if (isDownloading == 0) {
					progressBar.setVisibility(View.GONE);
					pullToRefreshView.onRefreshComplete();
				}
			}
		};

		dataRequester.execute("http://54.213.38.9/xcart/api.php?request=orders_statistic");
	}

	private void clearOrdersInfo() {
		completeOrders.setText("");
		notFinishedOrders.setText("");
		queuedOrders.setText("");
		processedOrders.setText("");
		backorderedOrders.setText("");
		declinedOrders.setText("");
		failedOrders.setText("");
		totalOrders.setText("");
		totalPaid.setText("");
		grossTotal.setText("");
	}

	private void clearNewsData() {
		clearLastOrderInfo();
		clearOrdersInfo();
	}

	public void productsButtonClick(View v) {
		Intent intent = new Intent(this, Products.class);
		startActivityForResult(intent, 1);
	}

	public void discountsButtonClick(View v) {
		Intent intent = new Intent(this, Discounts.class);
		startActivityForResult(intent, 1);
	}

	public void dashboardButtonClick(View v) {
		Intent intent = new Intent(this, Dashboard.class);
		startActivityForResult(intent, 1);
	}

	public void logoutClick(View v) {
		SharedPreferences authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
		Editor editor = authorizationData.edit();
		editor.remove("logged");
		editor.commit();
		Intent intent = new Intent(this, Authorization.class);
		startActivity(intent);
		finish();
	}

	public void settingsClick(View v) {
		Intent intent = new Intent(this, Settings.class);
		startActivityForResult(intent, 1);
	}

	public void usersButtonClick(View v) {
		Intent intent = new Intent(this, Users.class);
		startActivityForResult(intent, 1);
	}

	public void reviewsButtonClick(View v) {
		Intent intent = new Intent(this, Reviews.class);
		startActivityForResult(intent, 1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivityForResult(new Intent(this, Settings.class), 1);
			return true;
		}
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == noUpdateCode) {
			setNeedDownloadValue(false);
		}
	}

	private int currentPage;
	private int newsPageNumber;
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
	private TextView grossTotal;
	private ProgressBar progressBar;
	private int isDownloading;
	private Object lock = new Object();
	private PullToRefreshScrollView pullToRefreshView;
	private int firstPage;
	private View menuPage;
	private View newsPage;
	private ViewPager viewPager;
	private Button settingButton;
	private TextView menuArrow;
	private SwipingPagerAdapter pagerAdapter;
	private List<View> pages;
	private String details = null;
	private final int noUpdateCode = 4;
}
