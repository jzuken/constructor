package com.xcart.xcartnew.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.xcart.xcartnew.R;

public class CustomTabHost extends TabHost {

	public CustomTabHost(Context context) {
		super(context);
	}

	public CustomTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param side = {-1, 0, 1}; 
	 * -1 - left tab; 
	 *  0 - middle tab; 
	 *  1 - right tab;
	 */
	@SuppressWarnings("deprecation")
	public void addEmptyTab(String tag, String indicator, int side) {
		TabHost.TabSpec tabSpec = this.newTabSpec(tag);
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View indicatorView = inflater.inflate(R.layout.tab_indicator, null);
		TextView tabText = (TextView) indicatorView.findViewById(R.id.tab_text);
		tabText.setText(indicator);
		tabText.setTextColor(Color.WHITE);
		LinearLayout tabLayout = (LinearLayout) indicatorView.findViewById(R.id.tab_layout);
		switch (side) {
		case -1:
			tabLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_left_tab_selector));
			break;
		case 0:
			tabLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_selector));
			break;
		case 1:
			tabLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_right_tab_selector));
			break;
		}
		tabSpec.setIndicator(indicatorView);
		tabSpec.setContent(R.id.emptyContent);
		this.addTab(tabSpec);
	}
	
	/**
	 * @param side = {-1, 1, 1}; 
	 * -1 - left tab; 
	 *  0 - middle tab; 
	 *  1 - right tab;
	 */
	@SuppressWarnings("deprecation")
	public void addEmptyTab(String tag, String indicator, int side, int left, int top, int right, int bottom) {
		TabHost.TabSpec tabSpec = this.newTabSpec(tag);
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View indicatorView = inflater.inflate(R.layout.tab_indicator, null);
		TextView tabText = (TextView) indicatorView.findViewById(R.id.tab_text);
		tabText.setText(indicator);
		tabText.setTextColor(Color.WHITE);
		tabText.setPadding(left, top, right, bottom);
		LinearLayout tabLayout = (LinearLayout) indicatorView.findViewById(R.id.tab_layout);
		switch (side) {
		case -1:
			tabLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_left_tab_selector));
			break;
		case 0:
			tabLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_selector));
			break;
		case 1:
			tabLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_right_tab_selector));
			break;
		}
		tabSpec.setIndicator(indicatorView);
		tabSpec.setContent(R.id.emptyContent);
		this.addTab(tabSpec);
	}

}
