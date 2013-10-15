package com.xcart.xcartnew.managers;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.xcart.xcartnew.views.dialogs.ProgressDialog;

/**
 * Created by Nikita on 15.10.13.
 */
public class DialogManager {

    private FragmentManager fragmentManager;

    public DialogManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void showProgressDialog(int messageId, String dialogTag) {
        DialogFragment progressDialog = new ProgressDialog(messageId);
        progressDialog.setCancelable(false);
        progressDialog.show(fragmentManager, dialogTag);
    }

    public void dismissDialog(String key) {
        ProgressDialog progressDialog = (ProgressDialog)fragmentManager.findFragmentByTag(key);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
