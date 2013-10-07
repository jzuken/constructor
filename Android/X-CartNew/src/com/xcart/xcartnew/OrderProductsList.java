package com.xcart.xcartnew;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderProductsList extends LinearLayout {

	public OrderProductsList(Context context) {
		super(context);
		inflater = ((Activity) context).getLayoutInflater();
		initListener();
	}

	public OrderProductsList(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflater = ((Activity) context).getLayoutInflater();
		initListener();
	}

	public void addItem(final String id, final String name, final String price) {
		if (this.getChildCount() > 0) {
			this.addView(inflater.inflate(R.layout.divider, null));
		}
		View item = inflater.inflate(R.layout.order_product_item, null);
		TextView productName = (TextView) item.findViewById(R.id.product_name);
		TextView productPrice = (TextView) item.findViewById(R.id.product_price);
		productName.setText(name);
		productPrice.setText(price);
		item.setTag(id);
		this.addView(item);
	}

	private void initListener() {
		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.getTag();
			}
		});
	}

	private LayoutInflater inflater;
}
