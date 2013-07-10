package com.example.adminshop;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Discounts extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discounts);
		tenDp = convertPixelsToDip(10);

		LayoutInflater inflater = LayoutInflater.from(this);
		List<View> pages = new ArrayList<View>();

		View page1 = inflater.inflate(R.layout.discounts_list, null);
		LinearLayout layout = (LinearLayout) page1.findViewById(R.id.discountsLinearLayout);

		addTitle(layout, 1);

		RelativeLayout rl = new RelativeLayout(this);
		RelativeLayout.LayoutParams rpLeft = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		rpLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rpLeft.addRule(RelativeLayout.CENTER_VERTICAL);
		rpLeft.setMargins(tenDp, 0, 0, 0);
		RelativeLayout.LayoutParams rpRight = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		rpRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rpRight.setMargins(0, 0, tenDp, 0);

		TextView textView1 = new TextView(this);
		textView1.setText("Order subtotal:");
		textView1.setTextSize(12f);
		textView1.setLayoutParams(rpLeft);
		rl.addView(textView1);

		EditText textEdit1 = new EditText(this);
		textEdit1.setMinimumWidth(convertPixelsToDip(100));
		textEdit1.setTextSize(12f);
		textEdit1.setText("0.00");
		textEdit1.setLayoutParams(rpRight);
		textEdit1.setFocusable(false);
		textEdit1.setClickable(false);
		textEdit1.setFocusableInTouchMode(false);
		textEdit1.setCursorVisible(false);
		rl.addView(textEdit1);

		layout.addView(rl);

		addText(layout, "Discount:");
		addText(layout, "Discount type:");
		addText(layout, "Membership:");

		pages.add(page1);
		View page2 = inflater.inflate(R.layout.add_discount, null);
		allButton = (RadioButton) page2.findViewById(R.id.allRadioButton);
		allButton.setChecked(true);
		premiumButton = (RadioButton) page2.findViewById(R.id.premiumRadioButton);
		wholesaleButton = (RadioButton) page2.findViewById(R.id.wholesaleRadioButton);
		percentButton = (RadioButton) page2.findViewById(R.id.percentRadioButton);
		percentButton.setChecked(true);
		absoluteButton = (RadioButton) page2.findViewById(R.id.absoluteRadioButton);
		orderSubtotalEditor = (EditText) page2.findViewById(R.id.orderSubtotalEditor);
		discountEditor = (EditText) page2.findViewById(R.id.discountEditor);
		pages.add(page2);

		SwipingPagerAdapter pagerAdapter = new SwipingPagerAdapter(pages);
		ViewPager viewPager = (ViewPager) findViewById(R.id.discounts_view_pager);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(0);
	}

	public void addText(ViewGroup view, CharSequence text) {
		TextView textView = new TextView(this);
		textView.setText(text);
		textView.setTextSize(12f);
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		textParams.setMargins(tenDp, 0, 0, 0);
		textView.setLayoutParams(textParams);
		view.addView(textView);
	}

	public void addTitle(ViewGroup view, int number) {
		TextView discount = new TextView(this);
		discount.setText("Discount" + String.valueOf(number));
		discount.setBackgroundColor(getResources().getColor(R.color.orange));
		discount.setTextSize(15f);
		discount.setPadding(tenDp, 0, 0, 0);
		LinearLayout.LayoutParams discountParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		discount.setLayoutParams(discountParams);
		view.addView(discount);
	}

	public int convertPixelsToDip(int px) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
	}

	public void settingsClick(View v) {
		Intent intent = new Intent(this, Settings.class);
		startActivity(intent);
	}

	public void backClick(View v) {
		this.finish();
	}

	public void okClick(View v) {
		if (Float.valueOf(discountEditor.getText().toString()) > 100.0 && percentButton.isChecked()) {
			Toast.makeText(getBaseContext(), "New discount can not be added with discount value more than 100%",
					Toast.LENGTH_SHORT).show();
		}
	}

	private RadioButton allButton;
	private RadioButton premiumButton;
	private RadioButton wholesaleButton;
	private RadioButton percentButton;
	private RadioButton absoluteButton;
	private EditText orderSubtotalEditor;
	private EditText discountEditor;
	private int tenDp;
}
