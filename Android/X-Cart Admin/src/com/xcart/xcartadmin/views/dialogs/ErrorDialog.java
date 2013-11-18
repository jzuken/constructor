package com.xcart.xcartadmin.views.dialogs;

import android.content.DialogInterface;

import com.xcart.xcartadmin.R;

/**
 * Created by Nikita on 14.10.13.
 */
public class ErrorDialog extends OkDialog {
	
	public ErrorDialog() {}

    public ErrorDialog(int messageId, DialogInterface.OnClickListener onClickListener) {
        super(messageId, R.string.error, onClickListener);
    }
}
