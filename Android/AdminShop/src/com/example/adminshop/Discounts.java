package com.example.adminshop;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
		discountsList = (LinearLayout) page1.findViewById(R.id.discountsLinearLayout);
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

		// example
		addDiscountToList(30.0f, 10.0f, "Percent,  %", "All");
		addDiscountToList(50.0f, 20.0f, "Percent,  %", "All");
		addDiscountToList(20.0f, 5.0f, "Percent,  %", "All");
		addDiscountToList(100.0f, 30.0f, "Percent,  %", "All");
	}

	private void addTextToLeft(RelativeLayout rl, CharSequence text) {
		RelativeLayout.LayoutParams rpToLeft = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		rpToLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rpToLeft.addRule(RelativeLayout.CENTER_VERTICAL);
		rpToLeft.setMargins(tenDp, 0, 0, 0);

		TextView textView = new TextView(this);
		textView.setText(text);
		textView.setTextSize(12f);
		textView.setLayoutParams(rpToLeft);
		rl.addView(textView);
	}

	private void addTextToRight(RelativeLayout rl, CharSequence text) {
		RelativeLayout.LayoutParams rpToRight = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		rpToRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rpToRight.addRule(RelativeLayout.CENTER_VERTICAL);
		rpToRight.setMargins(0, 0, tenDp, 0);

		TextView textView = new TextView(this);
		textView.setText(text);
		textView.setTextSize(12f);
		textView.setLayoutParams(rpToRight);
		rl.addView(textView);
	}

	private void addNotEnableNumberToRight(RelativeLayout rl, CharSequence text) {
		RelativeLayout.LayoutParams rpToRight = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		rpToRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rpToRight.setMargins(0, 0, tenDp, 0);
		EditText textEdit = new EditText(this);
		textEdit.setMinimumWidth(convertPixelsToDip(100));
		textEdit.setTextSize(12f);
		textEdit.setText(text);
		textEdit.setLayoutParams(rpToRight);
		textEdit.setFocusable(false);
		textEdit.setClickable(false);
		textEdit.setLongClickable(false);
		textEdit.setFocusableInTouchMode(false);
		textEdit.setCursorVisible(false);
		textEdit.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		rl.addView(textEdit);
	}

	private void addDiscountTypeSpinnerToRight(RelativeLayout rl) {
		RelativeLayout.LayoutParams rpToRight = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		rpToRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rpToRight.setMargins(0, 0, tenDp, 0);
		Spinner discountTypeSpinner = new Spinner(this);
		String[] data = { "Percent, %", "Absolute, $" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		discountTypeSpinner.setAdapter(adapter);
		discountTypeSpinner.setPrompt("Date period");
		discountTypeSpinner.setSelection(0);
		discountTypeSpinner.setLayoutParams(rpToRight);
		discountTypeSpinner.setFocusable(false);
		discountTypeSpinner.setClickable(false);
		discountTypeSpinner.setLongClickable(false);
		discountTypeSpinner.setFocusableInTouchMode(false);
		discountTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		rl.addView(discountTypeSpinner);
	}

	private void addTitle(ViewGroup view, int number) {
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

	private void addDiscountToList(float subtotal, float discount, String discountType, String membership) {
		addTitle(discountsList, position);
		RelativeLayout subtotalLayout = new RelativeLayout(this);
		addTextToLeft(subtotalLayout, "Order subtotal:");
		// addNotEnableNumberToRight(subtotalLayout, String.valueOf(subtotal));

		addTextToRight(subtotalLayout, "0.00");

		discountsList.addView(subtotalLayout);
		RelativeLayout discountLayout = new RelativeLayout(this);
		addTextToLeft(discountLayout, "Discount:");
		// addNotEnableNumberToRight(discountLayout, String.valueOf(discount));

		addTextToRight(discountLayout, "0.00");

		discountsList.addView(discountLayout);
		RelativeLayout discountTypeLayout = new RelativeLayout(this);
		addTextToLeft(discountTypeLayout, "Discount type:");
		// addDiscountTypeSpinnerToRight(discountTypeLayout);

		addTextToRight(discountTypeLayout, "Percent, %");

		discountsList.addView(discountTypeLayout);
		RelativeLayout membershipLayout = new RelativeLayout(this);
		addTextToLeft(membershipLayout, "Membership:");

		addTextToRight(membershipLayout, "All");

		discountsList.addView(membershipLayout);
		position++;
	}

	private void enableNumber(EditText textEdit) {
		textEdit.setFocusable(true);
		textEdit.setClickable(true);
		textEdit.setLongClickable(true);
		textEdit.setFocusableInTouchMode(true);
		textEdit.setCursorVisible(true);
	}

	private void clearList() {
		discountsList.removeAllViews();
		position = 1;
	}

	private int convertPixelsToDip(int px) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
	}

	public void okClick(View v) {
		if (Float.valueOf(discountEditor.getText().toString()) > 100.0 && percentButton.isChecked()) {
			Toast.makeText(getBaseContext(), "New discount can not be added with discount value more than 100%",
					Toast.LENGTH_LONG).show();
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
	private LinearLayout discountsList;
	private int position = 1;
}
