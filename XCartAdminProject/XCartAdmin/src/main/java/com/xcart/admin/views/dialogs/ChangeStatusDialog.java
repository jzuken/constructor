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
    private int startPosition;
    private String status;
    private String[] values;

    public ChangeStatusDialog(Callback callback, String status, String[] values, int position) {
        this.callback = callback;
        this.status = status;
        this.values = values;
        this.startPosition = position;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.change_status);
        builder.setSingleChoiceItems(values, startPosition, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
            }
        });
        builder.setNeutralButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (callback != null) {
                    callback.save(values[selectedPosition]);
                }
            }
        });
        return builder.create();
    }

    public interface Callback {
        public void save(String selectedStatus);
    }
}
