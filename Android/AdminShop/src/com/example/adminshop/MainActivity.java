package com.example.adminshop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
		setContentView(R.layout.main);

		LayoutInflater inflater = LayoutInflater.from(this);
		List<View> pages = new ArrayList<View>();

		View menuPage = inflater.inflate(R.layout.menu, null);

		View newsPage = inflater.inflate(R.layout.latest_info, null);
		date = (TextView) newsPage.findViewById(R.id.last_order_date);
		product = (TextView) newsPage.findViewById(R.id.last_order_product);
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
			date.setText(savedInstanceState.getCharSequence("date"));
			product.setText(savedInstanceState.getCharSequence("product"));
			totalPrice.setText(savedInstanceState.getCharSequence("totalPrice"));
			user.setText(savedInstanceState.getCharSequence("user"));
			status.setText(savedInstanceState.getCharSequence("status"));
			totalOrders.setText(savedInstanceState.getCharSequence("totalOrders"));
			completeOrders.setText(savedInstanceState.getCharSequence("completeOrders"));
			notFinishedOrders.setText(savedInstanceState.getCharSequence("notFinishedOrders"));
			queuedOrders.setText(savedInstanceState.getCharSequence("queuedOrders"));
			processedOrders.setText(savedInstanceState.getCharSequence("processedOrders"));
			backorderedOrders.setText(savedInstanceState.getCharSequence("backorderedOrders"));
			declinedOrders.setText(savedInstanceState.getCharSequence("declinedOrders"));
			failedOrders.setText(savedInstanceState.getCharSequence("failedOrders"));
			totalPaid.setText(savedInstanceState.getCharSequence("totalPaid"));
			grossTotal.setText(savedInstanceState.getCharSequence("grossTotal"));
		}

		Button settingButton = (Button) menuPage.findViewById(R.id.settingsButton);
		TextView menuArrow = (TextView) menuPage.findViewById(R.id.menu_arrow);
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

		SwipingPagerAdapter pagerAdapter = new SwipingPagerAdapter(pages);
		ViewPager viewPager = (ViewPager) findViewById(R.id.start_view_pager);
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

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("firstPage", firstPage);
		outState.putCharSequence("date", date.getText());
		outState.putCharSequence("product", product.getText());
		outState.putCharSequence("totalPrice", totalPrice.getText());
		outState.putCharSequence("user", user.getText());
		outState.putCharSequence("status", status.getText());
		outState.putCharSequence("totalOrders", totalOrders.getText());
		outState.putCharSequence("completeOrders", completeOrders.getText());
		outState.putCharSequence("notFinishedOrders", notFinishedOrders.getText());
		outState.putCharSequence("queuedOrders", queuedOrders.getText());
		outState.putCharSequence("processedOrders", processedOrders.getText());
		outState.putCharSequence("backorderedOrders", backorderedOrders.getText());
		outState.putCharSequence("declinedOrders", declinedOrders.getText());
		outState.putCharSequence("failedOrders", failedOrders.getText());
		outState.putCharSequence("totalPaid", totalPaid.getText());
		outState.putCharSequence("grossTotal", grossTotal.getText());
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
						date.setText(getFormatDate(Long.parseLong(obj.getString("date"))));
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
}
