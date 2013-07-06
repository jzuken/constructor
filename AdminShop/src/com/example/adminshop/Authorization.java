package com.example.adminshop;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Authorization extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authorization);
	}
	
	public void okButtonClick(View v) {
		Intent intent = new Intent(this, MainActivity.class);
		SharedPreferences authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
		Editor editor = authorizationData.edit();
		editor.putBoolean("logged", true);
	    EditText siteName = (EditText) findViewById(R.id.authorizationLogin);
	    editor.putString("loggedSiteName", siteName.getText().toString());
	    editor.commit();
		startActivity(intent);
	}
	
}
