package com.example.adminshop;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class Users extends PinSupportNetworkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.users_search);
		setupListViewAdapter();
		setupSortSpinner();
	}

	@Override
	protected void withoutPinAction() {
		registered.setText("");
		online.setText("");
		clearData();
		updateUsersList();
	}

	public void showMoreClick(View v) {

	}

	private void updateUsersList() {
		progressBar.setVisibility(View.VISIBLE);
		isDownloading = true;
		hasNext = false;
		GetRequester dataRequester = new GetRequester() {
			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONObject responseObj = new JSONObject(result);
						JSONObject userCount = responseObj.getJSONObject("users_count");
						registered.setText(userCount.getString("registered"));
						JSONArray array = responseObj.getJSONArray("users");
						int length = array.length();
						if (length == packAmount) {
							hasNext = true;
						}
						for (int i = 0; i < length; i++) {
							JSONObject obj = array.getJSONObject(i);
							String id = obj.getString("id");
							String name = obj.getString("title") + " " + obj.getString("firstname") + " "
									+ obj.getString("lastname");
							String login = obj.getString("login");
							String type = obj.getString("usertype");
							String lastLogin = getFormatDate(Long.parseLong(obj.getString("last_login")));
							String totalOrder = obj.optString("orders_count");
							addUserToList(id, name, login, type, lastLogin, totalOrder);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					showConnectionErrorMessage();
				}
				progressBar.setVisibility(View.GONE);
				usersListView.onRefreshComplete();
				isDownloading = false;
			}
		};

		dataRequester.execute("http://54.213.38.9/xcart/api.php?request=users&from=" + String.valueOf(currentAmount)
				+ "&size=" + String.valueOf(packAmount) + "&sort=" + getCurrentSort());
		currentAmount += packAmount;
	}

	private void clearData() {
		adapter.clear();
		currentAmount = 0;
	}

	private String getFormatDate(Long seconds) {
		if (seconds == 0) {
			return "never";
		}
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

	private String getCurrentSort() {
		switch (currentSortOption) {
		case 0:
			return "login_date";
		case 1:
			return "order_date";
		case 2:
			return "orders";
		case 3:
			return "none";
		default:
			return null;
		}
	}

	private void addUserToList(final String id, final String name, final String login, final String type,
			final String lastLogin, final String totalOrders) {
		adapter.add(new User(id, name, login, type, lastLogin, totalOrders));
	}

	private void setupListViewAdapter() {
		adapter = new UsersListAdapter(this, R.layout.user_item, new ArrayList<User>());
		usersListView = (PullToRefreshListView) findViewById(R.id.users_list);
		LayoutInflater inflater = getLayoutInflater();

		View listHeader = inflater.inflate(R.layout.users_header, null, false);
		registered = (TextView) listHeader.findViewById(R.id.registered);
		online = (TextView) listHeader.findViewById(R.id.online);
		usersListView.getRefreshableView().addHeaderView(listHeader, null, false);

		View listFooter = inflater.inflate(R.layout.on_demand_footer, null, false);
		progressBar = (ProgressBar) listFooter.findViewById(R.id.progress_bar);
		usersListView.getRefreshableView().addFooterView(listFooter, null, false);

		usersListView.getRefreshableView().setHeaderDividersEnabled(false);
		usersListView.getRefreshableView().setFooterDividersEnabled(false);

		usersListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (totalItemCount > startItemCount && firstVisibleItem + visibleItemCount == totalItemCount
						&& !isDownloading && hasNext) {
					updateUsersList();
				}
			}
		});

		usersListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				registered.setText("");
				online.setText("");
				clearData();
				updateUsersList();
			}
		});

		usersListView.setAdapter(adapter);
	}

	private void setupSortSpinner() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortOptions);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Spinner spinner = (Spinner) findViewById(R.id.sort_spinner);
		spinner.setAdapter(adapter);
		spinner.setSelection(0);
		currentSortOption = 0;

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				currentSortOption = position;
				if (isFirstSelection) {
					isFirstSelection = false;
				} else {
					clearData();
					updateUsersList();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private boolean isFirstSelection = true;
	private TextView registered;
	private TextView online;
	private ProgressBar progressBar;
	private UsersListAdapter adapter;
	private int currentAmount;
	private boolean isDownloading;
	private final String[] sortOptions = { "Last login", "Last order", "Total orders", "None" };
	private int currentSortOption;
	private boolean hasNext;
	private int packAmount = 10;
	private final int startItemCount = 4;
	PullToRefreshListView usersListView;
}