package com.example.adminshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class PinSupportActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			needDownload = true;
		} else {
			needDownload = false;
		}
	}
	
	@Override
	public void onBackPressed() {
		setBackResult();
		super.onBackPressed();
	}
	private boolean isPaused;
	private boolean fromOtherPage;
	private boolean fromPin;
	
	@Override
	protected void onPause() {
		super.onPause();
		isPaused = true;
	}
	
	protected void setBackResult() {
		setResult(RESULT_OK);
	}
	
	/**
	 * Override in childs if need some action without pin activity
	 */
	protected void withoutPinAction() {	
		needDownload = true;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if (isPaused && !fromOtherPage && !fromPin) {
			Intent intent = new Intent(this, Unlock.class);
			fromOtherPage = true;
			intent.putExtra("afterPause", 1);
			startActivityForResult(intent, 1);
		} else {
			withoutPinAction();
		}
		isPaused = false;
		fromOtherPage = false;
		fromPin = false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK || resultCode > 3) {
			fromOtherPage = true;
		}
		if (resultCode == 2) {
			fromPin = true;
		}
	}
	
	protected boolean isNeedDownload() {
		return needDownload;
	}
	
	public void setNeedDownloadValue(boolean value) {
		needDownload = value;
	}
	
	private boolean needDownload;
}
