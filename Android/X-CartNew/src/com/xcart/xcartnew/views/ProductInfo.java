package com.xcart.xcartnew.views;

import java.util.ArrayList;
import java.util.List;

import org.jraf.android.backport.switchwidget.Switch;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xcart.xcartnew.R;
import com.xcart.xcartnew.managers.network.DownloadImageTask;
import com.xcart.xcartnew.managers.network.HttpManager;
import com.xcart.xcartnew.managers.network.Requester;
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
		initFullDescrLable();
		setupPriceItem();
		setupAvailabilitySwitch();
		setupVariantsSpinner();
		variantsLayout = (LinearLayout) findViewById(R.id.variants_layout);
		variantsDivider = findViewById(R.id.variants_divider);
		options = (TextView) findViewById(R.id.options);
		variantsArray = new JSONArray();
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

		requester = new Requester() {
			@Override
			protected String doInBackground(Void... params) {
				return new HttpManager(getBaseContext()).getProductInfo(productId);
			}

			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONObject obj = new JSONObject(result);
						description.loadDataWithBaseURL("", obj.getString("descr"), "text/html", "UTF-8", "");
						String fullDescriptionText = obj.getString("fulldescr");
						if (!fullDescriptionText.equals("")) {
							fullDescription.loadDataWithBaseURL("", fullDescriptionText, "text/html", "UTF-8", "");
							fullDescrLabel.setVisibility(View.VISIBLE);
						}
						price.setText("$" + obj.getString("price"));
						priceItem.setClickable(true);
						sold.setText(obj.getString("sales_stats"));
						inStock.setText(obj.getString("avail"));
						if (obj.getString("forsale").equals("Y")) {
							availabilitySwitch.setChecked(true);
						}
						isNeedAvailabilityChange = true;
						String imageUrl = obj.getString("image_url");
						if (!imageUrl.equals(NO_IMAGE_URL)) {
							new DownloadImageTask(productImage, progressBar).execute(imageUrl);
						} else {
							productImage.setImageDrawable(getResources().getDrawable(R.drawable.no_image));
							progressBar.setVisibility(View.GONE);
						}
						if (!obj.get("variants").equals(null)) {
							variantsArray = obj.getJSONArray("variants");
							for (int i = 0; i < variantsArray.length(); i++) {
								JSONObject variant = variantsArray.getJSONObject(i);
								variantsList.add(variant.getString("productcode"));
							}
							variantsAdapter.notifyDataSetChanged();
							showVariants();
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

		requester.execute();
	}

	private void clearData() {
		description.loadUrl("about:blank");
		fullDescription.loadUrl("about:blank");
		hideFullDescr();
		hideVariants();
		variantsList.clear();
		variantsAdapter.clear();
		price.setText("");
		sold.setText("");
		inStock.setText("");
		productImage.setImageResource(android.R.color.transparent);
		isNeedAvailabilityChange = false;
		availabilitySwitch.setChecked(false);
		priceItem.setClickable(false);
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

	private void hideVariants() {
		variantsLayout.setVisibility(View.GONE);
		variantsDivider.setVisibility(View.GONE);
	}

	private void showVariants() {
		variantsLayout.setVisibility(View.VISIBLE);
		variantsDivider.setVisibility(View.VISIBLE);
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
		priceItem.setClickable(false);
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
								dialogManager.showErrorDialog(R.string.price_error);
							}
						} catch (Exception e) {
							dialogManager.showErrorDialog(R.string.incorrect_input);
						}
					}
				});

				dialog.show();
			}
		});
	}

	private void setupVariantsSpinner() {
		variantsSpinner = (Spinner) findViewById(R.id.variants_spinner);
		variantsSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {	
				try {
					JSONObject variant = variantsArray.getJSONObject(position);
					price.setText("$" + variant.getString("price"));
					inStock.setText(variant.getString("avail"));				
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		variantsList = new ArrayList<String>();
		variantsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, variantsList);
		variantsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		variantsSpinner.setAdapter(variantsAdapter);
	}

	private void setNewPrice(final String newPrice) {
		try {
			new Requester() {
				@Override
				protected String doInBackground(Void... params) {
					return new HttpManager(getBaseContext()).updateProductPrice(productId, newPrice);
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

	private void setupAvailabilitySwitch() {
		availabilitySwitch = (Switch) findViewById(R.id.availability_switch);
		availabilitySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isNeedAvailabilityChange) {
					setNewAvailability(productId, isChecked);
				}
			}
		});
	}

	private void setNewAvailability(final String productId, final boolean available) {
		final String availability = available ? "1" : "2";

		try {
			new Requester() {
				@Override
				protected String doInBackground(Void... params) {
					return new HttpManager(getBaseContext()).changeAvailable(productId, availability);
				}

				@Override
				protected void onPostExecute(String response) {
					super.onPostExecute(response);
					if (response != null) {
						Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
					} else {
						showConnectionErrorMessage();
					}
				}
			}.execute();
		} catch (Exception e) {
			showConnectionErrorMessage();
		}
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
	private TextView fullDescrLabel;
	private RelativeLayout priceItem;
	private Switch availabilitySwitch;
	private boolean isNeedAvailabilityChange;
	private static final String NO_IMAGE_URL = "/xcart/default_image.gif";
	private ArrayAdapter<String> variantsAdapter;
	private Spinner variantsSpinner;
	private View variantsDivider;
	private LinearLayout variantsLayout;
	private List<String> variantsList;
	private TextView options;
	public static final int changePriceResultCode = 200;
	private JSONArray variantsArray;
}
