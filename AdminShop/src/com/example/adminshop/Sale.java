package com.example.adminshop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Sale extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sale);
		itemParams = new TableRow.LayoutParams();
		itemParams.setMargins(1, 1, 1, 1);
		table = (TableLayout) findViewById(R.id.saleTable);
		TableRow title = new TableRow(this);
		LayoutParams rowParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		title.setLayoutParams(rowParams);
		addTextView(title, "Order subtotal");
		addTextView(title, "Discount");
		addTextView(title, "Discount type");
		addTextView(title, "Membership");
		table.addView(title);

		// для проверки сделаем одну строку
		TableRow row = new TableRow(this);
		row.setLayoutParams(rowParams);

		addTextView(row, "100.00");
		addTextView(row, "30.00");
		addTextView(row, "Percents, %");
		addTextView(row, "Premium");

		Button editButton = new Button(this);
		editButton.setLayoutParams(itemParams);
		editButton.setText("Edit");
		final Sale thisObj = this;
		editButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(thisObj, EditSale.class);
				TableRow firstRow = (TableRow) table.getChildAt(1);
				intent.putExtra("orderSub", ((TextView) firstRow.getChildAt(0)).getText());
				intent.putExtra("discount", ((TextView) firstRow.getChildAt(1)).getText());
				intent.putExtra("discountType", ((TextView) firstRow.getChildAt(2)).getText());
				intent.putExtra("membership", ((TextView) firstRow.getChildAt(3)).getText());
				startActivity(intent);
			}
		});
		row.addView(editButton);

		table.addView(row);
	}

	public void addTextView(TableRow row, CharSequence text) {
		TextView textView = new TextView(this);
		textView.setText(text);
		textView.setTextSize(12);
		textView.setBackgroundColor(Color.WHITE);
		textView.setLayoutParams(itemParams);
		row.addView(textView);
	}

	public void addSaleClick(View v) {
		Intent intent = new Intent(this, SaleAdder.class);
		startActivity(intent);
	}

	private TableLayout table;
	private TableRow.LayoutParams itemParams;
}