package com.xcart.xcartnew;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xcart.xcartnew.managers.network.HttpManager;

public class OrderInfo extends PinSupportNetworkActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_info);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		orderId = (TextView) findViewById(R.id.order_title);
		orderId.setText(getResources().getString(R.string.order_id_number) + getIntent().getStringExtra("orderId"));
		status = (TextView) findViewById(R.id.status);
		trackingNumber = (TextView) findViewById(R.id.tracking_number);
		paymentMethod = (TextView) findViewById(R.id.payment_method);
		deliveryMethod = (TextView) findViewById(R.id.delivery_method);
		customer = (TextView) findViewById(R.id.customer);
		billingInfo = (TextView) findViewById(R.id.billing_info);
		shippingInfo = (TextView) findViewById(R.id.shipping_info);
		bPhone = (TextView) findViewById(R.id.b_phone);
		bFax = (TextView) findViewById(R.id.b_fax);
		sPhone = (TextView) findViewById(R.id.s_phone);
		sFax = (TextView) findViewById(R.id.s_fax);
		itemsList = (OrderProductsList) findViewById(R.id.items_list);
		subtotal = (TextView) findViewById(R.id.subtotal);
		discount = (TextView) findViewById(R.id.discount);
		couponSaving = (TextView) findViewById(R.id.coupon_saving);
		shippingCost = (TextView) findViewById(R.id.shipping_cost);
		total = (TextView) findViewById(R.id.total);

		authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
	}

	@Override
	protected void withoutPinAction() {
		if (isNeedDownload()) {
			clearData();
			updateData();
		}
		super.withoutPinAction();
	}

	private enum StatusSymbols {
		I, Q, P, B, D, F, C
	}

	private void updateData() {
		progressBar.setVisibility(View.VISIBLE);
        final String orderId = getIntent().getStringExtra("orderId");
		GetRequester dataRequester = new GetRequester() {

            @Override
            protected String doInBackground(Void... params) {
                return new HttpManager(authorizationData.getString("sid", "")).getOrderInfo(orderId);
            }

            @Override
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONObject obj = new JSONObject(result);
						status.setText(getStatusBySymbol(StatusSymbols.valueOf(obj.getString("status"))));
						trackingNumber.setText(obj.getString("tracking"));
						paymentMethod.setText(obj.getString("payment_method"));
						deliveryMethod.setText(obj.getString("shipping"));
						String title = obj.getString("title");
						if (!title.equals("")) {
							title += " ";
						}
						customer.setText(title + obj.getString("firstname") + " " + obj.getString("lastname"));

						String bTitle = obj.getString("b_title");
						if (!bTitle.equals("")) {
							bTitle += " ";
						}
						billingInfo.setText(bTitle + obj.getString("b_firstname") + " " + obj.getString("b_lastname")
								+ "\n" + obj.getString("b_address") + obj.getString("b_city") + ", "
								+ obj.getString("b_state") + " " + obj.getString("b_zipcode") + "\n"
								+ obj.getString("b_country"));
						bPhone.setText(obj.getString("b_phone"));
						bFax.setText(obj.getString("b_fax"));

						String sTitle = obj.getString("s_title");
						if (!sTitle.equals("")) {
							sTitle += " ";
						}
						shippingInfo.setText(sTitle + obj.getString("s_firstname") + " " + obj.getString("s_lastname")
								+ "\n" + obj.getString("s_address") + obj.getString("s_city") + ", "
								+ obj.getString("s_state") + " " + obj.getString("s_zipcode") + "\n"
								+ obj.getString("s_country"));
						sPhone.setText(obj.getString("s_phone"));
						sFax.setText(obj.getString("s_fax"));
						subtotal.setText("$" + obj.getString("subtotal"));
						discount.setText("$" + obj.getString("discount"));
						couponSaving.setText("$" + obj.getString("coupon_discount"));
						shippingCost.setText("$" + obj.getString("shipping_cost"));
						total.setText("$" + obj.getString("total"));
						JSONArray products = obj.getJSONArray("details");
						for (int i = 0; i < products.length(); i++) {
							JSONObject detObj = products.getJSONObject(i);
							String id = detObj.getString("productid");
							String name = detObj.getString("product");
							String price = detObj.getString("price");
							itemsList.addItem(id, name, price);
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

		setRequester(dataRequester);
		dataRequester.execute();
	}

	private String getStatusBySymbol(StatusSymbols symbol) {
		switch (symbol) {
		case I:
			return "Not finished";
		case Q:
			return "Queued";
		case P:
			return "Processed";
		case B:
			return "Backordered";
		case D:
			return "Declined";
		case F:
			return "Failed";
		case C:
			return "Complete";
		default:
			return "";
		}
	}

	private void clearData() {
		status.setText("");
		trackingNumber.setText("");
		paymentMethod.setText("");
		deliveryMethod.setText("");
		customer.setText("");
	}

	ProgressBar progressBar;
	TextView orderId;
	TextView status;
	TextView trackingNumber;
	TextView paymentMethod;
	TextView deliveryMethod;
	TextView customer;
	TextView shippingInfo;
	TextView billingInfo;
	TextView bPhone;
	TextView bFax;
	TextView sPhone;
	TextView sFax;
	OrderProductsList itemsList;
	TextView subtotal;
	TextView discount;
	TextView couponSaving;
	TextView shippingCost;
	TextView total;
	private SharedPreferences authorizationData;
}
