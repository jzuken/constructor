package com.example.adminshop;

import android.app.Activity;
import android.content.Intent;

public class PinSupportActivity extends Activity {
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
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
	
	/**
	 * Override in childs if need some action without pin activity
	 */
	protected void withoutPinAction() {	
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
		if (resultCode == RESULT_OK) {
			fromOtherPage = true;
		}
		if (resultCode == 2) {
			fromPin = true;
		}
	}
}
