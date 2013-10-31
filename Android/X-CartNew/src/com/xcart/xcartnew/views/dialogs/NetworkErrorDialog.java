package com.xcart.xcartnew.views.dialogs;

import android.content.DialogInterface;

/**
 * Created by Nikita on 10/31/13.
 */
public class NetworkErrorDialog extends ErrorDialog {

    public NetworkErrorDialog(int messageId, DialogInterface.OnClickListener onClickListener) {
        super(messageId, onClickListener);
    }
}
