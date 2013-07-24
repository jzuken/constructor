package com.example.adminshop;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
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
		fiveDp = convertPixelsToDip(5);

		LayoutInflater inflater = LayoutInflater.from(this);
		List<View> pages = new ArrayList<View>();

		View page1 = inflater.inflate(R.layout.discounts_list, null);
		discountsList = (LinearLayout) page1.findViewById(R.id.discountsLinearLayout);
		progressBar = (ProgressBar) page1.findViewById(R.id.progress_bar);
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
		clearList();
		updateDiscountsTable();
	}

	private void createNewDiscount() {
		String provider = "1";
		String orderSubtotalValue = orderSubtotalEditor.getText().toString();
		String discountValue = discountEditor.getText().toString();
		String discountTypeValue = getDiscountType();
		String response;
		try {
			response = new GetRequester().execute(
					"http://54.213.38.9/xcart/api.php?request=create_discount&minprice=" + orderSubtotalValue
							+ "&discount=" + discountValue + "&discount_type=" + discountTypeValue + "&provider="
							+ provider).get();
		} catch (Exception e) {
			response = null;
		}
		if (response != null) {
			Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
			clearList();
			updateDiscountsTable();
		} else {
			showConnectionErrorMessage();
		}
	}

	private void updateDiscountsTable() {
		progressBar.setVisibility(View.VISIBLE);
		GetRequester dataRequester = new GetRequester() {
			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONArray array = new JSONArray(result);
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							String id = obj.getString("discountid");
							String orderSubtotalString = obj.getString("minprice");
							String discountString = obj.getString("discount");
							String discountTypeString = obj.getString("discount_type");
							addDiscountToList(id, orderSubtotalString, discountString, discountTypeString, "All");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					showConnectionErrorMessage();
				}
				progressBar.setVisibility(View.GONE);
			}
		};

		dataRequester.execute("http://54.213.38.9/xcart/api.php?request=discounts");
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

	private void addDiscountToList(String id, String subtotal, String discount, String discountType, String membership) {
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

		RelativeLayout buttonsLayout = new RelativeLayout(this);
		buttonsLayout.setPadding(0, fiveDp, 0, fiveDp);
		addDeleteButtonToLeft(buttonsLayout, id);
		discountsList.addView(buttonsLayout);

		position++;
	}

	private void enableNumber(EditText textEdit) {
		textEdit.setFocusable(true);
		textEdit.setClickable(true);
		textEdit.setLongClickable(true);
		textEdit.setFocusableInTouchMode(true);
		textEdit.setCursorVisible(true);
	}

	private void addDeleteButtonToLeft(RelativeLayout rl, final String id) {
		RelativeLayout.LayoutParams rpToLeft = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		rpToLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rpToLeft.setMargins(tenDp, 0, 0, 0);
		Button deleteButton = new Button(this);
		deleteButton.setText("Delete");
		deleteButton.setTextColor(Color.WHITE);
		deleteButton.setBackgroundResource(R.drawable.button);
		deleteButton.setLayoutParams(rpToLeft);

		deleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String response;
				try {
					response = new GetRequester().execute(
							"http://54.213.38.9/xcart/api.php?request=delete_discount&id=" + id).get();
				} catch (Exception e) {
					response = null;
				}
				if (response != null) {
					Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
					clearList();
					updateDiscountsTable();
				} else {
					showConnectionErrorMessage();
				}
			}
		});

		rl.addView(deleteButton);
	}

	private void addEditButtonToRight(RelativeLayout rl, final String id) {
		RelativeLayout.LayoutParams rpToRight = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		rpToRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rpToRight.setMargins(0, 0, tenDp, 0);
		Button editButton = new Button(this);
		editButton.setText("Edit");
		editButton.setTextColor(Color.WHITE);
		editButton.setBackgroundResource(R.drawable.button);
		editButton.setMinimumWidth(100);
		editButton.setLayoutParams(rpToRight);

		editButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});

		rl.addView(editButton);
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
	private int fiveDp;
	private LinearLayout discountsList;
	private int position = 1;
	private ProgressBar progressBar;
}
