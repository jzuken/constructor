package com.example.adminshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Products extends PinSupportActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.products);
	}

	public void addProductClick(View v) {
		Intent intent = new Intent(this, ProductAdder.class);
		startActivityForResult(intent, 1);
	}

	public void productsSearchClick(View v) {

	}
}
