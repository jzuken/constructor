package com.example.adminshop;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
		SharedPreferences settingsData = PreferenceManager.getDefaultSharedPreferences(this);
		if (!settingsData.contains("password")) {
			Editor editor = settingsData.edit();
			editor.putString("password", "1234");
			editor.commit();
		}
		if (!(authorizationData.getBoolean("logged", false))) {
			Intent intent = new Intent(this, Authorization.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, Unlock.class);
			startActivity(intent);
		}
		this.finish();
	}
}
