package com.example.adminshop;

import android.widget.Toast;

public class PinSupportNetworkActivity extends PinSupportActivity {
	public void showConnectionErrorMessage() {
		Toast.makeText(getBaseContext(),
				"Sorry, unable to connect to server. Cannot update data. Please check your internet connection",
				Toast.LENGTH_LONG).show();
	}
}
