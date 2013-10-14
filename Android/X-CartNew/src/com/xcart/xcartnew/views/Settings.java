package com.xcart.xcartnew.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import com.xcart.xcartnew.R;

public class Settings extends PreferenceActivity {
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);

		setupPasswordEditText();
		setupOrdersAmountEditText();
		setupUsersAmountEditText();
		setupReviewsAmountEditText();
		setupProductsAmountEditText();
		setupLogoutButton();
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
	private void setupOrdersAmountEditText() {
		EditTextPreference usersAmount = (EditTextPreference) findPreference("orders_amount");
		setPackChangeListener(usersAmount, minPack);
	}

	@SuppressWarnings("deprecation")
	private void setupUsersAmountEditText() {
		EditTextPreference usersAmount = (EditTextPreference) findPreference("users_amount");
		setPackChangeListener(usersAmount, minPack);
	}

	@SuppressWarnings("deprecation")
	private void setupReviewsAmountEditText() {
		EditTextPreference reviewsAmount = (EditTextPreference) findPreference("reviews_amount");
		setPackChangeListener(reviewsAmount, minPack);
	}

	@SuppressWarnings("deprecation")
	private void setupProductsAmountEditText() {
		EditTextPreference productsAmount = (EditTextPreference) findPreference("products_amount");
		setPackChangeListener(productsAmount, minPack);
	}

	private void setPackChangeListener(EditTextPreference editText, final int minPackSize) {
		editText.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if (newValue.equals("") || Integer.parseInt((String) newValue) < minPackSize) {
					Toast.makeText(getBaseContext(), "Pack must be bigger than " + String.valueOf(minPack - 1),
							Toast.LENGTH_LONG).show();
					return false;
				}
				return true;
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void setupLogoutButton() {
		Preference button = (Preference) findPreference("logout");
		button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				SharedPreferences authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
				Editor editor = authorizationData.edit();
				editor.remove("logged");
				editor.commit();
				Intent broadcastIntent = new Intent();
				broadcastIntent.setAction("com.package.ACTION_LOGOUT");
				sendBroadcast(broadcastIntent);
				Intent intent = new Intent(getBaseContext(), Authorization.class);
				startActivity(intent);
				finish();
				return true;
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isPaused", isPaused);
		outState.putBoolean("fromPin", fromPin);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		isPaused = savedInstanceState.getBoolean("isPaused");
		fromPin = savedInstanceState.getBoolean("fromPin");
		super.onRestoreInstanceState(savedInstanceState);
	}

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

	@Override
	public void onBackPressed() {
		setResult(fromSettingCode);
		super.onBackPressed();
	}

	public static final int fromSettingCode = 3;
	private final int minPack = 10;
	private boolean isPaused;
	private boolean fromPin;
	
}