package com.xcart.admin.managers;

import android.app.Application;

/**
 * Created by Nikita on 12/24/13.
 */
public class XCartApplication extends Application {

    private static XCartApplication instance;

    private PreferenceManager preferenceManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static XCartApplication getInstance(){
        return instance;
    }

    public PreferenceManager getPreferenceManager(){
        if(preferenceManager == null){
            preferenceManager = new PreferenceManager(this);
        }
        return preferenceManager;
    }
}
