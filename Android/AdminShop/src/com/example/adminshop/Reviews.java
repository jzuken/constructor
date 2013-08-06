package com.example.adminshop;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

public class Reviews extends PinSupportNetworkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reviews);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		setupListViewAdapter();
	}

	@Override
	protected void withoutPinAction() {
		updateReviewsList();
	}

	private void updateReviewsList() {
		clearList();
		progressBar.setVisibility(View.VISIBLE);
		GetRequester dataRequester = new GetRequester() {
			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONArray array = new JSONArray(result);
						for (int i = 0; i < array.length(); i++) {
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
			}
		};

		dataRequester.execute("http://54.213.38.9/xcart/api.php?request=reviews");
	}

	private void addReviewToList(final String id, final String email, final String product, final String message) {
		adapter.add(new Review(id, "Review " + String.valueOf(position), email, product, message));
		position++;
	}
	
	private void deleteReview(String id) {
	
	}
	
	private void setupListViewAdapter() {
		adapter = new ReviewsListAdapter(this, R.layout.review_item, new ArrayList<Review>());
		ListView reviewsListView = (ListView) findViewById(R.id.reviews_list);
		reviewsListView.setAdapter(adapter);
	}

	private void clearList() {
		adapter.clear();
		position = 1;
	}

	private int position = 1;
	private ProgressBar progressBar;
	private ReviewsListAdapter adapter;
}
