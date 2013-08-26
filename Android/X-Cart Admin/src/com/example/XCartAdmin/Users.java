package com.example.XCartAdmin;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
		SharedPreferences settingsData = PreferenceManager.getDefaultSharedPreferences(this);
		packAmount = Integer.parseInt(settingsData.getString("users_amount", "10"));
		setupListViewAdapter();
		setupSortSpinner();
	}

	@Override
	protected void withoutPinAction() {
		if (isNeedDownload()) {
			registered.setText("");
			online.setText("");
			clearData();
			updateUsersList();
		}
		super.withoutPinAction();
	}

	private void updateUsersList() {
		progressBar.setVisibility(View.VISIBLE);
		synchronized (lock) {
			isDownloading = true;
		}
		hasNext = false;
		GetRequester dataRequester = new GetRequester() {
			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONObject responseObj = new JSONObject(result);
						JSONObject userCount = responseObj.getJSONObject("users_count");
						registered.setText(userCount.getString("registered"));
						online.setText(userCount.getString("online"));
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
							
							String type;
							String typeSymbol = obj.getString("usertype");
							if (typeSymbol.equals("C")) {
								type = "Customer";
							} else if (typeSymbol.equals("P")) {
								type = "Administrator";
							} else {
								type = "Partner";
							}
							
							String lastLogin = obj.getString("last_login");
							if (lastLogin.equals("01-01-1970")) {
								lastLogin = "Never logged in";
							}
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
				synchronized (lock) {
					isDownloading = false;
				}
			}
		};

		SharedPreferences authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
		String sid = authorizationData.getString("sid", "");
		dataRequester.execute("http://54.213.38.9/xcart/api.php?request=users&from=" + String.valueOf(currentAmount)
				+ "&size=" + String.valueOf(packAmount) + "&sort=" + getCurrentSort() + "&sid=" + sid);
		currentAmount += packAmount;
	}

	private void clearData() {
		adapter.clear();
		currentAmount = 0;
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

		usersListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showActionDialog(((User) view.getTag()));
			}
		});

		usersListView.setAdapter(adapter);
	}

	private void showOrders(String id, String name) {
		setNeedDownloadValue(false);
		Intent intent = new Intent(getBaseContext(), UserOrders.class);
		intent.putExtra("userId", id);
		intent.putExtra("userName", name);
		startActivityForResult(intent, 1);
	}

	private void showActionDialog(final User user) {
		LinearLayout action_view = (LinearLayout) getLayoutInflater().inflate(R.layout.action_dialog, null);
		final CustomDialog dialog = new CustomDialog(this, action_view);

		ListView actionList = (ListView) action_view.findViewById(R.id.action_list);

		String[] actions = { "Orders list", "Send message", "Ban user", "Cancel" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.action_item, R.id.textItem, actions);

		actionList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					dialog.dismiss();
					showOrders(user.getId(), user.getName());
					break;
				case 1:
					sendMessage(user.getLogin());
					dialog.dismiss();
					break;
				case 2:
					banClick(user.getId());
					dialog.dismiss();
					break;
				case 3:
					dialog.dismiss();
					break;
				default:
					break;
				}

			}
		});

		actionList.setAdapter(adapter);

		dialog.show();
	}

	private void sendMessage(String recipientEmail) {
		setNeedDownloadValue(false);
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", recipientEmail, null));
		startActivityForResult(Intent.createChooser(emailIntent, "Send message..."), 3);
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

	public void banClick(final String id) {
		LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.confirmation_dialog, null);
		((TextView) view.findViewById(R.id.confirm_question)).setText("Are you sure you want to ban this user?");
		final CustomDialog dialog = new CustomDialog(this, view);

		Button noButton = (Button) view.findViewById(R.id.dialog_no_button);
		noButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		Button yesButton = (Button) view.findViewById(R.id.dialog_yes_button);
		yesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				banUser(id);
			}
		});

		dialog.show();
	}

	private void banUser(String id) {

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
	private int packAmount;
	private final int startItemCount = 4;
	private PullToRefreshListView usersListView;
	private Object lock = new Object();
}