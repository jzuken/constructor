package com.xcart.xcartnew.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.xcart.xcartnew.R;
import com.xcart.xcartnew.managers.network.HttpManager;
import com.xcart.xcartnew.managers.network.Requester;
import com.xcart.xcartnew.model.Review;
import com.xcart.xcartnew.views.adapters.ReviewsListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Reviews extends PinSupportNetworkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reviews);
		settingsData = PreferenceManager.getDefaultSharedPreferences(this);
		setupListViewAdapter();
	}

	@Override
	protected void withoutPinAction() {
		packAmount = Integer.parseInt(settingsData.getString("reviews_amount", "10"));
		if (isNeedDownload()) {
			clearList();
			updateReviewsList();
		}
		super.withoutPinAction();
	}

	private void updateReviewsList() {
		progressBar.setVisibility(View.VISIBLE);
		synchronized (lock) {
			isDownloading = true;
		}
		hasNext = false;

		final String from = String.valueOf(currentAmount);

        requester = new Requester() {
			@Override
			protected String doInBackground(Void... params) {
				return new HttpManager(getBaseContext()).getReviews(from, String.valueOf(packAmount));
			}

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
				synchronized (lock) {
					isDownloading = false;
				}
			}
		};

        requester.execute();
		currentAmount += packAmount;

	}

	private void addReviewToList(final String id, final String email, final String product, final String message) {
		adapter.add(new Review(id, email, product, message));
	}

	private void setupListViewAdapter() {
		adapter = new ReviewsListAdapter(this, R.layout.review_item, new ArrayList<Review>());
		reviewsListView = (ListView) findViewById(R.id.reviews_list);

		LayoutInflater inflater = getLayoutInflater();
		View listFooter = inflater.inflate(R.layout.on_demand_footer, null, false);
		progressBar = (ProgressBar) listFooter.findViewById(R.id.progress_bar);
		reviewsListView.addFooterView(listFooter, null, false);

		reviewsListView.setFooterDividersEnabled(false);

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

		reviewsListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				setNeedDownloadValue(false);
				Review review = ((ReviewsListAdapter.ReviewHolder) view.getTag()).getReview();
				Intent intent = new Intent(getBaseContext(), FullMessage.class);
				intent.putExtra("email", review.getEmail());
				intent.putExtra("product", review.getProduct());
				intent.putExtra("message", review.getMessage());
				startActivityForResult(intent, 1);
			}
		});

		reviewsListView.setAdapter(adapter);
	}

	private void clearList() {
		adapter.clear();
		currentAmount = 0;
	}

	private ProgressBar progressBar;
	private ReviewsListAdapter adapter;
	private int currentAmount;
	private boolean isDownloading;
	private boolean hasNext;
	private int packAmount;
	private final int startItemCount = 3;
	private ListView reviewsListView;
	private Object lock = new Object();
	private SharedPreferences settingsData;
}
