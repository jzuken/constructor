package com.xcart.xcartnew.managers.network;

import com.xcart.xcartnew.managers.LogManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 14.10.13.
 */
public class DevServerApiManager {

    private static final LogManager LOG = new LogManager(DevServerApiManager.class.getSimpleName());

    private List<SubscriptionCallback> subscriptionCallbacks = new ArrayList<SubscriptionCallback>();

    private static DevServerApiManager instance;

    private DevServerApiManager() {
    }

    public void addSubscriptionCallback(SubscriptionCallback callback) {
        if (!subscriptionCallbacks.contains(callback)) {
            subscriptionCallbacks.add(callback);
        }
    }

    public void  removeSubscriptionCallback(SubscriptionCallback callback){
        subscriptionCallbacks.remove(callback);
    }

    private void notifySubscriptionListeners(SubscriptionStatus status) {
        for (SubscriptionCallback callback : subscriptionCallbacks) {
            callback.onSubscriptionChecked(status);
        }
    }


    public static DevServerApiManager getInstance() {
        if (instance == null) {
            instance = new DevServerApiManager();
        }
        return instance;
    }

    public void checkSubscription(String shopUrl) {
        new CheckSubscriptionTask(shopUrl) {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (s == null) {
                    notifySubscriptionListeners(SubscriptionStatus.NetworkError);
                } else {
                    parseSubscriptionResponse(s);
                }
            }
        }.execute();
    }

    private void parseSubscriptionResponse(String response) {
        LOG.d("parseSubscriptionResponse: " + response);

        try {
            JSONObject obj = new JSONObject(response);
            if (!obj.has("subscribed")) {
                notifySubscriptionListeners(SubscriptionStatus.None);
                return;
            }

            String subscription = obj.getString("subscribed");
            if (subscription.equals("none")) {
                notifySubscriptionListeners(SubscriptionStatus.None);
            } else if (subscription.equals("expired")) {
                notifySubscriptionListeners(SubscriptionStatus.Expired);
            } else if (subscription.equals("active")) {
                notifySubscriptionListeners(SubscriptionStatus.Active);
            } else {
            	notifySubscriptionListeners(SubscriptionStatus.None);
            }

        } catch (JSONException e) {
            LOG.e("JSONException", e);
            notifySubscriptionListeners(SubscriptionStatus.None);
        }
    }
}
