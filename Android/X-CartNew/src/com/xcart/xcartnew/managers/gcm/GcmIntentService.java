package com.xcart.xcartnew.managers.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.xcart.xcartnew.R;
import com.xcart.xcartnew.views.Dashboard;
import com.xcart.xcartnew.views.OrderInfo;
import com.xcart.xcartnew.views.Products;

public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	public static final String TAG = "GCM Demo";

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				sendNotification("Send error: " + extras.toString(), new Intent(this, Dashboard.class));
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
				sendNotification("Deleted messages on server: " + extras.toString(), new Intent(this, Dashboard.class));
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				String message = extras.getString("message");
				if (message.equals(NEW_ORDER)) {
					Intent screenIntent = new Intent(this, OrderInfo.class);
					screenIntent.putExtra("orderId", extras.getString("data"));
					sendNotification(message, screenIntent);
				}
				if (message.equals(LOW_STOCK)) {
					Intent screenIntent = new Intent(this, Products.class);
					screenIntent.putExtra("sortOption", "lowStock");
					sendNotification(message, screenIntent);
				}
				Log.i(TAG, extras.toString());
			}
		}

		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg, Intent screenIntent) {
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, screenIntent, 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.gcm_icon)
				.setContentTitle("Notification").setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg).setAutoCancel(true);

		mBuilder.setContentIntent(contentIntent);
		Notification notification = mBuilder.build();
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		mNotificationManager.notify(NOTIFICATION_ID, notification);
	}

	private final String NEW_ORDER = "New order received";
	private final String LOW_STOCK = "Low stock for one or more products";
}
