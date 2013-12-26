package com.xcart.admin.views;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import com.xcart.admin.R;
import com.xcart.admin.managers.MyActivityManager;
import com.xcart.admin.managers.PreferenceManager;
import com.xcart.admin.managers.XCartApplication;
import com.xcart.admin.managers.gcm.GcmManager;

public class Settings extends PreferenceActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        setupPasswordEditText();
        setupLimitEditText();
        setupGCMSwitch();
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
                    Toast.makeText(getBaseContext(), getString(R.string.password_error), Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void setupLimitEditText() {
        EditTextPreference downloadLimit = (EditTextPreference) findPreference("download_limit");
        setPackChangeListener(downloadLimit, minPack);
    }

    private void setPackChangeListener(EditTextPreference editText, final int minPackSize) {
        editText.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.equals("") || Integer.parseInt((String) newValue) < minPackSize) {
                    Toast.makeText(getBaseContext(), getString(R.string.pack_error) + String.valueOf(minPack - 1), Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void setupGCMSwitch() {
        gcmSwitch = (CheckBoxPreference) findPreference("gcm_switch");
        if (!GcmManager.checkPlayServices(Settings.this)) {
            gcmSwitch.setEnabled(false);
            gcmSwitch.setChecked(false);
        } else {
            gcmSwitch.setChecked(true);
        }
        gcmSwitch.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                GcmManager gcmManager = new GcmManager(Settings.this);
                String regid = gcmManager.getRegistrationId();
                if (gcmSwitch.isChecked()) {
                    gcmManager.unregisterGCMInBackend(regid);
                } else {
                    gcmManager.sendRegistrationIdToBackend(regid);
                }
                return true;
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void setupLogoutButton() {
        Preference button = findPreference("logout");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference arg0) {
                PreferenceManager preferenceManager = XCartApplication.getInstance().getPreferenceManager();
                if (GcmManager.checkPlayServices(Settings.this)) {
                    GcmManager gcmManager = new GcmManager(Settings.this);
                    if (gcmSwitch.isEnabled()) {
                        gcmManager.unregisterInBackground();
                    }
                    if (gcmSwitch.isChecked()) {
                        gcmManager.unregisterGCMInBackend(preferenceManager.getShopUrl(), preferenceManager.getShopKey(), preferenceManager.getRegistrationId());
                    }
                }
                preferenceManager.logout();
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                sendBroadcast(broadcastIntent);
                Intent intent = new Intent(getBaseContext(), ShopAuthorization.class);
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
        MyActivityManager.updateActivitiesState(this);
    }

    @Override
    protected void onResume() {
        if (isPaused && !fromPin && !MyActivityManager.isAfterNotification() || !MyActivityManager.isActivitiesFound() || Unlock.isLocked()) {
            Intent intent = new Intent(this, Unlock.class);
            intent.putExtra("afterPause", 1);
            startActivityForResult(intent, 1);
        } else {
            MyActivityManager.setIsActivitiesFoundState(true);
            if (MyActivityManager.isAfterNotification()) {
                MyActivityManager.setIsAfterNotificationValue(false);
            }
        }
        isPaused = false;
        fromPin = false;
        super.onResume();
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
    private static final int minPack = 10;
    private boolean isPaused;
    private boolean fromPin;
    private CheckBoxPreference gcmSwitch;
}