package com.xcart.admin.managers.gcm;

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
import com.xcart.admin.views.DashboardActivity;
import com.xcart.admin.views.OrderInfo;
import com.xcart.admin.views.Products;

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

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString(), new Intent(this, DashboardActivity.class));
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString(), new Intent(this, DashboardActivity.class));
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                String message = extras.getString("message");
                if (message.equals(NEW_ORDER)) {
                    Intent screenIntent = new Intent(this, OrderInfo.class);
                    screenIntent.putExtra("orderId", extras.getString("data"));
                    screenIntent.putExtra("isFromNotification", true);
                    sendNotification(message, screenIntent);
                }
                if (message.equals(LOW_STOCK)) {
                    Intent screenIntent = new Intent(this, Products.class);
                    screenIntent.putExtra("sortOption", "lowStock");
                    screenIntent.putExtra("isFromNotification", true);
                    sendNotification(message, screenIntent);
                }
                LOG.d(extras.toString());
            }
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg, Intent screenIntent) {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, screenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_gcm_notification);
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
