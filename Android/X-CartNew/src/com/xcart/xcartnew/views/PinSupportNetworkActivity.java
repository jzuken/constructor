package com.xcart.xcartnew.views;

import android.os.Bundle;

import com.xcart.xcartnew.managers.DialogManager;
import com.xcart.xcartnew.managers.network.GetRequester;

public class PinSupportNetworkActivity extends PinSupportActivity {

    protected DialogManager dialogManager;

	public void showConnectionErrorMessage() {
        dialogManager.showNetworkErrorDialog();
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogManager = new DialogManager(getSupportFragmentManager());
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
