package com.xcart.xcartadmin.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class PinSupportActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		needDownload = true;
		isNeedPin = getIntent().getIntExtra("pin_request", 1);

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
	}

	private boolean isPaused;
	private boolean fromOtherPage;
	private boolean fromPin;
	private int isNeedPin;

	@Override
	protected void onPause() {
		super.onPause();
		isPaused = true;
	}

	/**
	 * Override in childs if need some action without pin activity
	 */
	protected void withoutPinAction() {
		needDownload = true;
	}

	@Override
	protected void onResume() {
		if ((isPaused && !fromOtherPage && !fromPin) || isNeedPin == 10) {
			isNeedPin = 1;
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
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	private boolean needDownload;
	private BroadcastReceiver receiver;
	private final int pinPageCode = 2;
}
