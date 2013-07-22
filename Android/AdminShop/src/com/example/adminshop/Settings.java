package com.example.adminshop;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class Settings extends PreferenceActivity {
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		EditTextPreference password = (EditTextPreference) findPreference("password");
		password.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				int newValueLength = ((String) newValue).length();
				if (newValueLength != 4) {
					Toast.makeText(getBaseContext(), "Password must consists of four digits", Toast.LENGTH_LONG).show();
					return false;
				}
				return true;
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		super.onBackPressed();
	}
	private boolean isPaused;
	private boolean fromPin;
	
	@Override
	protected void onPause() {
		super.onPause();
		isPaused = true;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if (isPaused && !fromPin) {
			Intent intent = new Intent(this, Unlock.class);
			intent.putExtra("afterPause", 1);
			startActivityForResult(intent, 1);
		}
		isPaused = false;
		fromPin = false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 2) {
			fromPin = true;
		}
	}
}