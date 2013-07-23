package com.example.adminshop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class Discounts extends PinSupportActivity {

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
		orderSubtotalEditor = (EditText) page2.findViewById(R.id.orderSubtotalEditor);
		discountEditor = (EditText) page2.findViewById(R.id.discountEditor);
		pages.add(page2);

		SwipingPagerAdapter pagerAdapter = new SwipingPagerAdapter(pages);
		ViewPager viewPager = (ViewPager) findViewById(R.id.discounts_view_pager);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(0);
	}

	@Override
	protected void withoutPinAction() {
		initDiscountsData();
	}

	private void initDiscountsData() {
		String data;
		try {
			data = new GetRequester().execute("http://54.213.38.9/xcart/api.php?request=discounts").get();
		} catch (Exception e) {
			data = null;
		}
		if (data != null) {
			try {
				JSONArray array = new JSONArray(data);
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					String orderSubtotalString = obj.getString("minprice");
					String discountString = obj.getString("discount");
					String discountTypeString = obj.getString("discount_type");
					addDiscountToList(orderSubtotalString, discountString, discountTypeString, "All");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			showConnectionErrorMessage();
		}
	}

	private void createNewDiscount() {
		String provider = "1";
		String orderSubtotalValue = orderSubtotalEditor.getText().toString();
		String discountValue = discountEditor.getText().toString();
		String discountTypeValue = getDiscountType();
		String response;
		try {
			response = new GetRequester().execute("http://54.213.38.9/xcart/api.php?request=create_discount&minprice="
					+ orderSubtotalValue + "&discount=" + discountValue + "&discount_type=" + discountTypeValue
					+ "&provider=" + provider).get();
		} catch (Exception e) {
			response = null;
		}
		if (response != null) {
			Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
			addDiscountToList(orderSubtotalValue, discountValue, discountTypeValue, "All");
		} else {
			showConnectionErrorMessage();
		}
	}

	private void showConnectionErrorMessage() {
		Toast.makeText(getBaseContext(),
				"Sorry, unable to connect to server. Cannot update data. Please check your internet connection",
				Toast.LENGTH_LONG).show();
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

	private void addDiscountToList(String subtotal, String discount, String discountType, String membership) {
		addTitle(discountsList, position);
		RelativeLayout subtotalLayout = new RelativeLayout(this);
		addTextToLeft(subtotalLayout, "Order subtotal:");
		// addNotEnableNumberToRight(subtotalLayout, String.valueOf(subtotal));

		addTextToRight(subtotalLayout, subtotal);

		discountsList.addView(subtotalLayout);
		RelativeLayout discountLayout = new RelativeLayout(this);
		addTextToLeft(discountLayout, "Discount:");
		// addNotEnableNumberToRight(discountLayout, String.valueOf(discount));

		addTextToRight(discountLayout, discount);

		discountsList.addView(discountLayout);
		RelativeLayout discountTypeLayout = new RelativeLayout(this);
		addTextToLeft(discountTypeLayout, "Discount type:");
		// addDiscountTypeSpinnerToRight(discountTypeLayout);

		addTextToRight(discountTypeLayout, discountType);

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

	private String getDiscountType() {
		if (percentButton.isChecked()) {
			return "percent";
		} else {
			return "absolute";
		}
	}

	public void okClick(View v) {
		if (Float.valueOf(discountEditor.getText().toString()) > 100.0 && percentButton.isChecked()) {
			Toast.makeText(getBaseContext(), "New discount can not be added with discount value more than 100%",
					Toast.LENGTH_LONG).show();
		} else if (Float.valueOf(discountEditor.getText().toString()) == 0f) {
			Toast.makeText(getBaseContext(), "New discount can not be added with empty discount value",
					Toast.LENGTH_LONG).show();
		} else {
			createNewDiscount();
		}
	}

	private RadioButton allButton;
	private RadioButton premiumButton;
	private RadioButton wholesaleButton;
	private RadioButton percentButton;
	private EditText orderSubtotalEditor;
	private EditText discountEditor;
	private int tenDp;
	private LinearLayout discountsList;
	private int position = 1;
}
