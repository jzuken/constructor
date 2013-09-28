package com.xcart.xcartnew;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.Html;
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

	@Override
	protected void withoutPinAction() {
		if (isNeedDownload()) {
			clearData();
			updateData();
		}
		super.withoutPinAction();
	}

	private void updateData() {
		progressBar.setVisibility(View.VISIBLE);
		GetRequester dataRequester = new GetRequester() {
			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONObject obj = new JSONObject(result);
						name.setText(obj.getString("product"));
						description.setText(Html.fromHtml(obj.getString("descr")));
						fullDescription.setText(Html.fromHtml(obj.getString("fulldescr")));
						price.setText("$" + obj.getString("list_price"));
						sold.setText(obj.getString("sales_stats"));
						String inStockString = obj.getString("avail");
						inStock.setText(inStockString);
						if (isAvailable(inStockString, obj.getString("low_avail_limit"))) {
							availability.setText("Available");
						} else {
							availability.setText("Not available");
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

		dataRequester.execute("http://54.213.38.9/api/api2.php?request=product_info&id="
				+ getIntent().getStringExtra("id"));
	}

	private boolean isAvailable(String inStock, String minStock) {
		return (Integer.parseInt(inStock) > Integer.parseInt(minStock));
	}

	private void clearData() {
		name.setText("");
		description.setText("");
		fullDescription.setText("");
		hideFullDescr();
		price.setText("");
		sold.setText("");
		inStock.setText("");
		availability.setText("");
	}

	private void initFullDescrLable() {
		fullDescrLabel = (TextView) findViewById(R.id.full_description_label);
		fullDescrLabel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isVisibleFoolDescr) {
					showFullDescr();
				} else {
					hideFullDescr();
				}
			}
		});
	}

	private void hideFullDescr() {
		fullDescription.setVisibility(View.GONE);
		fullDescriptionDivider.setVisibility(View.GONE);
		isVisibleFoolDescr = false;
	}

	private void showFullDescr() {
		fullDescription.setVisibility(View.VISIBLE);
		fullDescriptionDivider.setVisibility(View.VISIBLE);
		isVisibleFoolDescr = true;
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
