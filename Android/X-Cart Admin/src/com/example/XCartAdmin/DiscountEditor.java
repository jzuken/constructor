package com.example.XCartAdmin;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class DiscountEditor extends PinSupportNetworkActivity {
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
		absoluteButton = (RadioButton) findViewById(R.id.absoluteRadioButton);

		if (getIntent().getStringExtra("discountType").equals("Percent, %")) {
			percentButton.setChecked(true);
		} else {
			absoluteButton.setChecked(true);
		}

		orderSubtotalEditor = (EditText) findViewById(R.id.orderSubtotalEditor);
		orderSubtotalEditor.setText(getIntent().getCharSequenceExtra("orderSub"));
		discountEditor = (EditText) findViewById(R.id.discountEditor);
		discountEditor.setText(getIntent().getCharSequenceExtra("discount"));
		TextView title = (TextView) findViewById(R.id.discount_editor_title);
		title.setText(getResources().getString(R.string.edit_discount));
	}

	@Override
	public void onBackPressed() {
		setResult(noUpdateCode);
		super.onBackPressed();
	}

	public void okClick(View v) {
		try {
			Double discountValue = Double.parseDouble((discountEditor.getText().toString()));
			Double.parseDouble(orderSubtotalEditor.getText().toString());
			if (discountValue > 100.0 && percentButton.isChecked()) {
				Toast.makeText(getBaseContext(), "Discount can not be added with discount value more than 100%",
						Toast.LENGTH_LONG).show();
			} else if (discountValue == 0) {
				Toast.makeText(getBaseContext(), "Discount can not be added with empty discount value",
						Toast.LENGTH_LONG).show();
			} else {
				updateDiscount();
			}
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "Incorrect input", Toast.LENGTH_SHORT).show();
		}
	}

	private void updateDiscount() {
		String provider = "1";
		String id = getIntent().getStringExtra("id");
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
					"https://54.213.38.9/xcart/api.php?request=update_discount&id=" + id + "&minprice="
							+ orderSubtotalValue + "&discount=" + discountValue + "&discount_type=" + discountTypeValue
							+ "&provider=" + provider + membershipIdParametr).get();
		} catch (Exception e) {
			response = null;
		}
		if (response != null) {
			Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
			finish();
		} else {
			showConnectionErrorMessage();
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

	private String getDiscountType() {
		if (percentButton.isChecked()) {
			return "percent";
		} else {
			return "absolute";
		}
	}

	private RadioButton allButton;
	private RadioButton premiumButton;
	private RadioButton wholesaleButton;
	private RadioButton percentButton;
	private RadioButton absoluteButton;
	private EditText orderSubtotalEditor;
	private EditText discountEditor;
	private final int noUpdateCode = 4;
}
