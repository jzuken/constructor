package com.example.adminshop;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class StatisticTable extends TableLayout {

	public StatisticTable(Context context) {
		super(context);
		this.context = context;
		this.currentPosition = 1;
		this.rowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.itemParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.itemParams.weight = 1;
	}

	public StatisticTable(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.currentPosition = 1;
		this.rowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.itemParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.itemParams.weight = 1;
	}

	public void addPositionToTable(String name, String quantity) {
		TableRow newRow = new TableRow(context);
		newRow.setLayoutParams(rowParams);
		if (currentPosition % 2 == 0) {
			newRow.setBackgroundColor(getResources().getColor(R.color.ghostwhite));
		}
		TextView newItem = newTableTextView(String.valueOf(currentPosition) + ". " + name, Gravity.LEFT, 10);
		TextView newItemQuantity = newTableTextView(quantity, Gravity.CENTER, 0);
		newRow.addView(newItem);
		newRow.addView(newItemQuantity);
		this.addView(newRow);
		currentPosition++;
	}

	public void clearTable() {
		this.removeViews(1, currentPosition - 1);
		currentPosition = 1;
	}

	private TextView newTableTextView(CharSequence text, int gravity, int paddingLeft) {
		TextView textView = new TextView(context);
		textView.setText(text);
		textView.setMaxWidth(convertPixelsToDip(100));
		textView.setGravity(gravity);
		textView.setLayoutParams(itemParams);
		textView.setPadding(paddingLeft, 0, 0, 0);
		return textView;
	}

	private int convertPixelsToDip(int px) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
	}

	private int currentPosition;
	private LayoutParams rowParams;
	private Context context;
	private TableRow.LayoutParams itemParams;
}
