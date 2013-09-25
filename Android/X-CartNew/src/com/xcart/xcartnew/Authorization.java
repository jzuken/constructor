package com.xcart.xcartnew;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Authorization extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authorization);
		authorizationLogin = (EditText) findViewById(R.id.authorizationLogin);
		authorizationPassword = (EditText) findViewById(R.id.authorizationPassword);

		authorizationLogin.setText("elengor91@gmail.com");
		authorizationPassword.setText("hgD4pH0");
	}

	public void okButtonClick(View v) {

		String androidId = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
		String login = authorizationLogin.getText().toString();
		String password = authorizationPassword.getText().toString();

		List<NameValuePair> loginData = new ArrayList<NameValuePair>(3);
		loginData.add(new BasicNameValuePair("name", login));
		loginData.add(new BasicNameValuePair("pass", password));
		loginData.add(new BasicNameValuePair("udid", androidId));

		trySignIn(loginData);
	}

	private void trySignIn(List<NameValuePair> loginData) {
		PostRequester postReq = new PostRequester(loginData);
		String authResult = null;
		try {
			authResult = postReq.execute("https://54.213.38.9/xcart/api.php?request=login").get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (authResult != null) {
			if (authResult.equals("")) {
				Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
			} else {
				try {
					JSONObject obj = new JSONObject(authResult);
					if (obj.optString("upload_status").equals("login success")) {
						SharedPreferences authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
						Editor editor = authorizationData.edit();
						editor.putBoolean("logged", true);
						editor.putString("sid", obj.getString("sid"));
						editor.commit();

						Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(this, Dashboard.class);
						startActivity(intent);
					} else {
						Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} else {
			Toast.makeText(getBaseContext(),
					"Sorry, unable to connect to server. Please check your internet connection", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private EditText authorizationLogin;
	private EditText authorizationPassword;
}
