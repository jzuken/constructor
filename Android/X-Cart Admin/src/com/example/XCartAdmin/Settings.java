package com.example.XCartAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
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

		setupPasswordEditText();
		setupUsersAmountEditText();
		setupReviewsAmountEditText();
		setupProductsAmountEditText();
		setupFirstScreenChooser();
	}

	@SuppressWarnings("deprecation")
	private void setupPasswordEditText() {
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

	@SuppressWarnings("deprecation")
	private void setupUsersAmountEditText() {
		EditTextPreference usersAmount = (EditTextPreference) findPreference("users_amount");
		setPackChangeListener(usersAmount, 7);
	}

	@SuppressWarnings("deprecation")
	private void setupReviewsAmountEditText() {
		EditTextPreference reviewsAmount = (EditTextPreference) findPreference("reviews_amount");
		setPackChangeListener(reviewsAmount, 7);
	}

	@SuppressWarnings("deprecation")
	private void setupProductsAmountEditText() {
		EditTextPreference productsAmount = (EditTextPreference) findPreference("products_amount");
		setPackChangeListener(productsAmount, 7);
	}

	private void setPackChangeListener(EditTextPreference editText, final int minPackSize) {
		editText.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if (newValue.equals("") || Integer.parseInt((String) newValue) < minPackSize) {
					Toast.makeText(getBaseContext(), "Pack must be bigger than " + String.valueOf(minPackSize - 1),
							Toast.LENGTH_LONG).show();
					return false;
				}
				return true;
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void setupFirstScreenChooser() {
		final ListPreference chooser = (ListPreference) findPreference("screens_list");

		chooser.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if (!chooser.getValue().equals(newValue)) {
					Toast.makeText(getBaseContext(), "Changes will apply after restart app", Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});
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