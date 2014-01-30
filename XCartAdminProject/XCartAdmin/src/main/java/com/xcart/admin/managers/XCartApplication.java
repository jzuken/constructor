package com.xcart.admin.managers;

import android.app.Application;
import android.content.pm.PackageManager;

/**
 * Created by Nikita on 12/24/13.
 */
public class XCartApplication extends Application {

    private static final LogManager LOG = new LogManager(XCartApplication.class.getSimpleName());

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

    public String getApplicationVersionName() {
        String versionName = "0.0.0";
        try {
            String packageName = this.getPackageName();
            versionName = this.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LOG.e(e.getMessage(), e);
        }
        return versionName;
    }



    //
    private String productsSortOrder;

    public String getProductsSortOrder() {
        return productsSortOrder;
    }

    public void setProductsSortOrder(String productsSortOrder) {
        this.productsSortOrder = productsSortOrder;
    }
}
