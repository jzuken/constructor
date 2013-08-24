package com.example.adminshop;

import android.os.Bundle;
import android.widget.TextView;

public class FullMessage extends PinSupportActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_message);
		TextView email = (TextView) findViewById(R.id.review_user);
		email.setText(getIntent().getStringExtra("email"));
		TextView product = (TextView) findViewById(R.id.review_product);
		product.setText(getIntent().getStringExtra("product"));
		TextView message = (TextView) findViewById(R.id.full_message);
		message.setText(getIntent().getStringExtra("message"));
	}

	@Override
	public void onBackPressed() {
		setResult(noUpdateCode);
		super.onBackPressed();
	}

	private final int noUpdateCode = 4;
}
