package com.example.adminshop;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class Orders extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orders);
		allDatesButton = (RadioButton) findViewById(R.id.all_dates_button);
		allDatesButton.setChecked(true);
		thisMonthButton = (RadioButton) findViewById(R.id.this_month_button);
		thisWeekButton = (RadioButton) findViewById(R.id.this_week_button);
		todayButton = (RadioButton) findViewById(R.id.today_button);
	}
	
	public void ordersSearchClick(View v) {
		
	}
	
	private RadioButton allDatesButton;
	private RadioButton thisMonthButton;
	private RadioButton thisWeekButton;
	private RadioButton todayButton;
}
