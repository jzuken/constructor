package com.example.adminshop;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class Reviews extends PinSupportNetworkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reviews);
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

	private void deleteReview(String id) {

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
		
		reviewsListView.setAdapter(adapter);
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
	private int packAmount = 10;
	private final int startItemCount = 3;
	PullToRefreshListView reviewsListView;
}
