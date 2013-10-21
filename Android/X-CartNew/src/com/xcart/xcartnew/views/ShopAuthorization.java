package com.xcart.xcartnew.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xcart.xcartnew.R;
import com.xcart.xcartnew.managers.DialogManager;
import com.xcart.xcartnew.managers.network.GetRequester;
import com.xcart.xcartnew.managers.network.HttpManager;

public class ShopAuthorization extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_authorization);
		shopUrl = (EditText) findViewById(R.id.shop_url);
		authorizationKey = (EditText) findViewById(R.id.authorization_key);

		shopUrl.setText("54.213.38.9");
		authorizationKey.setText("testKey");

		dialogManager = new DialogManager(getSupportFragmentManager());
	}

	public void okButtonClick(View v) {
		checkAuthorizationData();
	}

	private void checkAuthorizationData() {
		dialogManager.showProgressDialog(R.string.checking, PROGRESS_DIALOG);
		try {
			new GetRequester() {
				@Override
				protected String doInBackground(Void... params) {
					return new HttpManager().shopAuthorization(shopUrl.getText().toString(), authorizationKey.getText()
							.toString());
				}

				@Override
				protected void onPostExecute(String response) {
					super.onPostExecute(response);

					if (!isActive) {
						return;
					}

					if (response != null) {
						if (isEmptyResponse(response)) {
							Toast.makeText(ShopAuthorization.this, "Incorrect url or key", Toast.LENGTH_SHORT).show();
						} else {
							SharedPreferences authorizationData = getSharedPreferences("AuthorizationData",
									MODE_PRIVATE);
							Editor editor = authorizationData.edit();
							editor.putBoolean("shop_logged", true);
							editor.commit();

							Toast.makeText(ShopAuthorization.this, "Success", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(ShopAuthorization.this, Authorization.class);
							startActivity(intent);
						}
					} else {
						Toast.makeText(ShopAuthorization.this,
								"Sorry, unable to connect to server. Please check your internet connection",
								Toast.LENGTH_SHORT).show();
					}

					dialogManager.dismissDialog(PROGRESS_DIALOG);
				}
			}.execute();
		} catch (Exception e) {
			dialogManager.dismissDialog(PROGRESS_DIALOG);
			e.printStackTrace();
		}
	}

	private boolean isEmptyResponse(String str) {
		return str.equals("{}");
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

	private static final String PROGRESS_DIALOG = "Shop_authorization_progress";
	private DialogManager dialogManager;
	private boolean isActive = false;
	private EditText shopUrl;
	private EditText authorizationKey;
}
