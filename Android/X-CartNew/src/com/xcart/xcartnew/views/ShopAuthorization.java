package com.xcart.xcartnew.views;

import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.Toast;

import com.xcart.xcartnew.R;
import com.xcart.xcartnew.gcm.GCMManager;
import com.xcart.xcartnew.managers.DialogManager;
import com.xcart.xcartnew.managers.network.GetRequester;
import com.xcart.xcartnew.managers.network.HttpManager;

public class ShopAuthorization extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_authorization);
		shopUrl = (EditText) findViewById(R.id.shop_url);
		setupKeyEditText();
		// authorizationKey.setText("testKey");

		dialogManager = new DialogManager(getSupportFragmentManager());
		gcmManager = new GCMManager(this);
		if (gcmManager != null) {
			String regid = gcmManager.getRegistrationId();

			if (isEmpty(regid)) {
				gcmManager.registerInBackground();
			}
		}
	}

	public void okButtonClick(View v) {
		checkAuthorizationData();
	}

	private void checkAuthorizationData() {
		SharedPreferences authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
		Editor editor = authorizationData.edit();
		editor.putString("shop_key", authorizationKey.getText().toString());
		editor.commit();

		dialogManager.showProgressDialog(R.string.checking, PROGRESS_DIALOG);
		new GetRequester() {
			@Override
			protected String doInBackground(Void... params) {
				return new HttpManager()
						.shopAuthorization(authorizationKey.getText().toString(), shopUrl.getText().toString());
			}

			@Override
			protected void onPostExecute(String response) {
				super.onPostExecute(response);

				if (!isActive) {
					return;
				}

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
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(ShopAuthorization.this,
							"Sorry, unable to connect to server. Please check your internet connection",
							Toast.LENGTH_SHORT).show();
				}
			}
		}.execute();
	}

	@Override
	protected void onStart() {
		super.onStart();
		isActive = true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		isActive = false;
	}

	@Override
	protected void onPause() {
		super.onResume();
		dialogManager.dismissDialog(PROGRESS_DIALOG);
	}

	private boolean isEmpty(String string) {
		return string.equals("");
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
	private boolean isActive = false;
	private EditText authorizationKey;
	private EditText shopUrl;
	private GCMManager gcmManager;
}
