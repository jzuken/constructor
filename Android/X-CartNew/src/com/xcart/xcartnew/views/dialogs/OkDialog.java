package com.xcart.xcartnew.views.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.xcart.xcartnew.R;

/**
 * Created by Nikita on 14.10.13.
 */
public class OkDialog extends DialogFragment {

    private int messageId;
    private int titleId;
    private DialogInterface.OnClickListener onClickListener;

    public OkDialog(int messageId, int titleId, DialogInterface.OnClickListener onClickListener) {
        this.messageId = messageId;
        this.titleId = titleId;
        this.onClickListener = onClickListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titleId);
        builder.setMessage(messageId);
        builder.setPositiveButton(R.string.ok, onClickListener);
        return builder.create();
    }
}
