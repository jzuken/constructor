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

    //TODO: set one limit for all lists
    public int getDownloadListLimit(){
        return Integer.parseInt(settingsData.getString("orders_amount", "10"));
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
}
