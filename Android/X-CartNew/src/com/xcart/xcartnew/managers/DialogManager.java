package com.xcart.xcartnew.managers;

import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.xcart.xcartnew.views.dialogs.ErrorDialog;
import com.xcart.xcartnew.views.dialogs.NetworkErrorDialog;
import com.xcart.xcartnew.views.dialogs.ProgressDialog;

/**
 * Created by Nikita on 15.10.13.
 */
public class DialogManager {

    private FragmentManager fragmentManager;

    public DialogManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void showNetworkErrorDialog() {
        DialogFragment dialog = new NetworkErrorDialog();
        dialog.show(fragmentManager, "network_error");
    }

    public void showProgressDialog(int messageId, String dialogTag) {
        DialogFragment progressDialog = new ProgressDialog(messageId);
        progressDialog.setCancelable(false);
        progressDialog.show(fragmentManager, dialogTag);
    }

    public void dismissDialog(String key) {
        DialogFragment progressDialog = (DialogFragment)fragmentManager.findFragmentByTag(key);
        if (progressDialog != null) {
            progressDialog.dismissAllowingStateLoss();
        }
    }

    public void showErrorDialog(int messageId){
        showErrorDialog(messageId, null);
    }

    public void showErrorDialog(int messageId, DialogInterface.OnClickListener onClickListener) {
        DialogFragment dialog = new ErrorDialog(messageId, onClickListener);
        dialog.show(fragmentManager, "error");
    }
}
