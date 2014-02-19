package com.xcart.admin.managers;

import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.xcart.admin.views.dialogs.AboutDialogFragment;
import com.xcart.admin.views.dialogs.ChangeStatusDialog;
import com.xcart.admin.views.dialogs.ErrorDialog;
import com.xcart.admin.views.dialogs.NetworkErrorDialog;
import com.xcart.admin.views.dialogs.ProgressDialog;

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
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(dialog, "network_error");
        transaction.commitAllowingStateLoss();
    }

    public void showProgressDialog(int messageId, String dialogTag) {
        DialogFragment progressDialog = new ProgressDialog(messageId);
        progressDialog.setCancelable(false);
        progressDialog.show(fragmentManager, dialogTag);
    }

    public void dismissDialog(String key) {
        DialogFragment progressDialog = (DialogFragment) fragmentManager.findFragmentByTag(key);
        if (progressDialog != null) {
            progressDialog.dismissAllowingStateLoss();
        }
    }

    public void showErrorDialog(int messageId) {
        showErrorDialog(messageId, null);
    }

    public void showErrorDialog(int messageId, DialogInterface.OnClickListener onClickListener) {
        DialogFragment dialog = new ErrorDialog(messageId, onClickListener);
        dialog.show(fragmentManager, "error");
    }

    public void showAboutDialog() {
        DialogFragment dialog = new AboutDialogFragment();
        dialog.show(fragmentManager, "about");
    }

    public void showStatusDialog(ChangeStatusDialog.Callback callback, String status) {
        DialogFragment dialog = new ChangeStatusDialog(callback, status);
        dialog.show(fragmentManager, "change status");
    }
}
