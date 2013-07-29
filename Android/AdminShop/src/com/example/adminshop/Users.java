package com.example.adminshop;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;

public class Users extends PinSupportActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.users_search);
		registered = (TextView) findViewById(R.id.registered);
		online = (TextView) findViewById(R.id.online);

		EditText searchLine = (EditText) findViewById(R.id.usersSearchLine);
		searchLine.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					return true;
				}
				return false;
			}
		});
	}
	
	private TextView registered;
	private TextView online;
}