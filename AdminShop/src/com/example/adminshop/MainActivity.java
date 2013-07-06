package com.example.adminshop;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView siteName = (TextView) findViewById(R.id.loggedNameText);
        authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
        String name = authorizationData.getString("loggedSiteName", "");
        siteName.setText(name.toCharArray(), 0, name.length());
    }
    
    public void productsButtonClick(View v) {
    	Intent intent = new Intent(this, Products.class);
    	startActivity(intent);
    }
    
    public void ordersButtonClick(View v) {
    	Intent intent = new Intent(this, Orders.class);
    	startActivity(intent);
    }
    
    public void saleButtonClick(View v) {
    	Intent intent = new Intent(this, Sale.class);
    	startActivity(intent);
    }
    
    public void statisticButtonClick(View v) {
    	Intent intent = new Intent(this, Statistic.class);
    	startActivity(intent);
    }
    
    public void logoutClick(View v) {
    	Editor editor = authorizationData.edit();
    	editor.remove("logged");
    	editor.commit();
    	Intent intent = new Intent(this, Authorization.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	startActivity(intent);
    }
    
    public void usersButtonClick(View v) {
    	Intent intent = new Intent(this, Users.class);
    	startActivity(intent);
    }
    
    public void commentsButtonClick(View v) {
    	Intent intent = new Intent(this, Comments.class);
    	startActivity(intent);
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
			startActivity(new Intent(this, Settings.class));
			return true;
		}
		return false;
	}
	
	private SharedPreferences authorizationData;
}
