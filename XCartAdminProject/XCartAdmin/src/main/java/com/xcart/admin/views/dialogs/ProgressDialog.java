package com.xcart.admin.views.dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Nikita on 14.10.13.
 */
public class ProgressDialog extends DialogFragment {

    private int messageId;


    //This constructor mast have - android bug
    //http://stackoverflow.com/questions/14011808/why-use-newinstance-for-dialogfragment-instead-of-the-constructor
    public ProgressDialog() {
    }

    public ProgressDialog(int messageId) {
        this.messageId = messageId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.ProgressDialog progressDialog = new android.app.ProgressDialog(getActivity());
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        if (messageId == 0) {
            messageId = savedInstanceState.getInt("messageId", 0);
        }
        if (messageId != 0) {
            progressDialog.setMessage(getString(messageId));
        }
        return progressDialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("messageId", messageId);
        super.onSaveInstanceState(outState);
    }
}
