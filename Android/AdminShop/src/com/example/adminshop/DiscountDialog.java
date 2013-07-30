package com.example.adminshop;

import android.os.Bundle;
import android.view.View;

public class DiscountDialog extends PinSupportActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discount_dialog);
	}
	
	public void deleteClick(View v) {
		setResult(3);
		finish();
	}
	
	public void editClick(View v) {
		setResult(4);
		finish();
	}
}
