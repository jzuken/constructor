package com.xcart.xcartnew.views;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xcart.xcartnew.R;
import com.xcart.xcartnew.managers.network.DownloadImageTask;
import com.xcart.xcartnew.managers.network.GetRequester;
import com.xcart.xcartnew.managers.network.HttpManager;
import com.xcart.xcartnew.views.dialogs.CustomDialog;

public class ProductInfo extends PinSupportNetworkActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_full_info);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		productId = getIntent().getStringExtra("id");
		name = (TextView) findViewById(R.id.product_name);
		name.setText(getIntent().getStringExtra("name"));
		productImage = (ImageView) findViewById(R.id.product_image);
		description = (WebView) findViewById(R.id.description);
		initDescriptionWebView(description);
		fullDescription = (WebView) findViewById(R.id.full_description);
		initDescriptionWebView(fullDescription);
		isVisibleFoolDescr = false;
		fullDescriptionDivider = findViewById(R.id.full_description_divider);
		sold = (TextView) findViewById(R.id.sold);
		inStock = (TextView) findViewById(R.id.in_stock);
		availability = (TextView) findViewById(R.id.availability);
		initFullDescrLable();
		setupPriceItem();
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
			protected String doInBackground(Void... params) {
				return new HttpManager(authorizationData.getString("sid", "")).getProductInfo(productId);
			}

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
						String imageUrl = obj.getString("url");
						if (!imageUrl.equals(NO_IMAGE_URL)) {
							new DownloadImageTask(productImage, progressBar).execute(imageUrl);
						} else {
							productImage.setImageDrawable(getResources().getDrawable(R.drawable.no_image));
							progressBar.setVisibility(View.GONE);
						}
					} catch (JSONException e) {
						e.printStackTrace();
						progressBar.setVisibility(View.GONE);
					}
				} else {
					showConnectionErrorMessage();
					progressBar.setVisibility(View.GONE);
				}
			}
		};

		setRequester(dataRequester);
		dataRequester.execute();
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
		productImage.setImageResource(android.R.color.transparent);
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

	private void setupPriceItem() {
		price = (TextView) findViewById(R.id.price);
		priceItem = (RelativeLayout) findViewById(R.id.price_item);
		final Context context = this;
		priceItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				priceItem.setClickable(false);
				LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.change_value_dialog, null);
				((TextView) view.findViewById(R.id.label)).setText(R.string.set_price);
				final EditText priceEditor = (EditText) view.findViewById(R.id.value_editor);
				String temp = price.getText().toString();
				final String oldPrice = temp.substring(1);
				priceEditor.setText(oldPrice);
				final CustomDialog dialog = new CustomDialog(context, view) {
					@Override
					public void dismiss() {
						priceItem.setClickable(true);
						super.dismiss();
					}
				};

				Button saveButton = (Button) view.findViewById(R.id.dialog_save_button);
				saveButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						hideKeyboard(priceEditor);
						String newPrice = priceEditor.getText().toString();
						try {
							Double price = Double.parseDouble(newPrice);
							if (price > 0) {
								dialog.dismiss();
								if (!newPrice.equals(oldPrice)) {
									setNewPrice(newPrice);
								}
							} else {
								Toast.makeText(getBaseContext(), "The price can not be zero", Toast.LENGTH_SHORT)
										.show();
							}
						} catch (Exception e) {
							Toast.makeText(getBaseContext(), "Incorrect input", Toast.LENGTH_SHORT).show();
						}
					}
				});

				dialog.show();
			}
		});
	}

	private void setNewPrice(final String newPrice) {
		try {
			new GetRequester() {
				@Override
				protected String doInBackground(Void... params) {
					return new HttpManager(authorizationData.getString("sid", "")).updateProductPrice(productId,
							newPrice);
				}

				@Override
				protected void onPostExecute(String response) {
					super.onPostExecute(response);
					if (response != null) {
						Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
						price.setText("$" + newPrice);
						Intent resultIntent = new Intent();
						resultIntent.putExtra("price", newPrice);
						setResult(changePriceResultCode, resultIntent);
					} else {
						showConnectionErrorMessage();
					}
				}
			}.execute();
		} catch (Exception e) {
			showConnectionErrorMessage();
		}
	}

	private void hideKeyboard(EditText edit) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
	}

	private ProgressBar progressBar;
	private String productId = "";
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
	private RelativeLayout priceItem;
	private static final String NO_IMAGE_URL = "http://54.213.38.9/xcart";
	public static final int changePriceResultCode = 200;
}
