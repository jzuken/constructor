package com.example.adminshop;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Users extends PinSupportNetworkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.users_search);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		setupListViewAdapter();
	}

	@Override
	protected void withoutPinAction() {
		updateUsersList();
	}
	
	public void showMoreClick(View v) {
		
	}

	private void updateUsersList() {
		clearData();
		progressBar.setVisibility(View.VISIBLE);
		showMoreButton.setVisibility(View.GONE);
		GetRequester dataRequester = new GetRequester() {
			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONObject responseObj = new JSONObject(result);
						JSONObject userCount = responseObj.getJSONObject("users_count");
						registered.setText(userCount.getString("registered"));
						JSONArray array = responseObj.getJSONArray("users");
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							String name = obj.getString("title") + " " + obj.getString("firstname") + " "
									+ obj.getString("lastname");
							String login = obj.getString("login");
							String type = obj.getString("usertype");
							String lastLogin = getFormatDate(Long.parseLong(obj.getString("last_login")));
							String totalOrder = obj.getString("orders_count");
							addUserToList(name, login, type, lastLogin, totalOrder);
						}
						showMoreButton.setVisibility(View.VISIBLE);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					showConnectionErrorMessage();
				}
				progressBar.setVisibility(View.GONE);
			}
		};

		dataRequester.execute("http://54.213.38.9/xcart/api.php?request=users&from=0&size=10&sort=orders");
	}
	
	private void clearData() {
		adapter.clear();
		registered.setText("");
		online.setText("");
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

	private void addUserToList(final String name, final String login, final String type, final String lastLogin,
			final String totalOrders) {
		adapter.add(new User(name, login, type, lastLogin, totalOrders));
	}

	private void setupListViewAdapter() {
		adapter = new UsersListAdapter(this, R.layout.user_item, new ArrayList<User>());
		ListView usersListView = (ListView) findViewById(R.id.users_list);	
		LayoutInflater inflater = getLayoutInflater();
		
		View listHeader = inflater.inflate(R.layout.users_header, null, false);
		registered = (TextView) listHeader.findViewById(R.id.registered);
		online = (TextView) listHeader.findViewById(R.id.online);
		usersListView.addHeaderView(listHeader);
		
		View listFooter = inflater.inflate(R.layout.users_footer, null, false);
		showMoreButton = (Button) listFooter.findViewById(R.id.show_more_button);
		usersListView.addFooterView(listFooter);
		
		usersListView.setAdapter(adapter);
	}

	private TextView registered;
	private TextView online;
	private ProgressBar progressBar;
	private UsersListAdapter adapter;
	private Button showMoreButton;
}