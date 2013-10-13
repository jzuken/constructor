package com.xcart.xcartnew;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xcart.xcartnew.managers.network.HttpManager;

public class OrderInfo extends PinSupportNetworkActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_info);
		orderIdValue = getIntent().getStringExtra("orderId");
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		orderId = (TextView) findViewById(R.id.order_title);
		orderId.setText(getResources().getString(R.string.order_id_number) + getIntent().getStringExtra("orderId"));
		paymentMethod = (TextView) findViewById(R.id.payment_method);
		deliveryMethod = (TextView) findViewById(R.id.delivery_method);
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
		setupCustomerItem();
		setupStatusItem();
		setupTrackingNumberItem();

		authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
		sid = authorizationData.getString("sid", "");
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

		statusItem.setClickable(false);
		trackingNumberItem.setClickable(false);
		customerItem.setClickable(false);
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

						statusItem.setClickable(true);
						trackingNumberItem.setClickable(true);
						customerItem.setClickable(true);
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

	private void setupStatusItem() {
		status = (TextView) findViewById(R.id.status);
		statusItem = (RelativeLayout) findViewById(R.id.status_item);
		statusItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNeedDownloadValue(false);
				Intent intent = new Intent(getBaseContext(), ChangeStatus.class);
				intent.putExtra("orderId", orderIdValue);
				startActivityForResult(intent, 1);
			}
		});
	}

	private void setupTrackingNumberItem() {
		trackingNumber = (TextView) findViewById(R.id.tracking_number);
		trackingNumberItem = (RelativeLayout) findViewById(R.id.tracking_number_item);
		final Context context = this;
		trackingNumberItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				trackingNumberItem.setClickable(false);
				LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.change_value_dialog, null);
				final EditText numberEditor = (EditText) view.findViewById(R.id.value_editor);
				final String oldNumber = trackingNumber.getText().toString();
				numberEditor.setText(oldNumber);
				final CustomDialog dialog = new CustomDialog(context, view) {
					@Override
					public void dismiss() {
						trackingNumberItem.setClickable(true);
						super.dismiss();
					}
				};

				Button saveButton = (Button) view.findViewById(R.id.dialog_save_button);
				saveButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						hideKeyboard(numberEditor);
						String newNumber = numberEditor.getText().toString();
						try {
							Integer number = Integer.parseInt(newNumber);
							dialog.dismiss();
							if (!newNumber.equals(oldNumber)) {
								setNewTrackingNumber(newNumber);
							}
						} catch (Exception e) {
							Toast.makeText(context, "Incorrect input", Toast.LENGTH_SHORT).show();
						}
					}
				});

				dialog.show();
			}
		});
	}

	private void setNewTrackingNumber(String newNumber) {
		String response;
//		try {
//			response = new GetRequester().execute(
//					"https://54.213.38.9/api/api2.php?request=change_tracking&order_id=" + orderIdValue
//							+ "&tracking_number=" + newNumber + "&sid=" + sid).get();
//		} catch (Exception e) {
//			response = null;
//		}
//		if (response != null) {
//			Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
//			trackingNumber.setText(newNumber);
//		} else {
//			showConnectionErrorMessage();
//		}
	}

	private void hideKeyboard(EditText edit) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
	}

	private void setupCustomerItem() {
		customer = (TextView) findViewById(R.id.customer);
		customerItem = (RelativeLayout) findViewById(R.id.customer_item);
	}

	private String orderIdValue = "";
	private ProgressBar progressBar;
	private TextView orderId;
	private TextView status;
	private TextView trackingNumber;
	private TextView paymentMethod;
	private TextView deliveryMethod;
	private TextView customer;
	private TextView shippingInfo;
	private TextView billingInfo;
	private TextView bPhone;
	private TextView bFax;
	private TextView sPhone;
	private TextView sFax;
	private OrderProductsList itemsList;
	private TextView subtotal;
	private TextView discount;
	private TextView couponSaving;
	private TextView shippingCost;
	private TextView total;
	private RelativeLayout statusItem;
	private RelativeLayout trackingNumberItem;
	private RelativeLayout customerItem;
	private SharedPreferences authorizationData;
	private String sid;
}
