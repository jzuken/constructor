package com.xcart.admin.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.xcart.admin.R;
import com.xcart.admin.managers.DialogManager;
import com.xcart.admin.managers.network.Requester;

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
        if (requester != null && !requester.isCancelled()) {
            requester.cancel(true);
        }
    }
}
