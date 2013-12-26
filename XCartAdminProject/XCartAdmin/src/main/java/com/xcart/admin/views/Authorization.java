package com.xcart.admin.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xcart.admin.R;
import com.xcart.admin.managers.DialogManager;
import com.xcart.admin.managers.LogManager;
import com.xcart.admin.managers.network.DevServerApiManager;
import com.xcart.admin.managers.network.HttpManager;
import com.xcart.admin.managers.network.SubscriptionCallback;
import com.xcart.admin.managers.network.SubscriptionStatus;
import com.xcart.admin.views.dialogs.ConnectionErrorDialog;
import com.xcart.admin.views.dialogs.ErrorDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Authorization extends FragmentActivity implements SubscriptionCallback {

    private static final LogManager LOG = new LogManager(Authorization.class.getSimpleName());

    private boolean isActive = false;

    private static final String PROGRESS_DIALOG = "Authorization_progress";
    private DialogManager dialogManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorization);
        authorizationLogin = (EditText) findViewById(R.id.authorizationLogin);
        authorizationPassword = (EditText) findViewById(R.id.authorizationPassword);

        //authorizationLogin.setText("kuzma@x-cart.com");
        //authorizationPassword.setText("kuzmacdevru‏");

        authorizationLogin.setText("elengor91@gmail.com");
        authorizationPassword.setText("hgD4pH0");

        dialogManager = new DialogManager(getSupportFragmentManager());
    }

    @Override
    protected void onStart() {
        super.onStart();
        DevServerApiManager.getInstance().addSubscriptionCallback(this);
        isActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        DevServerApiManager.getInstance().removeSubscriptionCallback(this);
        isActive = false;
    }

    @Override
    protected void onPause() {
        super.onResume();
        dialogManager.dismissDialog(PROGRESS_DIALOG);
    }

    public void okButtonClick(View v) {
        checkSubscription();
    }

    private void checkSubscription() {
        dialogManager.showProgressDialog(R.string.checking_subscription, PROGRESS_DIALOG);

        SharedPreferences authorizationData = getSharedPreferences("AuthorizationData", Context.MODE_PRIVATE);
        String shopName = authorizationData.getString("shop_name", "");
        DevServerApiManager.getInstance().checkSubscription(shopName);
    }

    private void trySignIn(final List<NameValuePair> loginData) {
        dialogManager.showProgressDialog(R.string.logging_in, PROGRESS_DIALOG);

        AsyncTask<Void, Void, String> postReq = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return new HttpManager().login(loginData);
            }

            @Override
            protected void onPostExecute(String authResult) {
                super.onPostExecute(authResult);

                if (!isActive) {
                    return;
                }

                dialogManager.dismissDialog(PROGRESS_DIALOG);

                if (authResult != null) {
                    if (authResult.equals("")) {
                        ErrorDialog errorDialog = new ErrorDialog(R.string.login_error, null);
                        errorDialog.show(getSupportFragmentManager(), "login_error");
                    } else {
                        try {
                            JSONObject obj = new JSONObject(authResult);
                            if (obj.optString("upload_status").equals("login success")) {
                                SharedPreferences authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
                                Editor editor = authorizationData.edit();
                                editor.putBoolean("logged", true);
                                editor.putString("sid", obj.getString("sid"));
                                editor.commit();

                                Toast.makeText(Authorization.this, "Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Authorization.this, DashboardActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Authorization.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            LOG.e(e.getMessage(), e);
                        }
                    }
                } else {
                    ErrorDialog errorDialog = new ConnectionErrorDialog();
                    errorDialog.show(getSupportFragmentManager(), "login_error");
                }
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                dialogManager.dismissDialog(PROGRESS_DIALOG);
            }
        };

        postReq.execute();
    }


    DialogInterface.OnClickListener finishListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Authorization.this.finish();
        }
    };

    @Override
    public void onSubscriptionChecked(SubscriptionStatus status) {
        dialogManager.dismissDialog(PROGRESS_DIALOG);

        switch (status) {
            case Active:
                String androidId = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
                String login = authorizationLogin.getText().toString();
                String password = authorizationPassword.getText().toString();

                List<NameValuePair> loginData = new ArrayList<NameValuePair>(3);
                loginData.add(new BasicNameValuePair("name", login));
                loginData.add(new BasicNameValuePair("pass", password));
                loginData.add(new BasicNameValuePair("udid", androidId));

                trySignIn(loginData);
                break;
            case Expired:
                new ErrorDialog(R.string.subscription_expired, finishListener).show(getSupportFragmentManager(), "subscribed");
                break;
            case None:
                new ErrorDialog(R.string.no_subscription, finishListener).show(getSupportFragmentManager(), "subscribed");
                break;
            case NetworkError:
                new ConnectionErrorDialog().show(getSupportFragmentManager(), "subscription");
                break;
        }
    }

    private EditText authorizationLogin;
    private EditText authorizationPassword;
}
