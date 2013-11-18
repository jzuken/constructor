package com.xcart.xcartadmin.views.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

/**
 * Created by Nikita on 10/31/13.
 */
public class BaseArrayAdapter<T> extends ArrayAdapter<T> {

    protected List<T> items;
    protected int layoutResourceId;
    protected Context context;
    protected LayoutInflater inflater;

    public BaseArrayAdapter(Context context, int resource, List<T> items) {
        super(context, resource, items);
        this.context = context;
        this.layoutResourceId = resource;
        this.items = items;
        this.inflater = ((Activity) context).getLayoutInflater();
    }
}
