package com.xcart.xcartnew.managers;

import android.content.Context;
import android.content.res.Resources;

import com.xcart.xcartnew.R;
import com.xcart.xcartnew.model.OrderStatus;

/**
 * Created by Nikita on 10/31/13.
 */
public class StatusConverter {

    public static String getStatusBySymbol(Context context, OrderStatus symbol) {
        switch (symbol) {
            case I:
                return context.getString(R.string.not_finished);
            case Q:
                return context.getString(R.string.queued);
            case P:
                return context.getString(R.string.processed);
            case B:
                return context.getString(R.string.backordered);
            case D:
                return context.getString(R.string.declined);
            case F:
                return context.getString(R.string.failed);
            case C:
                return context.getString(R.string.complete);
            default:
                return "";
        }
    }

    public static int getColorResourceBySymbol(Context context, OrderStatus symbol) {
        Resources resources = context.getResources();
        switch (symbol) {
            case I:
            case D:
            case F:
                return resources.getColor(R.color.red_status);
            case Q:
            case B:
                return resources.getColor(R.color.dark_blue_status);
            case P:
                return resources.getColor(R.color.light_blue_status);
            case C:
                return resources.getColor(R.color.green_status);
            default:
                return resources.getColor(R.color.black);
        }
    }

}
