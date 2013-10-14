package com.xcart.xcartnew.managers.network;

import android.os.AsyncTask;

/**
 * Created by Nikita on 14.10.13.
 */
public class CheckSubscriptionTask  extends AsyncTask<Void, Void, String>{

    private String shopUrl;

    public CheckSubscriptionTask(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    @Override
    protected String doInBackground(Void... params) {
        return new HttpManager().checkSubscription(shopUrl);
    }
}
