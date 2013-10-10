package com.xcart.xcartnew;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class OrderInfo extends PinSupportNetworkActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_info);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		orderId = (TextView) findViewById(R.id.order_title);
		orderId.setText(getResources().getString(R.string.order_id_number) + getIntent().getStringExtra("orderId"));
		status = (TextView) findViewById(R.id.status);
		trackingNumber = (TextView) findViewById(R.id.tracking_number);
		paymentMethod = (TextView) findViewById(R.id.payment_method);
		deliveryMethod = (TextView) findViewById(R.id.delivery_method);
		customer = (TextView) findViewById(R.id.customer);
		shippingInfo = (TextView) findViewById(R.id.shipping_info);
		phone = (TextView) findViewById(R.id.phone);
		fax = (TextView) findViewById(R.id.fax);
		itemsList = (OrderProductsList) findViewById(R.id.items_list);
		
		//test
		itemsList.addItem("123", "Apple iPod touch 32 GB", "$299.00");
		itemsList.addItem("123", "Asus S56CA-WH31 15,6' Ultrabook", "$579.98");
		
		subtotal = (TextView) findViewById(R.id.subtotal);
		discount = (TextView) findViewById(R.id.discount);
		couponSaving = (TextView) findViewById(R.id.coupon_saving);
		shippingCost = (TextView) findViewById(R.id.shipping_cost);
		total = (TextView) findViewById(R.id.total);
	}

	ProgressBar progressBar;
	TextView orderId;
	TextView status;
	TextView trackingNumber;
	TextView paymentMethod;
	TextView deliveryMethod;
	TextView customer;
	TextView shippingInfo;
	TextView phone;
	TextView fax;
	OrderProductsList itemsList;
	TextView subtotal;
	TextView discount;
	TextView couponSaving;
	TextView shippingCost;
	TextView total;
}