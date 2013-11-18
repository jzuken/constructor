package com.xcart.xcartadmin.managers.gcm;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.xcart.xcartadmin.R;
import com.xcart.xcartadmin.managers.DialogManager;
import com.xcart.xcartadmin.managers.LogManager;
import com.xcart.xcartadmin.managers.network.HttpManager;

public class GCMManager {

	public GCMManager(Context context) {
		this.context = (Activity) context;
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(context);
		} else {
			LOG.d("No valid Google Play Services APK found.");
		}
	}

	public String getRegistrationId() {
		final SharedPreferences prefs = getGcmPreferences();
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (isEmpty(registrationId)) {
			LOG.d("Registration not found.");
			return "";
		}

		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
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
					String regid = gcm.register(SENDER_ID);
					LOG.d("reg id = " + regid);

					sendRegistrationIdToBackend(regid);
					storeRegistrationId(regid);
				} catch (IOException e) {
					LOG.e(e.getMessage(), e);
				}
				return msg;
			}
		}.execute(null, null, null);
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
		}.execute(null, null, null);
	}

	public void sendRegistrationIdToBackend(final String regid) {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String authResult = new HttpManager(context).sendRegIdToBackend(regid);
				LOG.d("reg response" + authResult);
				return authResult;
			}
		}.execute(null, null, null);

	}

	public void unregisterGCMInBackend(final String regid) {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String unregResult = new HttpManager(context).unregisterGCMInBackend(regid);
				LOG.d("unreg response" + unregResult);
				return unregResult;
			}
		}.execute(null, null, null);
	}
	
	public void unregisterGCMInBackend(final String apiUrl, final String apiKey, final String regid) {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String unregResult = new HttpManager().unregisterGCMInBackend(apiUrl, apiKey, regid);
				LOG.d("unreg response" + unregResult);
				return unregResult;
			}
		}.execute(null, null, null);
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

	private void storeRegistrationId(String regId) {
		final SharedPreferences prefs = getGcmPreferences();
		int appVersion = getAppVersion(context);
		LOG.d("Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private SharedPreferences getGcmPreferences() {
		return context.getSharedPreferences("gcm preference", Context.MODE_PRIVATE);
	}

	private boolean isEmpty(String string) {
		return string.equals("");
	}

	private static final LogManager LOG = new LogManager(GCMManager.class.getSimpleName());
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final String SENDER_ID = "443902181577";
	private GoogleCloudMessaging gcm;
	private Activity context;
}
