package com.example.adminshop;

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
					Toast.makeText(getBaseContext(), "Password must consists of four digits", Toast.LENGTH_SHORT).show();
					return false;
				}
				return true;
			}
		});
	}
}