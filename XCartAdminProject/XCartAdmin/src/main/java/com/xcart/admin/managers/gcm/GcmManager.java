package com.xcart.admin.managers.gcm;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.xcart.admin.R;
import com.xcart.admin.managers.DialogManager;
import com.xcart.admin.managers.LogManager;
import com.xcart.admin.managers.PreferenceManager;
import com.xcart.admin.managers.XCartApplication;
import com.xcart.admin.managers.network.HttpManager;

import java.io.IOException;

public class GcmManager {

    public GcmManager(Context context) {
        this.context = (Activity) context;
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(context);
        } else {
            LOG.d("No valid Google Play Services APK found.");
        }
    }

    public String getRegistrationId() {
        PreferenceManager preferenceManager = XCartApplication.getInstance().getPreferenceManager();
        String registrationId = preferenceManager.getRegistrationId();
        if (isEmpty(registrationId)) {
            LOG.d("Registration not found.");
            return "";
        }

        int registeredVersion = preferenceManager.getAppVersion();
        int currentVersion = preferenceManager.getCurrentAppVersion();
        if (registeredVersion != currentVersion) {
            LOG.d("App version changed.");
            return "";
        }
        return registrationId;
    }

    public void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    String regId = gcm.register(SENDER_ID);
                    LOG.d("reg id = " + regId);

                    sendRegistrationIdToBackend(regId);
                    XCartApplication.getInstance().getPreferenceManager().saveGcmRegistration(regId);
                } catch (IOException e) {
                    LOG.e(e.getMessage(), e);
                }
                return msg;
            }
        }.execute();
    }

    public void unregisterInBackground() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    gcm.unregister();
                } catch (IOException e) {
                    LOG.e(e.getMessage(), e);
                }
                return null;
            }
        }.execute();
    }

    public void sendRegistrationIdToBackend(final String regid) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String authResult = new HttpManager(context).sendRegIdToBackend(regid);
                LOG.d("reg response" + authResult);
                return authResult;
            }
        }.execute();

    }

    public void unregisterGCMInBackend(final String regid) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String unregResult = new HttpManager(context).unregisterGCMInBackend(regid);
                LOG.d("unreg response" + unregResult);
                return unregResult;
            }
        }.execute();
    }

    public void unregisterGCMInBackend(final String apiUrl, final String apiKey, final String regid) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String unregResult = new HttpManager().unregisterGCMInBackend(apiUrl, apiKey, regid);
                LOG.d("unreg response: " + unregResult);
                return unregResult;
            }
        }.execute();
    }

    public static boolean checkPlayServices(Context context) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        return resultCode == ConnectionResult.SUCCESS;
    }

    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, context, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                LOG.d("This device is not supported.");
                new DialogManager(((FragmentActivity) context).getSupportFragmentManager()).showErrorDialog(
                        R.string.play_not_supported, null);
            }
            return false;
        }
        return true;
    }

    private boolean isEmpty(String string) {
        return string.equals("");
    }

    private static final LogManager LOG = new LogManager(GcmManager.class.getSimpleName());
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    //    private static final String SENDER_ID = "175432225698";
    private static final String SENDER_ID = "443902181577";
    private GoogleCloudMessaging gcm;
    private Activity context;
}
