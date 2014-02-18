package com.xcart.admin.views.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.xcart.admin.R;

/**
 * Created by Nikita on 2/18/14.
 */
public class ChangeStatusDialog extends DialogFragment {

    public ChangeStatusDialog() {
    }

    private Callback callback;
    private int selectedPosition;

    public ChangeStatusDialog(Callback callback) {
        this.callback = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.change_status);
        builder.setSingleChoiceItems(R.array.statuses_array, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
            }
        });
        builder.setNeutralButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (callback != null) {
                    callback.save("");
                }
            }
        });
        return builder.create();
    }

    public interface Callback {
        public void save(String selectedStatus);
    }
}
