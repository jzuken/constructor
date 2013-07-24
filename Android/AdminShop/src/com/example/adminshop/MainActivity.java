package com.example.adminshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends PinSupportActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void productsButtonClick(View v) {
		Intent intent = new Intent(this, Products.class);
		startActivityForResult(intent, 1);
	}

	public void discountsButtonClick(View v) {
		Intent intent = new Intent(this, Discounts.class);
		startActivityForResult(intent, 1);
	}

	public void dashboardButtonClick(View v) {
		Intent intent = new Intent(this, Dashboard.class);
		startActivityForResult(intent, 1);
	}

	public void logoutClick(View v) {
		SharedPreferences authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
		Editor editor = authorizationData.edit();
		editor.remove("logged");
		editor.commit();
		Intent intent = new Intent(this, Authorization.class);
		startActivity(intent);
		finish();
	}

	public void settingsClick(View v) {
		Intent intent = new Intent(this, Settings.class);
		startActivityForResult(intent, 1);
	}

	public void usersButtonClick(View v) {
		Intent intent = new Intent(this, Users.class);
		startActivityForResult(intent, 1);
	}

	public void reviewsButtonClick(View v) {
		Intent intent = new Intent(this, Reviews.class);
		startActivityForResult(intent, 1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivityForResult(new Intent(this, Settings.class), 1);
			return true;
		}
		return false;
	}
}
