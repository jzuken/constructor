package com.xcart.xcartnew;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
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
		name.setText(getIntent().getStringExtra("name"));
		productImage = (ImageView) findViewById(R.id.product_image);
		description = (WebView) findViewById(R.id.description);
		initDescriptionWebView(description);
		fullDescription = (WebView) findViewById(R.id.full_description);
		initDescriptionWebView(fullDescription);
		isVisibleFoolDescr = false;
		fullDescriptionDivider = findViewById(R.id.full_description_divider);
		price = (TextView) findViewById(R.id.price);
		sold = (TextView) findViewById(R.id.sold);
		inStock = (TextView) findViewById(R.id.in_stock);
		availability = (TextView) findViewById(R.id.availability);
		initFullDescrLable();	
		authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
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
						description.loadDataWithBaseURL("", obj.getString("descr"), "text/html", "UTF-8", "");
						fullDescription.loadDataWithBaseURL("", obj.getString("fulldescr"), "text/html", "UTF-8", "");
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

		dataRequester.execute("https://54.213.38.9/api/api2.php?request=product_info&id="
				+ getIntent().getStringExtra("id") + "&sid=" + authorizationData.getString("sid", ""));
	}

	private boolean isAvailable(String inStock, String minStock) {
		return (Integer.parseInt(inStock) > Integer.parseInt(minStock));
	}

	private void clearData() {
		description.loadUrl("about:blank");
		fullDescription.loadUrl("about:blank");
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

	@SuppressLint("SetJavaScriptEnabled")
	private void initDescriptionWebView(WebView descript) {
		WebSettings descriptionSettings = descript.getSettings();
		descriptionSettings.setJavaScriptEnabled(true);
		descriptionSettings.setDefaultFontSize(14);
	}

	private ProgressBar progressBar;
	private TextView name;
	private ImageView productImage;
	private WebView description;
	private WebView fullDescription;
	boolean isVisibleFoolDescr;
	private View fullDescriptionDivider;
	private TextView price;
	private TextView sold;
	private TextView inStock;
	private TextView availability;
	private TextView fullDescrLabel;
	private SharedPreferences authorizationData;
}
