package com.example.XCartAdmin;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Toast;

public class Unlock extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unlock);
		settingsData = PreferenceManager.getDefaultSharedPreferences(this);
		pin1 = (WheelView) findViewById(R.id.passw_1);
		initWheel(pin1);
		pin2 = (WheelView) findViewById(R.id.passw_2);
		initWheel(pin2);
		pin3 = (WheelView) findViewById(R.id.passw_3);
		initWheel(pin3);
		pin4 = (WheelView) findViewById(R.id.passw_4);
		initWheel(pin4);
		isScrolling = false;
	}

	public void okButtonClick(View v) {
		if (!isScrolling) {
			if (getPassword().equals(settingsData.getString("password", ""))) {
				Intent intent = getIntent();
				if (intent.getIntExtra("afterPause", 0) == 0) {
					Intent newIntent = new Intent(this, MainActivity.class);
					startActivity(newIntent);
				} else {
					setResult(pinPageCode);
					finish();
				}
			}
			else Toast.makeText(this, "incorrect password", Toast.LENGTH_SHORT).show();
		}
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

	private SharedPreferences settingsData;
	private WheelView pin1;
	private WheelView pin2;
	private WheelView pin3;
	private WheelView pin4;
	private boolean isScrolling;
	private final int pinPageCode = 2;
}
