package com.xcart.xcartnew.views;

import android.widget.Toast;

import com.xcart.xcartnew.views.PinSupportActivity;
import com.xcart.xcartnew.managers.network.GetRequester;

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

	private GetRequester currentDataRequester = new GetRequester() {
        @Override
        protected String doInBackground(Void... params) {
            return null;
        }
    };
}
