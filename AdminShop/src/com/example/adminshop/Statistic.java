package com.example.adminshop;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

public class Statistic extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = LayoutInflater.from(this);
		List<View> pages = new ArrayList<View>();

		View page = inflater.inflate(R.layout.statistic, null);
		pages.add(page);
		page = inflater.inflate(R.layout.statistic, null);
		pages.add(page);
		page = inflater.inflate(R.layout.statistic, null);
		pages.add(page);

		SwipingPagerAdapter pagerAdapter = new SwipingPagerAdapter(pages);
		ViewPager viewPager = new ViewPager(this);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(0);

		setContentView(viewPager);
	}
}