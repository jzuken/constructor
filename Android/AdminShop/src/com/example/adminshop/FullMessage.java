package com.example.adminshop;

import android.os.Bundle;
import android.widget.TextView;

public class FullMessage extends PinSupportActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_message);
		TextView message = (TextView) findViewById(R.id.full_message);
		message.setText(getIntent().getStringExtra("message"));
	}
}
