package com.xcart.admin.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xcart.admin.R;
import com.xcart.admin.managers.DialogManager;
import com.xcart.admin.managers.LogManager;
import com.xcart.admin.managers.gcm.GcmManager;
import com.xcart.admin.managers.network.HttpManager;
import com.xcart.admin.managers.network.Requester;

import org.json.JSONObject;


public class ShopAuthorization extends FragmentActivity {

    private static final LogManager LOG = new LogManager(ShopAuthorization.class.getName());
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_authorization);
        shopUrl = (EditText) findViewById(R.id.shop_url);
        setupKeyEditText();
        loginButton = (Button) findViewById(R.id.shop_login_button);
        dialogManager = new DialogManager(getSupportFragmentManager());
        gcmManager = new GcmManager(this);

        //TODO: stub
        shopUrl.setText("54.213.38.9");
        authorizationKey.setText("MobileAdminApiKey");
        //shopUrl.setText("ec2-54-213-169-59.us-west-2.compute.amazonaws.com");
        //authorizationKey.setText("testKey");
    }

    public void okButtonClick(View v) {
        loginButton.setEnabled(false);
        checkAuthorizationData();
    }

    private void checkAuthorizationData() {
        SharedPreferences authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
        Editor editor = authorizationData.edit();
        editor.putString("shop_key", authorizationKey.getText().toString());
        editor.putString("shop_name", shopUrl.getText().toString());
        editor.commit();

        dialogManager.showProgressDialog(R.string.checking, PROGRESS_DIALOG);
        new Requester() {
            @Override
            protected String doInBackground(Void... params) {
                return new HttpManager()
                        .shopAuthorization(authorizationKey.getText().toString(), shopUrl.getText().toString());
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);

                dialogManager.dismissDialog(PROGRESS_DIALOG);

                if (response != null) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        String result = obj.getString("api");
                        if (result.equals("wrongKey")) {
                            Toast.makeText(ShopAuthorization.this, "Incorrect key", Toast.LENGTH_SHORT).show();
                        } else if (result.equals("expired")) {
                            Toast.makeText(ShopAuthorization.this, "Subscription has expired", Toast.LENGTH_SHORT)
                                    .show();
                        } else if (result.equals("notSubscribed")) {
                            Toast.makeText(ShopAuthorization.this, "Not subscribed", Toast.LENGTH_SHORT).show();
                        } else if (result.equals("noShop")) {
                            Toast.makeText(ShopAuthorization.this, "Incorrect shop url", Toast.LENGTH_SHORT).show();
                        } else {
                            SharedPreferences authorizationData = getSharedPreferences("AuthorizationData",
                                    MODE_PRIVATE);
                            Editor editor = authorizationData.edit();
                            editor.putBoolean("shop_logged", true);
                            editor.putString("shop_api", result);
                            editor.commit();

                            Toast.makeText(ShopAuthorization.this, "Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ShopAuthorization.this, Dashboard.class);
                            startActivity(intent);
                            registerGCM();
                        }
                    } catch (Exception e) {
                        LOG.e(e.getMessage(), e);
                    } finally {
                        loginButton.setEnabled(true);
                    }
                } else {
                    dialogManager.showNetworkErrorDialog();
                    loginButton.setEnabled(true);
                }
            }
        }.execute();
    }

    private boolean isEmpty(String string) {
        return string.equals("");
    }

    private void registerGCM() {
        if (gcmManager != null) {
            String regid = gcmManager.getRegistrationId();

            if (isEmpty(regid)) {
                gcmManager.registerInBackground();
            }
        }
    }

    private void setupKeyEditText() {
        authorizationKey = (EditText) findViewById(R.id.authorization_key);
        authorizationKey.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    checkAuthorizationData();
                    return true;
                }
                return false;
            }
        });
    }

    private static final String PROGRESS_DIALOG = "Shop_authorization_progress";
    private DialogManager dialogManager;
    private EditText authorizationKey;
    private EditText shopUrl;
    private GcmManager gcmManager;
}
