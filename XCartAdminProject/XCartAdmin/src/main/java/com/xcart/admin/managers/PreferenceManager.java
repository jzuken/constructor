package com.xcart.admin.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Nikita on 12/24/13.
 */
public class PreferenceManager {


    private SharedPreferences authorizationData;
    private SharedPreferences gcmPreferences;
    private SharedPreferences settingsData;


    public PreferenceManager(Context context) {
        authorizationData = context.getSharedPreferences("AuthorizationData", Context.MODE_PRIVATE);
        gcmPreferences = context.getSharedPreferences("gcm preference", Context.MODE_PRIVATE);
        settingsData = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveAuth(String shop, String key) {
        SharedPreferences.Editor editor = authorizationData.edit();
        editor.putString("shop_key", shop);
        editor.putString("shop_name", key);
        editor.commit();
    }

    public String getRegistrationId() {
        return gcmPreferences.getString("registration_id", "");
    }

    public int getAppVersion() {
        return gcmPreferences.getInt("appVersion", Integer.MIN_VALUE);
    }

    public String getShopUrl() {
        return authorizationData.getString("shop_api", "");
    }

    public String getShopName() {
        return authorizationData.getString("shop_name", "");
    }

    public void savePassword(String pass) {
        SharedPreferences.Editor editor = settingsData.edit();
        editor.putString("password", pass);
        editor.commit();
    }

    public String getPassword() {
        return settingsData.getString("password", "0000");
    }

    public Boolean isShopLogged() {
        return authorizationData.getBoolean("shop_logged", false);
    }

    public void saveShopUrl(String url) {
        SharedPreferences.Editor editor = authorizationData.edit();
        editor.putBoolean("shop_logged", true);
        editor.putString("shop_api", url);
        editor.commit();
    }

    public String getShopKey() {
        return authorizationData.getString("shop_key", "");
    }

    public void saveGcmRegistration(String regId) {
        SharedPreferences.Editor editor = gcmPreferences.edit();
        editor.putString("registration_id", regId);
        editor.putInt("appVersion", getCurrentAppVersion());
        editor.commit();
    }

    public int getDownloadListLimit() {
        return Integer.parseInt(settingsData.getString("download_limit", "10"));
    }

    public int getCurrentAppVersion() {
        try {
            Context context = XCartApplication.getInstance().getApplicationContext();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public void logout() {
        SharedPreferences.Editor editor = authorizationData.edit();
        editor.remove("shop_logged");
        editor.remove("shop_api");
        editor.remove("shop_name");
        editor.remove("shop_key");
        editor.commit();
        editor = gcmPreferences.edit();
        editor.remove("registration_id");
        editor.commit();
    }

    public boolean isPasswordProtectionEnabled() {
        return settingsData.getBoolean("security_switch", false);
    }

    public void saveCurrencyType(String symbol, String format) {
        SharedPreferences.Editor editor = settingsData.edit();
        editor.putString("symbol", symbol);
        editor.putString("format", format.replace("x", "%s"));
        editor.apply();
    }

    public String getCurrencySymbol() {
        return settingsData.getString("symbol", "$");
    }

    public String getCurrencyFormat() {
        return settingsData.getString("format", "$%s");
    }

    public void saveXCartVersion(String version) {
        SharedPreferences.Editor editor = settingsData.edit();
        editor.putString("version", version);
        editor.apply();
    }

    public String getXCartVersion() {
        return settingsData.getString("version", "XCart4");
    }

    public void saveConfig(String config) {
        SharedPreferences.Editor editor = settingsData.edit();
        editor.putString("config", config);
        editor.apply();
    }

    public String getConfig() {
        return settingsData.getString("config", "");
    }
}
