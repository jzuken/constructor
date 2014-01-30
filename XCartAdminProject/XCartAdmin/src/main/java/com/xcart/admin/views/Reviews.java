package com.xcart.admin.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xcart.admin.R;
import com.xcart.admin.managers.XCartApplication;
import com.xcart.admin.managers.network.HttpManager;
import com.xcart.admin.managers.network.Requester;
import com.xcart.admin.views.adapters.ReviewsListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Reviews extends PinSupportNetworkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviews);
        setupListViewAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reviews, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void withoutPinAction() {
        packAmount = XCartApplication.getInstance().getPreferenceManager().getDownloadListLimit();
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
                        if (length == 0 && currentAmount == 0) {
                            progressBar.setVisibility(View.GONE);
                            noReviews.setVisibility(View.VISIBLE);
                            return;
                        }
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
                        currentAmount += packAmount;
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
    }

    private void addReviewToList(final String id, final String email, final String product, final String message) {
        adapter.add(new com.xcart.admin.model.Review(id, email, product, message));
    }

    private void setupListViewAdapter() {
        adapter = new ReviewsListAdapter(this, R.layout.review_item, new ArrayList<com.xcart.admin.model.Review>());
        reviewsListView = (ListView) findViewById(R.id.reviews_list);

        LayoutInflater inflater = getLayoutInflater();
        View listFooter = inflater.inflate(R.layout.on_demand_footer_with_message, null, false);
        progressBar = (ProgressBar) listFooter.findViewById(R.id.progress_bar);
        noReviews = (TextView) listFooter.findViewById(R.id.no_content_message);
        noReviews.setText(R.string.no_reviews);
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
                com.xcart.admin.model.Review review = ((ReviewsListAdapter.ReviewHolder) view.getTag()).getReview();
                Intent intent = new Intent(getBaseContext(), Review.class);
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
        noReviews.setVisibility(View.GONE);
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
    private TextView noReviews;
}
