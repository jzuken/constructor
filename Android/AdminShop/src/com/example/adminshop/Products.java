package com.example.adminshop;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class Products extends PinSupportNetworkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.products);
		SharedPreferences settingsData = PreferenceManager.getDefaultSharedPreferences(this);
		packAmount = Integer.parseInt(settingsData.getString("products_amount", "10"));
		setupListViewAdapter();
	}

	@Override
	protected void withoutPinAction() {
		clearList();
		updateProductsList();
	}

	private void updateProductsList() {
		progressBar.setVisibility(View.VISIBLE);
		isDownloading = true;
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
							String name = obj.getString("productcode") + " " + obj.getString("product");
							String available = obj.getString("avail");
							String sold = obj.getString("sales_stats");
							String price = obj.getString("list_price");
							addProductToList(id, name, available, sold, price);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					showConnectionErrorMessage();
				}
				progressBar.setVisibility(View.GONE);
				productsListView.onRefreshComplete();
				isDownloading = false;
			}
		};

		dataRequester.execute("http://54.213.38.9/xcart/api.php?request=products&search_word=" + searchWord + "&from="
				+ String.valueOf(currentAmount) + "&size=" + String.valueOf(packAmount));
		currentAmount += packAmount;
	}

	private void addProductToList(final String id, final String name, final String available, final String sold,
			final String price) {
		adapter.add(new Product(id, name, available, sold, price));
	}

	private void setupListViewAdapter() {
		adapter = new ProductsListAdapter(this, R.layout.product_item, new ArrayList<Product>());
		productsListView = (PullToRefreshListView) findViewById(R.id.products_list);
		LayoutInflater inflater = getLayoutInflater();

		View listHeader = inflater.inflate(R.layout.products_header, null, false);
		productsSearchLine = (EditText) listHeader.findViewById(R.id.productsSearchLine);
		setupSearchLineListener(productsSearchLine);
		productsListView.getRefreshableView().addHeaderView(listHeader, null, false);

		View listFooter = inflater.inflate(R.layout.on_demand_footer, null, false);
		progressBar = (ProgressBar) listFooter.findViewById(R.id.progress_bar);
		productsListView.getRefreshableView().addFooterView(listFooter, null, false);

		productsListView.getRefreshableView().setHeaderDividersEnabled(false);
		productsListView.getRefreshableView().setFooterDividersEnabled(false);

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

		productsListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				clearList();
				updateProductsList();
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

		String[] actions = { "Edit price", "Delete", "Cancel" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.action_item, R.id.textItem, actions);

		actionList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					editPriceClick(item.getId());
					dialog.dismiss();
					break;
				case 1:
					deleteClick(item.getId());
					dialog.dismiss();
					break;
				case 2:
					dialog.dismiss();
				default:
					break;
				}

			}
		});

		actionList.setAdapter(adapter);

		dialog.show();
	}

	private void editPriceClick(final String id) {
		LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.change_price_dialog, null);
		final EditText newPriceEditor = (EditText) view.findViewById(R.id.product_price_editor);
		final CustomDialog dialog = new CustomDialog(this, view);

		Button okButton = (Button) view.findViewById(R.id.dialog_ok_button);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideKeyboard(newPriceEditor);
				dialog.dismiss();
				setNewPrice(id, newPriceEditor.getText().toString());
			}
		});

		dialog.show();
	}

	private void setNewPrice(String id, String newPrice) {
		String response;
		try {
			response = new GetRequester().execute(
					"http://54.213.38.9/xcart/api.php?request=update_product&id=" + id + "&price=" + newPrice).get();
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
		Context context = getApplicationContext();
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
	}

	public void deleteClick(final String id) {
		LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.confirmation_dialog, null);
		((TextView) view.findViewById(R.id.confirm_question)).setText("Are you sure you want to delete this product?");
		final CustomDialog dialog = new CustomDialog(this, view);

		Button noButton = (Button) view.findViewById(R.id.dialog_no_button);
		noButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		Button yesButton = (Button) view.findViewById(R.id.dialog_yes_button);
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
			response = new GetRequester().execute("http://54.213.38.9/xcart/api.php?request=delete_product&id=" + id)
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

	private void setupSearchLineListener(EditText line) {
		line.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					searchWord = productsSearchLine.getText().toString();
					clearList();
					updateProductsList();
					return true;
				}
				return false;
			}
		});
	}

	private EditText productsSearchLine;
	private ProgressBar progressBar;
	private ProductsListAdapter adapter;
	private int currentAmount;
	private boolean isDownloading;
	private boolean hasNext;
	private int packAmount;
	private final int startItemCount = 4;
	private String searchWord = "";
	PullToRefreshListView productsListView;
}
