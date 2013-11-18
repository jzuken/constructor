package com.xcart.xcartadmin.views;

import android.os.Bundle;

import com.xcart.xcartadmin.managers.DialogManager;
import com.xcart.xcartadmin.managers.network.Requester;

public class PinSupportNetworkActivity extends PinSupportActivity {

    protected DialogManager dialogManager;

    protected Requester requester;

	public void showConnectionErrorMessage() {
        dialogManager.showNetworkErrorDialog();
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogManager = new DialogManager(getSupportFragmentManager());
    }


	@Override
	public void onDestroy() {
		cancelRequest();
		super.onDestroy();
	}
	
	public void cancelRequest() {
		if(requester != null && !requester.isCancelled()){
            requester.cancel(true);
        }
	}
}
