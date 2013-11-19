package com.xcart.admin.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;

import com.xcart.admin.R;
import com.xcart.admin.managers.DialogManager;
import com.xcart.admin.managers.network.DevServerApiManager;
import com.xcart.admin.managers.network.SubscriptionCallback;
import com.xcart.admin.managers.network.SubscriptionStatus;
import com.xcart.admin.views.dialogs.ConnectionErrorDialog;
import com.xcart.admin.views.dialogs.ErrorDialog;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

public class Unlock extends FragmentActivity implements SubscriptionCallback {

    private static final String PROGRESS_DIALOG = "Unlock_progress";
    private Button okButton;
    private DialogManager dialogManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unlock);
        settingsData = PreferenceManager.getDefaultSharedPreferences(this);
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
    }

    public void okButtonClick(View v) {
        okButton.setEnabled(false);
        tryUnlock();
    }

    private void tryUnlock() {
        if (!isScrolling) {
            if (getPassword().equals(settingsData.getString("password", ""))) {
                checkSubscription();
            } else {
                dialogManager.showErrorDialog(R.string.incorrect_password);
                okButton.setEnabled(true);
            }
        }
    }

    private void checkSubscription() {
        dialogManager.showProgressDialog(R.string.checking_subscription, PROGRESS_DIALOG);

        SharedPreferences authorizationData = getSharedPreferences("AuthorizationData", Context.MODE_PRIVATE);
        String shopName = authorizationData.getString("shop_name", "");
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
        DevServerApiManager.getInstance().addSubscriptionCallback(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DevServerApiManager.getInstance().removeSubscriptionCallback(this);
    }

    @Override
    protected void onPause() {
        super.onResume();
        dialogManager.dismissDialog(PROGRESS_DIALOG);
    }

    private SharedPreferences settingsData;
    private WheelView pin1;
    private WheelView pin2;
    private WheelView pin3;
    private WheelView pin4;
    private boolean isScrolling;
    private final int pinPageCode = 2;

    @Override
    public void onSubscriptionChecked(SubscriptionStatus status) {
        dialogManager.dismissDialog(PROGRESS_DIALOG);

        switch (status) {
            case Active:
                Intent intent = getIntent();
                if (intent.getIntExtra("afterPause", 0) == 0) {
                    Intent newIntent = new Intent(this, Dashboard.class);
                    startActivity(newIntent);
                } else {
                    setResult(pinPageCode);
                    finish();
                }
                break;
            case Expired:
                new ErrorDialog(R.string.subscription_expired, null).show(getSupportFragmentManager(), "subscribed");
                break;
            case None:
                new ErrorDialog(R.string.no_subscription, null).show(getSupportFragmentManager(), "subscribed");
                break;
            case NetworkError:
                new ConnectionErrorDialog().show(getSupportFragmentManager(), "subscription");
                break;
        }
        okButton.setEnabled(true);
    }
}
