package com.xcart.xcartnew;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
		isVisibleFoolDescr = false;
		fullDescriptionDivider = findViewById(R.id.full_description_divider);
		price = (TextView) findViewById(R.id.price);
		sold = (TextView) findViewById(R.id.sold);
		inStock = (TextView) findViewById(R.id.in_stock);
		availability = (TextView) findViewById(R.id.availability);
		initFullDescrLable();
	}

	private void initFullDescrLable() {
		fullDescrLabel = (TextView) findViewById(R.id.full_description_label);
		fullDescrLabel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isVisibleFoolDescr) {
					fullDescription.setVisibility(View.VISIBLE);
					fullDescriptionDivider.setVisibility(View.VISIBLE);
					isVisibleFoolDescr = true;
				} else {
					fullDescription.setVisibility(View.GONE);
					fullDescriptionDivider.setVisibility(View.GONE);
					isVisibleFoolDescr = false;
				}
			}
		});
	}

	private ProgressBar progressBar;
	private TextView name;
	private ImageView productImage;
	private TextView description;
	private TextView fullDescription;
	boolean isVisibleFoolDescr;
	private View fullDescriptionDivider;
	private TextView price;
	private TextView sold;
	private TextView inStock;
	private TextView availability;
	private TextView fullDescrLabel;
}
