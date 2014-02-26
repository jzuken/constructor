package com.xcart.admin.views;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.xcart.admin.managers.XCartApplication;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: describe hack
        SharedPreferences settingsData = PreferenceManager.getDefaultSharedPreferences(this);
        if (!settingsData.contains("password")) {
            Editor editor = settingsData.edit();
            editor.putString("password", "0000");
            editor.commit();
        }
        if (!XCartApplication.getInstance().getPreferenceManager().isShopLogged()) {
            Intent intent = new Intent(this, ShopAuthorization.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
//		else
//        if (!(authorizationData.getBoolean("logged", false))) {
//			Intent intent = new Intent(this, Authorization.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//			startActivity(intent);
//		}
        else if (!XCartApplication.getInstance().getPreferenceManager().isPasswordProtectionEnabled()) {
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, Unlock.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
