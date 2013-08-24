package com.example.adminshop;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.ListView;

public class OrderProducts extends PinSupportActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_products);
		setupListViewAdapter();
		addProductsToList();
	}
	
	@Override
	public void onBackPressed() {
		setResult(noUpdateCode);
		super.onBackPressed();
	}

	private void addProductsToList() {
		try {
			JSONArray details = new JSONArray(getIntent().getStringExtra("details"));
			for (int i = 0; i < details.length(); i++) {
				JSONObject detailsObj = details.getJSONObject(i);
				String name = detailsObj.getString("product");
				String quantity = detailsObj.getString("amount");
				String price = detailsObj.getString("price");
				adapter.add(new OrderProduct(name, quantity, price));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void setupListViewAdapter() {
		adapter = new OrderProductsListAdapter(this, R.layout.order_product_item, new ArrayList<OrderProduct>());
		ListView orderProductsListView = (ListView) findViewById(R.id.order_products_list);
		orderProductsListView.setAdapter(adapter);
	}

	private OrderProductsListAdapter adapter;
	private final int noUpdateCode = 4;
}
