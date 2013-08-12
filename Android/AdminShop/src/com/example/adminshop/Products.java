package com.example.adminshop;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

public class Products extends PinSupportNetworkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.products);
		setupSearchLine();
	}

	private void setupSearchLine() {
		productsSearchLine = (EditText) findViewById(R.id.productsSearchLine);
		productsSearchLine.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					return true;
				}
				return false;
			}
		});
	}

	private EditText productsSearchLine;
}
