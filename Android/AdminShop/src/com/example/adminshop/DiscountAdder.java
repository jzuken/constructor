package com.example.adminshop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class DiscountAdder extends PinSupportNetworkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discount_edit);
		allButton = (RadioButton) findViewById(R.id.allRadioButton);
		allButton.setChecked(true);
		premiumButton = (RadioButton) findViewById(R.id.premiumRadioButton);
		percentButton = (RadioButton) findViewById(R.id.percentRadioButton);
		percentButton.setChecked(true);
		orderSubtotalEditor = (EditText) findViewById(R.id.orderSubtotalEditor);
		discountEditor = (EditText) findViewById(R.id.discountEditor);
		Button okButton = (Button) findViewById(R.id.okButton);
		okButton.setText("Add");
		TextView smallTitle = (TextView) findViewById(R.id.small_title);
		smallTitle.setText("Add new discount");
	}
	
	private void createNewDiscount() {
		String provider = "1";
		String orderSubtotalValue = orderSubtotalEditor.getText().toString();
		String discountValue = discountEditor.getText().toString();
		String discountTypeValue = getDiscountType();
		String membershipIdParametr = "";
		String memvershipId = getMembershipId();
		if (!memvershipId.equals("0")) {
			membershipIdParametr = "&membership_id=" + memvershipId;
		}
		String response;
		try {
			response = new GetRequester().execute(
					"http://54.213.38.9/xcart/api.php?request=create_discount&minprice=" + orderSubtotalValue
							+ "&discount=" + discountValue + "&discount_type=" + discountTypeValue + "&provider="
							+ provider + membershipIdParametr).get();
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
	
	public void okClick(View v) {
		if (Float.valueOf(discountEditor.getText().toString()) > 100.0 && percentButton.isChecked()) {
			Toast.makeText(getBaseContext(), "New discount can not be added with discount value more than 100%",
					Toast.LENGTH_LONG).show();
		} else if (Float.valueOf(discountEditor.getText().toString()) == 0f) {
			Toast.makeText(getBaseContext(), "New discount can not be added with empty discount value",
					Toast.LENGTH_LONG).show();
		} else {
			createNewDiscount();
		}
	}
	
	private String getMembershipId() {
		if (allButton.isChecked()) {
			return "0";
		} else if (premiumButton.isChecked()) {
			return "1";
		} else {
			return "2";
		}
		
	}
	
	private RadioButton allButton;
	private RadioButton premiumButton;
	private RadioButton percentButton;
	private EditText orderSubtotalEditor;
	private EditText discountEditor;
}
