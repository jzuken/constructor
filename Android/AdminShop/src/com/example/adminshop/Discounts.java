package com.example.adminshop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Discounts extends PinSupportNetworkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discounts_list);
		tenDp = convertPixelsToDip(10);
		fiveDp = convertPixelsToDip(5);
		discountsList = (LinearLayout) findViewById(R.id.discountsLinearLayout);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
	}

	@Override
	protected void withoutPinAction() {
		updateDiscountsTable();
	}

	private void updateDiscountsTable() {
		clearList();
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

	private void addTitle(ViewGroup view, int number) {
		TextView discount = new TextView(this);
		discount.setText("Discount" + String.valueOf(number));
		discount.setBackgroundColor(getResources().getColor(R.color.gray));
		discount.setTextSize(15f);
		discount.setPadding(tenDp, 0, 0, 0);
		LinearLayout.LayoutParams discountParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		discount.setLayoutParams(discountParams);
		view.addView(discount);
	}

	private void addDiscountToList(final String id, final String subtotal, final String discount,
			final String discountType, final String membership) {
		LinearLayout currentLayout = new LinearLayout(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		currentLayout.setLayoutParams(params);
		currentLayout.setOrientation(LinearLayout.VERTICAL);
		currentLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DiscountData.id = id;
				DiscountData.subtotal = subtotal;
				DiscountData.discount = discount;
				DiscountData.discountType = discountType;
				DiscountData.membership = membership;
				Intent intent = new Intent(getBaseContext(), DiscountDialog.class);
				startActivityForResult(intent, 1);
			}
		});
		addTitle(currentLayout, position);

		RelativeLayout subtotalLayout = new RelativeLayout(this);
		addTextToLeft(subtotalLayout, "Order subtotal:");
		addTextToRight(subtotalLayout, subtotal);
		currentLayout.addView(subtotalLayout);

		RelativeLayout discountLayout = new RelativeLayout(this);
		addTextToLeft(discountLayout, "Discount:");
		addTextToRight(discountLayout, discount);
		currentLayout.addView(discountLayout);

		RelativeLayout discountTypeLayout = new RelativeLayout(this);
		addTextToLeft(discountTypeLayout, "Discount type:");
		addTextToRight(discountTypeLayout, discountType);
		currentLayout.addView(discountTypeLayout);

		RelativeLayout membershipLayout = new RelativeLayout(this);
		addTextToLeft(membershipLayout, "Membership:");
		addTextToRight(membershipLayout, "All");
		currentLayout.addView(membershipLayout);

		RelativeLayout buttonsLayout = new RelativeLayout(this);
		buttonsLayout.setPadding(0, fiveDp, 0, fiveDp);
		currentLayout.addView(buttonsLayout);

		discountsList.addView(currentLayout);

		position++;
	}

	private void clearList() {
		discountsList.removeAllViews();
		position = 1;
	}

	public void addNewDiscountClick(View v) {
		Intent intent = new Intent(this, DiscountAdder.class);
		startActivityForResult(intent, 1);
	}

	private int convertPixelsToDip(int px) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 3:
			deleteDiscount();
			break;
		case 4:
			startEditDiscount();
			break;
		default:
			break;
		}
	}
	
	private void deleteDiscount() {
		String response;
		try {
			response = new GetRequester().execute(
					"http://54.213.38.9/xcart/api.php?request=delete_discount&id=" + DiscountData.id).get();
		} catch (Exception e) {
			response = null;
		}
		if (response != null) {
			Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
			updateDiscountsTable();
		} else {
			showConnectionErrorMessage();
		}
	}

	private void startEditDiscount() {
		Intent intent = new Intent(getBaseContext(), DiscountEditor.class);
		intent.putExtra("id", DiscountData.id);
		intent.putExtra("orderSub", DiscountData.subtotal);
		intent.putExtra("discount", DiscountData.discount);
		intent.putExtra("discountType", DiscountData.discountType);
		intent.putExtra("membership", DiscountData.membership);
		startActivityForResult(intent, 1);
	}

	private int tenDp;
	private int fiveDp;
	private LinearLayout discountsList;
	private int position = 1;
	private ProgressBar progressBar;

	private static class DiscountData {
		public static String id;
		public static String subtotal;
		public static String discount;
		public static String discountType;
		public static String membership;
	}
}
