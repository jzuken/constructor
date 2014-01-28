package com.xcart.admin.views.dialogs;

/**
 * Created by Nikita on 1/28/14.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xcart.admin.R;
import com.xcart.admin.managers.XCartApplication;

public class AboutDialog extends AlertDialog {

    private static final String LINK_PATTERN = "<a href='%s'> %s </a>";

    public AboutDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setIcon(R.drawable.ic_launcher);
        String versionName = XCartApplication.getInstance().getApplicationVersionName();
        setTitle(getContext().getString(R.string.about_application_version, versionName));

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View aboutContentView = inflater.inflate(R.layout.about_dialog, null);
        setView(aboutContentView);

        TextView mobileAdminTv = ((TextView) aboutContentView.findViewById(R.id.xcart_mobile_admin));
        mobileAdminTv.setMovementMethod(LinkMovementMethod.getInstance());
        mobileAdminTv.setText(Html.fromHtml(String.format(LINK_PATTERN, getContext().getString(R.string.xcart_mobile_admin_link), getContext().getString(R.string.xcart_mobile_admin))));

        TextView helpTv = ((TextView) aboutContentView.findViewById(R.id.xcart_help));
        helpTv.setMovementMethod(LinkMovementMethod.getInstance());
        helpTv.setText(Html.fromHtml(String.format(LINK_PATTERN, getContext().getString(R.string.xcart_help_link), getContext().getString(R.string.xcart_help))));

        TextView emailTv = ((TextView) aboutContentView.findViewById(R.id.about_email));
        emailTv.setMovementMethod(LinkMovementMethod.getInstance());
        emailTv.setText(Html.fromHtml(String.format(LINK_PATTERN, getContext().getString(R.string.xcart_email_link), getContext().getString(R.string.about_email_text))));

        TextView tvFacebook = ((TextView) aboutContentView.findViewById(R.id.fb));
        tvFacebook.setMovementMethod(LinkMovementMethod.getInstance());
        tvFacebook.setText(Html.fromHtml(String.format(LINK_PATTERN, getContext().getString(R.string.fb_link), getContext().getString(R.string.facebook))));

        TextView tvTwitter = ((TextView) aboutContentView.findViewById(R.id.tw));
        tvTwitter.setMovementMethod(LinkMovementMethod.getInstance());
        tvTwitter.setText(Html.fromHtml(String.format(LINK_PATTERN, getContext().getString(R.string.tw_link), getContext().getString(R.string.twitter))));

        setButton(BUTTON_NEUTRAL, getContext().getString(R.string.about_exit_button_text),
                new OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        setCancelable(true);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttachedToWindow() {
    }
}