package com.example.adminshop;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class DiscountEditor extends PinSupportActivity {
	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discount_edit);
		allButton = (RadioButton) findViewById(R.id.allRadioButton);
		premiumButton = (RadioButton) findViewById(R.id.premiumRadioButton);
		wholesaleButton = (RadioButton) findViewById(R.id.wholesaleRadioButton);
		
		String membership = getIntent().getStringExtra("membership");
		if (membership.equals("All")) {
			allButton.setChecked(true);
		} else if (membership.equals("Premium")) {
			premiumButton.setChecked(true);
		} else {
			wholesaleButton.setChecked(true);
		}
		
		percentButton = (RadioButton) findViewById(R.id.percentRadioButton);
		absoluteButton  = (RadioButton) findViewById(R.id.absoluteRadioButton);
		
		if (getIntent().getStringExtra("discountType").equals("percent")) {
			percentButton.setChecked(true);
		} else {
			absoluteButton.setChecked(true);
		}
		
		orderSubtotalEditor = (EditText) findViewById(R.id.orderSubtotalEditor);
		orderSubtotalEditor.setText(getIntent().getCharSequenceExtra("orderSub"));
		discountEditor = (EditText) findViewById(R.id.discountEditor);
		discountEditor.setText(getIntent().getCharSequenceExtra("discount"));
	}

	public void okClick(View v) {
		if (Float.valueOf(discountEditor.getText().toString()) > 100.0 && percentButton.isChecked()) {
			Toast.makeText(getBaseContext(), "New discount can not be added with discount value more than 100%",
					Toast.LENGTH_LONG).show();
		} else if (Float.valueOf(discountEditor.getText().toString()) == 0f) {
			Toast.makeText(getBaseContext(), "New discount can not be added with empty discount value",
					Toast.LENGTH_LONG).show();
		} else {
			updateDiscount();
		}
	}

	private void updateDiscount() {
		String provider = "1";
		String id = getIntent().getStringExtra("id");
		String orderSubtotalValue = orderSubtotalEditor.getText().toString();
		String discountValue = discountEditor.getText().toString();
		String discountTypeValue = getDiscountType();
		String response;
		try {
			response = new GetRequester().execute(
					"http://54.213.38.9/xcart/api.php?request=update_discount&id=" + id + "&minprice="
							+ orderSubtotalValue + "&discount=" + discountValue + "&discount_type=" + discountTypeValue
							+ "&provider=" + provider).get();
		} catch (Exception e) {
			response = null;
		}
		if (response != null) {
			Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
			onBackPressed();
		} else {
			showConnectionErrorMessage();
		}
	}

	private String getDiscountType() {
		if (percentButton.isChecked()) {
			return "percent";
		} else {
			return "absolute";
		}
	}

	private void showConnectionErrorMessage() {
		Toast.makeText(getBaseContext(),
				"Sorry, unable to connect to server. Cannot update data. Please check your internet connection",
				Toast.LENGTH_LONG).show();
	}

	private RadioButton allButton;
	private RadioButton premiumButton;
	private RadioButton wholesaleButton;
	private RadioButton percentButton;
	private RadioButton absoluteButton;
	private EditText orderSubtotalEditor;
	private EditText discountEditor;
}
