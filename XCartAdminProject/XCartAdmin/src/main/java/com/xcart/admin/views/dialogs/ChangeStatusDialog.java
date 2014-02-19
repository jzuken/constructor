package com.xcart.admin.views.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.xcart.admin.R;
import com.xcart.admin.model.OrderStatus;

/**
 * Created by Nikita on 2/18/14.
 */
public class ChangeStatusDialog extends DialogFragment {

    public ChangeStatusDialog() {
    }

    private Callback callback;
    private int selectedPosition;
    private String status;

    public ChangeStatusDialog(Callback callback, String status) {
        this.callback = callback;
        this.status = status;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.change_status);
        builder.setSingleChoiceItems(R.array.statuses_array, OrderStatus.valueOf(status).ordinal(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
            }
        });
        builder.setNeutralButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (callback != null) {
                    callback.save(OrderStatus.values()[selectedPosition].name());
                }
            }
        });
        return builder.create();
    }

    public interface Callback {
        public void save(String selectedStatus);
    }
}
