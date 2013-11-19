package com.xcart.admin.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

public class SameSelectableSpinner extends Spinner {

    public SameSelectableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSelection(int position, boolean animate) {
        super.setSelection(position, animate);
        if (listener != null)
            listener.onItemSelected(null, null, position, 0);
    }

    public void setOnItemSelectedEvenIfUnchangedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    private OnItemSelectedListener listener;
}
