package com.example.adminshop;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Discounts extends PinSupportNetworkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discounts_list);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		setupListViewAdapter();
	}

	@Override
	protected void withoutPinAction() {
		updateDiscountsTable();
	}

	private void updateDiscountsTable() {
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
							String id = obj.getString("discountid");
							String orderSubtotalString = obj.getString("minprice");
							String discountString = obj.getString("discount");
							String discountTypeString = obj.getString("discount_type");
							addDiscountToList(id, orderSubtotalString, discountString, discountTypeString, "All");
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

		dataRequester.execute("http://54.213.38.9/xcart/api.php?request=discounts");
	}

	private void addDiscountToList(final String id, final String subtotal, final String discount,
			final String discountType, final String membership) {
		adapter.add(new Discount(id, "Position" + String.valueOf(position), subtotal, discount, discountType,
				membership));
		position++;
	}

	public void editClick(View v) {
		Discount itemToEdit = (Discount) v.getTag();
		Intent intent = new Intent(getBaseContext(), DiscountEditor.class);
		intent.putExtra("id", itemToEdit.getId());
		intent.putExtra("orderSub", itemToEdit.getOrderSubtotal());
		intent.putExtra("discount", itemToEdit.getDiscount());
		intent.putExtra("discountType", itemToEdit.getDiscountType());
		intent.putExtra("membership", itemToEdit.getMembership());
		startActivityForResult(intent, 1);
	}

	public void deleteClick(View v) {
		Discount itemToRemove = (Discount) v.getTag();
		String response;
		try {
			response = new GetRequester().execute(
					"http://54.213.38.9/xcart/api.php?request=delete_discount&id=" + itemToRemove.getId()).get();
		} catch (Exception e) {
			response = null;
		}
		if (response != null) {
			Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
			updateDiscountsTable();
		} else {
			showConnectionErrorMessage();
		}
	}

	private void setupListViewAdapter() {
		adapter = new DiscountsListAdapter(this, R.layout.discount_item, new ArrayList<Discount>());
		ListView discountsListView = (ListView) findViewById(R.id.discounts);
		discountsListView.setAdapter(adapter);
	}

	private void clearList() {
		adapter.clear();
		position = 1;
	}

	public void addNewDiscountClick(View v) {
		Intent intent = new Intent(this, DiscountAdder.class);
		startActivityForResult(intent, 1);
	}

	private int position = 1;
	private ProgressBar progressBar;
	private DiscountsListAdapter adapter;
}
