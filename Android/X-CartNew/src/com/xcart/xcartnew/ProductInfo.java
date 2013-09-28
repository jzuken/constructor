package com.xcart.xcartnew;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProductInfo extends PinSupportNetworkActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_full_info);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		name = (TextView) findViewById(R.id.product_name);
		productImage = (ImageView) findViewById(R.id.product_image);
		description = (TextView) findViewById(R.id.description);
		fullDescription = (TextView) findViewById(R.id.full_description);
		fullDescriptionDivider = findViewById(R.id.full_description_divider);
		price = (TextView) findViewById(R.id.price);
		sold = (TextView) findViewById(R.id.sold);
		inStock = (TextView) findViewById(R.id.in_stock);
		availability = (TextView) findViewById(R.id.availability);
	}
	
	private ProgressBar progressBar;
	private TextView name;
	private ImageView productImage;
	private TextView description;
	private TextView fullDescription;
	private View fullDescriptionDivider;
	private TextView price;
	private TextView sold;
	private TextView inStock;
	private TextView availability;
}
