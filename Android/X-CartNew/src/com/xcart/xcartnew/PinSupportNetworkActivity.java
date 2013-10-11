package com.xcart.xcartnew;

import android.widget.Toast;

public class PinSupportNetworkActivity extends PinSupportActivity {
	public void showConnectionErrorMessage() {
		Toast.makeText(getBaseContext(),
				"Sorry, unable to connect to server. Cannot update data. Please check your internet connection",
				Toast.LENGTH_SHORT).show();
	}

	public void setRequester(GetRequester requester) {
		this.currentDataRequester = requester;
	}

	@Override
	public void onDestroy() {
		cancelRequest();
		super.onDestroy();
	}
	
	public void cancelRequest() {
		currentDataRequester.cancel(true);
	}

	private GetRequester currentDataRequester = new GetRequester();
}
