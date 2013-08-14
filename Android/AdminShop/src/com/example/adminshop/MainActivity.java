package com.example.adminshop;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends PinSupportActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		LayoutInflater inflater = LayoutInflater.from(this);
		List<View> pages = new ArrayList<View>();
		
		View page1 = null;
		View page2 = null;
		SharedPreferences settingsData = PreferenceManager.getDefaultSharedPreferences(this);
		int firstPage = Integer.parseInt(settingsData.getString("screens_list", "0"));
		if (firstPage == 0) {
			page1 = inflater.inflate(R.layout.menu, null);
			page2 = inflater.inflate(R.layout.news, null);
		} else {
			page2 = inflater.inflate(R.layout.menu, null);
			page1 = inflater.inflate(R.layout.news, null);
		}

		pages.add(page1);
		pages.add(page2);
		
		SwipingPagerAdapter pagerAdapter = new SwipingPagerAdapter(pages);
		ViewPager viewPager = (ViewPager) findViewById(R.id.start_view_pager);
		viewPager.setAdapter(pagerAdapter);

		viewPager.setCurrentItem(0);
		currentPage = 0;

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				currentPage = position;
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
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

	private int currentPage;
}
