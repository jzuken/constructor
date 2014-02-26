package com.xcart.admin.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;

import com.xcart.admin.R;
import com.xcart.admin.managers.DialogManager;
import com.xcart.admin.managers.MyActivityManager;
import com.xcart.admin.managers.XCartApplication;
import com.xcart.admin.managers.network.DevServerApiManager;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

public class Unlock extends ActionBarActivity {

    private static final String PROGRESS_DIALOG = "Unlock_progress";
    private Button okButton;
    private DialogManager dialogManager;
    private static boolean isLockedState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unlock);
        okButton = (Button) findViewById(R.id.unlockOkButton);
        pin1 = (WheelView) findViewById(R.id.passw_1);
        initWheel(pin1);
        pin2 = (WheelView) findViewById(R.id.passw_2);
        initWheel(pin2);
        pin3 = (WheelView) findViewById(R.id.passw_3);
        initWheel(pin3);
        pin4 = (WheelView) findViewById(R.id.passw_4);
        initWheel(pin4);
        isScrolling = false;

        dialogManager = new DialogManager(getSupportFragmentManager());
        isLockedState = true;
    }

    public void okButtonClick(View v) {
        tryUnlock();
    }

    public static boolean isLocked() {
        return isLockedState;
    }

    private void tryUnlock() {
        if (!isScrolling) {
            okButton.setEnabled(false);
            if (getPassword().equals(XCartApplication.getInstance().getPreferenceManager().getPassword())) {
                isLockedState = false;
                MyActivityManager.setIsActivitiesFoundState(true);
                Intent intent = getIntent();
                if (intent.getIntExtra("afterPause", 0) == 0) {
                    Intent newIntent = new Intent(this, DashboardActivity.class);
                    startActivity(newIntent);
                } else {
                    setResult(pinPageCode);
                    finish();
                }
            } else {
                dialogManager.showErrorDialog(R.string.incorrect_password);
                okButton.setEnabled(true);
            }
        }
    }

    private void checkSubscription() {
        dialogManager.showProgressDialog(R.string.checking_subscription, PROGRESS_DIALOG);
        String shopName = XCartApplication.getInstance().getPreferenceManager().getShopName();
        DevServerApiManager.getInstance().checkSubscription(shopName);
    }

    private void initWheel(WheelView wheel) {
        wheel.setViewAdapter(new NumericWheelAdapter(this, 0, 9));
        wheel.setCurrentItem(0);
        wheel.addChangingListener(changedListener);
        wheel.addScrollingListener(scrolledListener);
        wheel.setCyclic(true);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());
    }

    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
        }
    };

    private OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        public void onScrollingStarted(WheelView wheel) {
            isScrolling = true;
        }

        public void onScrollingFinished(WheelView wheel) {
            isScrolling = false;
        }
    };

    private String getPinString(WheelView wheel) {
        return String.valueOf(wheel.getCurrentItem());
    }

    private String getPassword() {
        return getPinString(pin1) + getPinString(pin2) + getPinString(pin3) + getPinString(pin4);
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        if (intent.getIntExtra("afterPause", 0) == 1) {
            moveTaskToBack(true);
        }
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onResume();
        dialogManager.dismissDialog(PROGRESS_DIALOG);
    }

    private WheelView pin1;
    private WheelView pin2;
    private WheelView pin3;
    private WheelView pin4;
    private boolean isScrolling;
    private final int pinPageCode = 2;
}
