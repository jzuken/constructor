package com.xcart.admin.managers;

import android.util.Log;

/**
 * Created by Nikita on 13.10.13.
 */
public class LogManager {

    private static final boolean isDebug = true;
    private final String TAG;

    public LogManager(String TAG) {
        this.TAG = "X-CartAdmin " + TAG;
    }

    public void d(String message) {
        if (isDebug) {
            Log.d(TAG, message);
        }
    }

    public void e(String message, Throwable tr) {
        Log.e(TAG, message, tr);
    }
}
