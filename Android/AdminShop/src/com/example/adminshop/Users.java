package com.example.adminshop;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

public class Users extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.users);

		LayoutInflater inflater = LayoutInflater.from(this);
		List<View> pages = new ArrayList<View>();

		View page = inflater.inflate(R.layout.users_search, null);
		pages.add(page);

		SwipingPagerAdapter pagerAdapter = new SwipingPagerAdapter(pages);
		ViewPager viewPager = (ViewPager) findViewById(R.id.users_view_pager);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(0);
	}
	
	public void usersSearchClick(View v) {
		
	}

	public void settingsClick(View v) {
		Intent intent = new Intent(this, Settings.class);
		startActivity(intent);
	}
}