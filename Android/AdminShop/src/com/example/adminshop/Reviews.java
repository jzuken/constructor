package com.example.adminshop;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class Reviews extends PinSupportNetworkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reviews);
		SharedPreferences settingsData = PreferenceManager.getDefaultSharedPreferences(this);
		packAmount = Integer.parseInt(settingsData.getString("reviews_amount", "10"));
		setupListViewAdapter();
		
	}

	@Override
	protected void withoutPinAction() {
		clearList();
		updateReviewsList();
	}

	private void updateReviewsList() {
		progressBar.setVisibility(View.VISIBLE);
		isDownloading = true;
		hasNext = false;
		GetRequester dataRequester = new GetRequester() {
			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONArray array = new JSONArray(result);
						int length = array.length();
						if (length == packAmount) {
							hasNext = true;
						}
						for (int i = 0; i < length; i++) {
							JSONObject obj = array.getJSONObject(i);
							String id = obj.getString("review_id");
							String email = obj.getString("email");
							String product = obj.getString("product");
							String message = obj.getString("message");
							addReviewToList(id, email, product, message);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					showConnectionErrorMessage();
				}
				progressBar.setVisibility(View.GONE);
				reviewsListView.onRefreshComplete();
				isDownloading = false;
			}
		};

		dataRequester.execute("http://54.213.38.9/xcart/api.php?request=reviews&from=" + String.valueOf(currentAmount)
				+ "&size=" + String.valueOf(packAmount));
		currentAmount += packAmount;

	}

	private void addReviewToList(final String id, final String email, final String product, final String message) {
		adapter.add(new Review(id, "Review " + String.valueOf(position), email, product, message));
		position++;
	}
	
	public void deleteClick(final String id) {
		LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.confirmation_dialog, null);
		((TextView) view.findViewById(R.id.confirm_question)).setText("Are you sure you want to delete this review?");
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
				deleteReview(id);
			}
		});

		dialog.show();
	}

	private void deleteReview(String id) {
		String response;
		try {
			response = new GetRequester().execute("http://54.213.38.9/xcart/api.php?request=delete_review&id=" + id)
					.get();
		} catch (Exception e) {
			response = null;
		}
		if (response != null) {
			Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
			clearList();
			updateReviewsList();
		} else {
			showConnectionErrorMessage();
		}
	}

	private void setupListViewAdapter() {
		adapter = new ReviewsListAdapter(this, R.layout.review_item, new ArrayList<Review>());
		reviewsListView = (PullToRefreshListView) findViewById(R.id.reviews_list);

		LayoutInflater inflater = getLayoutInflater();
		View listFooter = inflater.inflate(R.layout.on_demand_footer, null, false);
		progressBar = (ProgressBar) listFooter.findViewById(R.id.progress_bar);
		reviewsListView.getRefreshableView().addFooterView(listFooter, null, false);

		reviewsListView.getRefreshableView().setFooterDividersEnabled(false);

		reviewsListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (totalItemCount > startItemCount && firstVisibleItem + visibleItemCount == totalItemCount
						&& !isDownloading && hasNext) {
					updateReviewsList();
				}
			}
		});

		reviewsListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				clearList();
				updateReviewsList();
			}
		});

		reviewsListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showActionDialog(((Review) view.getTag()).getId());
			}
		});

		reviewsListView.setAdapter(adapter);
	}

	private void showActionDialog(final String review_id) {
		LinearLayout action_view = (LinearLayout) getLayoutInflater().inflate(R.layout.action_dialog, null);
		final CustomDialog dialog = new CustomDialog(this, action_view);

		ListView actionList = (ListView) action_view.findViewById(R.id.action_list);

		String[] actions = { "Delete", "Cancel"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.action_item, R.id.textItem, actions);

		actionList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					deleteClick(review_id);
					dialog.dismiss();
					break;
				case 1:
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

	private void clearList() {
		adapter.clear();
		position = 1;
		currentAmount = 0;
	}

	private int position = 1;
	private ProgressBar progressBar;
	private ReviewsListAdapter adapter;
	private int currentAmount;
	private boolean isDownloading;
	private boolean hasNext;
	private int packAmount;
	private final int startItemCount = 3;
	PullToRefreshListView reviewsListView;
}
