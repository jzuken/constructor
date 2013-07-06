package com.example.adminshop;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

public class Unlock extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unlock);
		settingsData = PreferenceManager.getDefaultSharedPreferences(this);
	}

	public void okButtonClick(View v) {
		EditText password = (EditText) findViewById(R.id.unlockPassword);
		if (password.getText().toString().equals(settingsData.getString("password", ""))) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			this.finish();
		}
	}

	private SharedPreferences settingsData;
}
