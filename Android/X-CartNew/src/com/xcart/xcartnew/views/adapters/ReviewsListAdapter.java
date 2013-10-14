package com.xcart.xcartnew.views.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xcart.xcartnew.R;
import com.xcart.xcartnew.model.Review;

public class ReviewsListAdapter extends ArrayAdapter<Review> {

	public ReviewsListAdapter(Context context, int resource, List<Review> items) {
		super(context, resource, items);
		this.context = context;
		this.layoutResourceId = resource;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ReviewHolder holder = null;

		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);

		holder = new ReviewHolder();
		holder.review = items.get(position);
		holder.email = (TextView) row.findViewById(R.id.review_email);
		holder.product = (TextView) row.findViewById(R.id.review_product);
		holder.message = (TextView) row.findViewById(R.id.review_message);
		row.setTag(holder.review);
		setupItem(holder);
		return row;
	}

	private void setupItem(ReviewHolder holder) {
		holder.email.setText(holder.review.getEmail());
		holder.product.setText(holder.review.getProduct());
		holder.message.setText(holder.review.getMessage());
	}

	private static class ReviewHolder {
		Review review;
		TextView email;
		TextView product;
		TextView message;
	}

	private List<Review> items;
	private int layoutResourceId;
	private Context context;
}
