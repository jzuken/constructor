package com.example.adminshop;

import android.os.Bundle;
import android.widget.TextView;

public class Users extends PinSupportActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.users_search);
		registered = (TextView) findViewById(R.id.registered);
		online = (TextView) findViewById(R.id.online);
	}
	
	private TextView registered;
	private TextView online;
}