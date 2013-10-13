package com.xcart.xcartnew;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xcart.xcartnew.managers.network.HttpManager;

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

        final SharedPreferences authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
        final String sid = authorizationData.getString("sid", "");
        final String from = String.valueOf(currentAmount);

		GetRequester dataRequester = new GetRequester() {
            @Override
            protected String doInBackground(Void... params) {
                return new HttpManager(sid).getReviews(from, String.valueOf(packAmount));
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

		setRequester(dataRequester);
		dataRequester.execute();
		currentAmount += packAmount;

	}

	private void addReviewToList(final String id, final String email, final String product, final String message) {
		adapter.add(new Review(id, email, product, message));
	}

	private void deleteClick(final String id) {
		LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.confirmation_dialog, null);
		((TextView) view.findViewById(R.id.confirm_question)).setText("Are you sure you want to delete this review?");
		final CustomDialog dialog = new CustomDialog(this, view);

		ImageButton noButton = (ImageButton) view.findViewById(R.id.dialog_no_button);
		noButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		ImageButton yesButton = (ImageButton) view.findViewById(R.id.dialog_yes_button);
		yesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				deleteReview(id);
			}
		});

		dialog.show();
	}

	private void deleteReview(final String id) {
		try {
            final SharedPreferences authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
            final String sid = authorizationData.getString("sid", "");
			new GetRequester() {
                @Override
                protected String doInBackground(Void... params) {
                    return new HttpManager(sid).deleteReview(id);
                }

                @Override
                protected void onPostExecute(String response) {
                    super.onPostExecute(response);
                    if (response != null) {
                        Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
                        clearList();
                        updateReviewsList();
                    } else {
                        showConnectionErrorMessage();
                    }
                }
            }.execute();
		} catch (Exception e) {
            showConnectionErrorMessage();
		}

	}

	private void showFullMessage(String email, String product, String message) {
		setNeedDownloadValue(false);
		Intent intent = new Intent(getBaseContext(), FullMessage.class);
		intent.putExtra("email", email);
		intent.putExtra("product", product);
		intent.putExtra("message", message);
		startActivityForResult(intent, 1);
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
				showActionDialog(((Review) view.getTag()));
			}
		});

		reviewsListView.setAdapter(adapter);
	}

	private void showActionDialog(final Review review) {
		LinearLayout action_view = (LinearLayout) getLayoutInflater().inflate(R.layout.action_dialog, null);
		final CustomDialog dialog = new CustomDialog(this, action_view);

		ListView actionList = (ListView) action_view.findViewById(R.id.action_list);

		String[] actions = { "Full text", "Remove", "Cancel" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.action_item, R.id.textItem, actions);

		actionList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					dialog.dismiss();
					showFullMessage(review.getEmail(), review.getProduct(), review.getMessage());
					break;
				case 1:
					deleteClick(review.getId());
					dialog.dismiss();
					break;
				case 2:
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
