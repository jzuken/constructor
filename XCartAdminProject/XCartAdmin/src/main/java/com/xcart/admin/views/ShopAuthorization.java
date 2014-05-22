package com.xcart.admin.views;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.xcart.admin.R;
import com.xcart.admin.managers.DialogManager;
import com.xcart.admin.managers.LogManager;
import com.xcart.admin.managers.MyActivityManager;
import com.xcart.admin.managers.XCartApplication;
import com.xcart.admin.managers.gcm.GcmManager;
import com.xcart.admin.managers.network.HttpManager;

import org.json.JSONException;
import org.json.JSONObject;


public class ShopAuthorization extends FragmentActivity {

    private static final LogManager LOG = new LogManager(ShopAuthorization.class.getName());
    private Button loginButton;
    private boolean autoLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_authorization);
        shopUrl = (EditText) findViewById(R.id.shop_url);
        setupKeyEditText();
        loginButton = (Button) findViewById(R.id.shop_login_button);
        gcmManager = new GcmManager(this);
        //TODO: stub
        //shopUrl.setText("54.213.38.9");
        //authorizationKey.setText("MobileAdminApiKey");
        //shopUrl.setText("ec2-54-213-169-59.us-west-2.compute.amazonaws.com");
        //authorizationKey.setText("testKey");
        //shopUrl.setText("mobileadmin.x-cart.com");
        //authorizationKey.setText("FQMTED8L");
        shopUrl.setText("mobileadmin.x-cart.com");
        authorizationKey.setText("D7PMJ9SY");
        LOG.d("onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LOG.d("onStart");
        dialogManager = new DialogManager(getSupportFragmentManager());
        if (autoLogin) {
            okButtonClick(null);
            autoLogin = false;
        }
    }

    public void okButtonClick(View v) {
        loginButton.setEnabled(false);
        checkAuthorizationData();
    }


    public void barcodeButtonClick(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }

    private void checkAuthorizationData() {
        XCartApplication.getInstance().getPreferenceManager().saveAuth(authorizationKey.getText().toString(), shopUrl.getText().toString());

        dialogManager.showProgressDialog(R.string.checking, PROGRESS_DIALOG);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                HttpManager httpManager = new HttpManager();
                String response = httpManager.shopAuthorization(authorizationKey.getText().toString(), shopUrl.getText().toString());

                try {
                    JSONObject obj = new JSONObject(response);
                    String result = obj.getString("url");
                    String currency = httpManager.getCurrencyType(result);
                    if (currency != null) {
                        JSONObject currencyObj = new JSONObject(currency);
                        XCartApplication.getInstance().getPreferenceManager().saveCurrencyType(currencyObj.getString("General:currency_symbol"), currencyObj.getString("General:currency_format"));
                        LOG.d("currency " + currency);
                    }
                } catch (JSONException e) {
                    LOG.e(e.getMessage(), e);
                } catch (NullPointerException e) {
                    LOG.e(e.getMessage(), e);
                }

                return response;
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
                            Toast.makeText(ShopAuthorization.this, R.string.incorrect_key, Toast.LENGTH_SHORT).show();
                        } else if (result.equals("noShop")) {
                            Toast.makeText(ShopAuthorization.this, R.string.incorrect_shop_url, Toast.LENGTH_SHORT).show();
                        } else if (result.equals("ok") || result.equals("trial") || result.equals("expired")) {
                            if (result.equals("expired")) {
                                Toast.makeText(ShopAuthorization.this, R.string.subscription_expired, Toast.LENGTH_SHORT).show();
                                XCartApplication.getInstance().setExpired(true);
                            } else if (result.equals("trial")) {
                                Toast.makeText(ShopAuthorization.this, String.format(getString(R.string.subscription_trial), obj.getString("remains")), Toast.LENGTH_SHORT).show();
                            }
                            MyActivityManager.setIsActivitiesFoundState(true);
                            XCartApplication.getInstance().getPreferenceManager().saveShopUrl(obj.getString("url"));

                            Toast.makeText(ShopAuthorization.this, "Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ShopAuthorization.this, DashboardActivity.class);
                            startActivity(intent);
                            registerGcm();
                            finish();
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

    private void registerGcm() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && scanResult.getContents() != null) {
            LOG.d("scanResult: " + scanResult);
            Uri uri = Uri.parse(scanResult.getContents());
            if (uri.getQueryParameters("key").size() > 0) {
                shopUrl.setText(uri.getHost());
                authorizationKey.setText(uri.getQueryParameters("key").get(0));
                LOG.d("onActivityResult");
                autoLogin = true;
            }
        }
    }

    private static final String PROGRESS_DIALOG = "Shop_authorization_progress";
    private DialogManager dialogManager;
    private EditText authorizationKey;
    private EditText shopUrl;
    private GcmManager gcmManager;
}
