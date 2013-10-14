package com.xcart.xcartnew.views;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xcart.xcartnew.R;
import com.xcart.xcartnew.managers.network.DevServerApiManager;
import com.xcart.xcartnew.managers.network.PostRequester;
import com.xcart.xcartnew.managers.network.SubscriptionCallback;
import com.xcart.xcartnew.managers.network.SubscriptionStatus;
import com.xcart.xcartnew.views.dialogs.ConnectionErrorDialog;
import com.xcart.xcartnew.views.dialogs.ErrorDialog;
import com.xcart.xcartnew.views.dialogs.ProgressDialog;
import com.xcart.xcartnew.managers.LogManager;

public class Authorization extends FragmentActivity implements SubscriptionCallback {

    private static final LogManager LOG = new LogManager(Authorization.class.getSimpleName());

    private DialogFragment progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorization);
        authorizationLogin = (EditText) findViewById(R.id.authorizationLogin);
        authorizationPassword = (EditText) findViewById(R.id.authorizationPassword);

        authorizationLogin.setText("elengor91@gmail.com");
        authorizationPassword.setText("hgD4pH0");

        DevServerApiManager.getInstance().addSubscriptionCallback(this);
    }

    public void okButtonClick(View v) {
        checkSubscription();
    }

    private void checkSubscription() {
        progressDialog = new ProgressDialog(R.string.checking_subscription);
        progressDialog.setCancelable(false);
        progressDialog.show(getSupportFragmentManager(), "progress");

        //TODO: create url input
        DevServerApiManager.getInstance().checkSubscription("54.213.38.9");
    }

    private void trySignIn(List<NameValuePair> loginData) {
        progressDialog = new ProgressDialog(R.string.logging_in);
        progressDialog.setCancelable(false);

        //strange Android bug
        //progressDialog.show(getSupportFragmentManager(), "progress");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(progressDialog, "progress");
        transaction.commitAllowingStateLoss();

        PostRequester postReq = new PostRequester(loginData) {
            @Override
            protected void onPostExecute(String authResult) {
                super.onPostExecute(authResult);

                progressDialog.dismiss();

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
                                Intent intent = new Intent(Authorization.this, Dashboard.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Authorization.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    ErrorDialog errorDialog = new ConnectionErrorDialog();
                    errorDialog.show(getSupportFragmentManager(), "login_error");
                }
            }
        };
        postReq.execute("https://54.213.38.9/api/api2.php?request=login");
    }


    DialogInterface.OnClickListener finishListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Authorization.this.finish();
        }
    };

    @Override
    public void onSubscriptionChecked(SubscriptionStatus status) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

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
