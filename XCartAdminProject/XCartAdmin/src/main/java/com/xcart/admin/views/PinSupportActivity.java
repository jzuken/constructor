package com.xcart.admin.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;

import com.xcart.admin.R;
import com.xcart.admin.managers.MyActivityManager;
import com.xcart.admin.managers.gcm.GcmIntentService;

public class PinSupportActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        needDownload = true;

        // for logout
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        registerReceiver(receiver, intentFilter);

        fromNotification = getIntent().getBooleanExtra("isFromNotification", false);
    }

    private boolean isPaused;
    private boolean fromOtherPage;
    private boolean fromPin;
    private boolean fromNotification;

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
        MyActivityManager.updateActivitiesState(this);
    }

    /**
     * Override in childs if need some action without pin activity
     */
    protected void withoutPinAction() {
        needDownload = true;
        MyActivityManager.setIsActivitiesFoundState(true);
        if (MyActivityManager.isAfterNotification()) {
            MyActivityManager.setIsAfterNotificationValue(false);
        }
    }

    @Override
    protected void onResume() {
        if ((isPaused && !fromOtherPage && !fromPin && !MyActivityManager.isAfterNotification())
                || !MyActivityManager.isActivitiesFound() || Unlock.isLocked()) {
            Intent intent = new Intent(this, Unlock.class);
            intent.putExtra("afterPause", 1);
            startActivityForResult(intent, pinPageCode);
        } else {
            withoutPinAction();
        }
        isPaused = false;
        fromOtherPage = false;
        fromPin = false;
        super.onResume();
    }

    public void settingsClick(View v) {
        setNeedDownloadValue(false);
        Intent intent = new Intent(this, Settings.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == pinPageCode) {
            fromPin = true;
        } else if (requestCode != pinPageCode) {
            fromOtherPage = true;
        }
    }

    protected boolean isNeedDownload() {
        return needDownload;
    }

    public void setNeedDownloadValue(boolean value) {
        needDownload = value;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("fromOtherPage", fromOtherPage);
        outState.putBoolean("isPaused", isPaused);
        outState.putBoolean("fromPin", fromPin);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        fromOtherPage = savedInstanceState.getBoolean("fromOtherPage");
        isPaused = savedInstanceState.getBoolean("isPaused");
        fromPin = savedInstanceState.getBoolean("fromPin");
        needDownload = true;
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settings:
                setNeedDownloadValue(false);
                Intent intent = new Intent(this, Settings.class);
                startActivityForResult(intent, 1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackButtonClicked(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (fromNotification) {
            MyActivityManager.setIsAfterNotificationValue(true);
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private boolean needDownload;
    private BroadcastReceiver receiver;
    private final int pinPageCode = 2;
}
