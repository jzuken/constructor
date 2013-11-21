package com.xcart.admin.managers;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.List;

/**
 * Created by vladimir-zakharov on 20.11.13.
 */
public class MyActivityManager {
    private static boolean isActivitiesFound;
    private static boolean isFromNotification;
    private static boolean isAfterNotification;

    public static boolean isFromNotification() {
        return  isFromNotification;
    }

    public static void setIsFromNotificationValue(boolean value) {
        isFromNotification = value;
    }

    public static boolean isAfterNotification() {
        return  isAfterNotification;
    }

    public static void setIsAfterNotificationValue(boolean value) {
        isAfterNotification = value;
    }

    public static void updateActivitiesState(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);

        if (services.get(0).topActivity.getPackageName().toString()
                .equalsIgnoreCase(context.getPackageName().toString())) {
            isActivitiesFound = true;
        } else {
            isActivitiesFound = false;
        }
    }

    public static boolean isActivitiesFound() {
        return isActivitiesFound;
    }

    public static void setIsActivitiesFoundState(boolean state) {
        isActivitiesFound = state;
    }
}
