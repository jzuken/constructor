package com.xcart.xcartnew.dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Nikita on 14.10.13.
 */
public class ProgressDialog extends DialogFragment {

    private int messageId;

    public ProgressDialog(int messageId) {
        this.messageId = messageId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.ProgressDialog progressDialog = new android.app.ProgressDialog(getActivity());
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(messageId));
        return progressDialog;
    }
}
