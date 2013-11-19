package com.xcart.admin.managers.gcm;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.xcart.admin.R;
import com.xcart.admin.managers.LogManager;
import com.xcart.admin.views.Dashboard;
import com.xcart.admin.views.OrderInfo;
import com.xcart.admin.views.Products;

import java.util.List;

public class GcmIntentService extends IntentService {

    private static final LogManager LOG = new LogManager(GcmIntentService.class.getSimpleName());

    public static final int NOTIFICATION_ID = 1;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        Context appContext = getApplicationContext();

        ActivityManager activityManager = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);
        boolean isActivityFound = false;

        if (services.get(0).topActivity.getPackageName().toString()
                .equalsIgnoreCase(appContext.getPackageName().toString())) {
            isActivityFound = true;
        }

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString(), new Intent(this, Dashboard.class));
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString(), new Intent(this, Dashboard.class));
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                String message = extras.getString("message");
                if (message.equals(NEW_ORDER)) {
                    Intent screenIntent = new Intent(this, OrderInfo.class);
                    /*if (!isActivityFound) {
                        screenIntent.putExtra("pin_request", 10);
					}*/
                    screenIntent.putExtra("orderId", extras.getString("data"));
                    sendNotification(message, screenIntent);
                }
                if (message.equals(LOW_STOCK)) {
                    Intent screenIntent = new Intent(this, Products.class);
					/*if (!isActivityFound) {
						screenIntent.putExtra("pin_request", 10);
					}*/
                    screenIntent.putExtra("sortOption", "lowStock");
                    sendNotification(message, screenIntent);
                }
                LOG.d(extras.toString());
            }
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg, Intent screenIntent) {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, screenIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.gcm_icon);
        builder.setContentTitle("Notification");
        builder.setStyle(
                new NotificationCompat
                        .BigTextStyle()
                        .bigText(msg));
        builder.setContentText(msg);
        builder.setAutoCancel(true);

        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private final String NEW_ORDER = "New order received";
    private final String LOW_STOCK = "Low stock for one or more products";
}
