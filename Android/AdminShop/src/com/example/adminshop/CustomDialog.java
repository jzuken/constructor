package com.example.adminshop;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

public class CustomDialog extends Dialog {
    public CustomDialog(Context context, View view) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
    }
}
