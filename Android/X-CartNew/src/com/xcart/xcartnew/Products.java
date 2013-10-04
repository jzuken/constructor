package com.xcart.xcartnew;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Products extends PinSupportNetworkActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.products);
		setupListViewAdapter();
		String sortOption = getIntent().getStringExtra("sortOption");
		settingsData = PreferenceManager.getDefaultSharedPreferences(this);
		authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
	}

	@Override
	protected void withoutPinAction() {
		packAmount = Integer.parseInt(settingsData.getString("products_amount", "10"));
		if (isNeedDownload()) {
			clearList();
			updateProductsList();
		}
		super.withoutPinAction();
	}

	private void updateProductsList() {
		progressBar.setVisibility(View.VISIBLE);
		synchronized (lock) {
			isDownloading = true;
		}
		hasNext = false;
		GetRequester dataRequester = new GetRequester() {
			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONArray array = new JSONArray(result);
						int length = array.length();
						if (length == packAmount) {
							hasNext = true;
						}
						for (int i = 0; i < length; i++) {
							JSONObject obj = array.getJSONObject(i);
							String id = obj.getString("productid");
							String name = obj.getString("product");
							String productCode = obj.getString("productcode");
							String sku = productCode.substring(3, productCode.length());
							String inStock = obj.getString("avail");
							String price = obj.getString("list_price");
							addProductToList(id, name, sku, inStock, price);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					showConnectionErrorMessage();
				}
				progressBar.setVisibility(View.GONE);
				synchronized (lock) {
					isDownloading = false;
				}
			}
		};

		dataRequester.execute("https://54.213.38.9/api/api2.php?request=products&from="
				+ String.valueOf(currentAmount) + "&size=" + String.valueOf(packAmount) + "&sid="
				+ authorizationData.getString("sid", ""));
		currentAmount += packAmount;
	}

	private void addProductToList(final String id, final String name, final String sku, final String inStock,
			final String price) {
		adapter.add(new Product(id, name, sku, inStock, price));
	}

	private void setupListViewAdapter() {
		adapter = new ProductsListAdapter(this, R.layout.product_item, new ArrayList<Product>());
		productsListView = (ListView) findViewById(R.id.products_list);
		LayoutInflater inflater = getLayoutInflater();

		View listFooter = inflater.inflate(R.layout.on_demand_footer, null, false);
		progressBar = (ProgressBar) listFooter.findViewById(R.id.progress_bar);
		productsListView.addFooterView(listFooter, null, false);

		productsListView.setFooterDividersEnabled(false);

		productsListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (totalItemCount > startItemCount && firstVisibleItem + visibleItemCount == totalItemCount
						&& !isDownloading && hasNext) {
					updateProductsList();
				}
			}
		});

		productsListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showActionDialog(((Product) view.getTag()));
			}
		});

		productsListView.setAdapter(adapter);
	}

	private void showActionDialog(final Product item) {
		LinearLayout action_view = (LinearLayout) getLayoutInflater().inflate(R.layout.action_dialog, null);
		final CustomDialog dialog = new CustomDialog(this, action_view);

		ListView actionList = (ListView) action_view.findViewById(R.id.action_list);

		String[] actions = { "Full info", "Set price", "Remove", "Cancel" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.action_item, R.id.textItem, actions);

		actionList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					showFullInfo(item.getId());
					dialog.dismiss();
					break;
				case 1:
					editPriceClick(item.getId(), item.getPrice());
					dialog.dismiss();
					break;
				case 2:
					deleteClick(item.getId());
					dialog.dismiss();
					break;
				case 3:
					dialog.dismiss();
					break;
				default:
					break;
				}

			}
		});

		actionList.setAdapter(adapter);

		dialog.show();
	}

	private void showFullInfo(final String id) {
		setNeedDownloadValue(false);
		Intent intent = new Intent(this, ProductInfo.class);
		intent.putExtra("id", id);
		startActivityForResult(intent, 1);
	}

	private void editPriceClick(final String id, final String oldPrice) {
		LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.change_price_dialog, null);
		final EditText newPriceEditor = (EditText) view.findViewById(R.id.product_price_editor);
		newPriceEditor.setText(oldPrice);
		final CustomDialog dialog = new CustomDialog(this, view);

		ImageButton okButton = (ImageButton) view.findViewById(R.id.dialog_ok_button);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideKeyboard(newPriceEditor);
				String newPrice = newPriceEditor.getText().toString();
				try {
					Double price = Double.parseDouble(newPrice);
					if (price > 0) {
						dialog.dismiss();
						if (!newPrice.equals(oldPrice)) {
							setNewPrice(id, newPrice);
						}
					} else {
						Toast.makeText(getBaseContext(), "The price can not be zero", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Toast.makeText(getBaseContext(), "Incorrect input", Toast.LENGTH_SHORT).show();
				}
			}
		});

		dialog.show();
	}

	private void setNewPrice(String id, String newPrice) {
		String response;
		try {
			response = new GetRequester().execute(
					"https://54.213.38.9/xcart/api.php?request=update_product&id=" + id + "&price=" + newPrice).get();
		} catch (Exception e) {
			response = null;
		}
		if (response != null) {
			Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
			clearList();
			updateProductsList();
		} else {
			showConnectionErrorMessage();
		}
	}

	private void hideKeyboard(EditText edit) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
	}

	private void deleteClick(final String id) {
		LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.confirmation_dialog, null);
		((TextView) view.findViewById(R.id.confirm_question)).setText("Are you sure you want to remove this product?");
		final CustomDialog dialog = new CustomDialog(this, view);

		ImageButton noButton = (ImageButton) view.findViewById(R.id.dialog_no_button);
		noButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		ImageButton yesButton = (ImageButton) view.findViewById(R.id.dialog_yes_button);
		yesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				deleteProduct(id);
			}
		});

		dialog.show();
	}

	private void deleteProduct(String id) {
		String response;
		try {
			response = new GetRequester().execute("https://54.213.38.9/xcart/api.php?request=delete_product&id=" + id)
					.get();
		} catch (Exception e) {
			response = null;
		}
		if (response != null) {
			Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
			clearList();
			updateProductsList();
		} else {
			showConnectionErrorMessage();
		}
	}

	private void clearList() {
		adapter.clear();
		currentAmount = 0;
	}

	private ProgressBar progressBar;
	private ProductsListAdapter adapter;
	private int currentAmount;
	private boolean isDownloading;
	private boolean hasNext;
	private int packAmount;
	private final int startItemCount = 4;
	private String searchWord = "";
	private ListView productsListView;
	private Object lock = new Object();
	private SharedPreferences settingsData;
	private SharedPreferences authorizationData;
}
