package com.xcart.xcartnew.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
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

import com.xcart.xcartnew.R;
import com.xcart.xcartnew.managers.StatusConverter;
import com.xcart.xcartnew.managers.network.HttpManager;
import com.xcart.xcartnew.managers.network.Requester;
import com.xcart.xcartnew.model.OrderStatus;
import com.xcart.xcartnew.views.dialogs.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	}

	@Override
	protected void withoutPinAction() {
		if (isNeedDownload()) {
			clearData();
			updateData();
		}
		super.withoutPinAction();
	}

	private void updateData() {
		progressBar.setVisibility(View.VISIBLE);

		statusItem.setClickable(false);
		trackingNumberItem.setClickable(false);
		customerItem.setClickable(false);
		final String orderId = getIntent().getStringExtra("orderId");
		requester = new Requester() {

			@Override
			protected String doInBackground(Void... params) {
				return new HttpManager(getBaseContext()).getOrderInfo(orderId);
			}

			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONObject obj = new JSONObject(result);

						statusSymbol = obj.getString("status");
						status.setText(StatusConverter.getStatusBySymbol(getBaseContext(),
								OrderStatus.valueOf(statusSymbol)));
						status.setTextColor(StatusConverter.getColorResourceBySymbol(getBaseContext(),
								OrderStatus.valueOf(statusSymbol)));
						trackingNumber.setText(obj.getString("tracking"));
						paymentMethod.setText(obj.getString("payment_method"));
						deliveryMethod.setText(obj.getString("shipping"));
						String title = obj.getString("title");
						if (!title.equals("false")) {
							title += " ";
						} else {
							title = "";
						}
						userName = title + obj.getString("firstname") + " " + obj.getString("lastname");
						customer.setText(userName);
						userId = obj.getString("userid");

						String bTitle = obj.getString("b_title");
						if (!bTitle.equals("false")) {
							bTitle += " ";
						} else {
							bTitle = "";
						}
						billingInfo.setText(bTitle + obj.getString("b_firstname") + " " + obj.getString("b_lastname")
								+ "\n" + obj.getString("b_address") + obj.getString("b_city") + ", "
								+ obj.getString("b_state") + " " + obj.getString("b_zipcode") + "\n"
								+ obj.getString("b_country"));
						bPhone.setText(obj.getString("b_phone"));
						bFax.setText(obj.getString("b_fax"));

						String sTitle = obj.getString("s_title");
						if (!sTitle.equals("false")) {
							sTitle += " ";
						} else {
							sTitle = "";
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

		requester.execute();
	}

	private void clearData() {
		status.setText("");
		trackingNumber.setText("");
		paymentMethod.setText("");
		deliveryMethod.setText("");
		customer.setText("");
		billingInfo.setText("");
		bPhone.setText("");
		bFax.setText("");
		shippingInfo.setText("");
		sPhone.setText("");
		sFax.setText("");
		itemsList.clearList();
		subtotal.setText("");
		discount.setText("");
		couponSaving.setText("");
		shippingCost.setText("");
		total.setText("");
	}

	private void setupStatusItem() {
		status = (TextView) findViewById(R.id.status);
		statusItem = (RelativeLayout) findViewById(R.id.status_item);
		statusItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNeedDownloadValue(false);
				Intent intent = new Intent(getBaseContext(), ChangeStatus.class);
				intent.putExtra("status", statusSymbol);
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
				((TextView) view.findViewById(R.id.label)).setText(R.string.change_tracking_number);
				InputFilter filter = new InputFilter() {
					public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
							int dend) {
						for (int i = start; i < end; i++) {
							if (!Character.isLetterOrDigit(source.charAt(i))) {
								return "";
							}
						}
						return null;
					}
				};
				final EditText numberEditor = (EditText) view.findViewById(R.id.value_editor);
				numberEditor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_FILTER
						| InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
				numberEditor.setFilters(new InputFilter[] { filter, new InputFilter.AllCaps(),
						new InputFilter.LengthFilter(16) });
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
						dialog.dismiss();
						if (!newNumber.equals(oldNumber)) {
							setNewTrackingNumber(newNumber);
						}
					}
				});

				dialog.show();
			}
		});
	}

	private void setNewTrackingNumber(final String newNumber) {
		try {
			new Requester() {
				@Override
				protected String doInBackground(Void... params) {
					return new HttpManager(getBaseContext()).changeTrackingNumber(orderIdValue, newNumber);
				}

				@Override
				protected void onPostExecute(String response) {
					super.onPostExecute(response);

					if (response != null) {
						Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
						trackingNumber.setText(newNumber);
					} else {
						showConnectionErrorMessage();
					}
				}
			}.execute();
		} catch (Exception e) {
			showConnectionErrorMessage();
		}
	}

	private void hideKeyboard(EditText edit) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
	}

	private void setupCustomerItem() {
		customer = (TextView) findViewById(R.id.customer);
		customerItem = (RelativeLayout) findViewById(R.id.customer_item);
		customerItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNeedDownloadValue(false);
				Intent intent = new Intent(getBaseContext(), UserInfo.class);
				intent.putExtra("userId", userId);
				intent.putExtra("userName", userName);
				startActivityForResult(intent, 1);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == ChangeStatus.changeStatusResultCode) {
			statusSymbol = data.getStringExtra("status");
			status.setText(StatusConverter.getStatusBySymbol(getBaseContext(), OrderStatus.valueOf(statusSymbol)));
			status.setTextColor(StatusConverter.getColorResourceBySymbol(getBaseContext(),
					OrderStatus.valueOf(statusSymbol)));
			Intent resultIntent = new Intent();
			resultIntent.putExtra("status", statusSymbol);
			setResult(ChangeStatus.changeStatusResultCode, resultIntent);
		}
		super.onActivityResult(requestCode, resultCode, data);
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
	private String statusSymbol;
	private String userId;
	private String userName;
}
