package com.example.adminshop;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class Discounts extends PinSupportNetworkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discounts_list);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		setupListViewAdapter();
	}

	@Override
	protected void withoutPinAction() {
		if (isNeedDownload()) {
			updateDiscountsTable();
		}
		super.withoutPinAction();
	}

	private void updateDiscountsTable() {
		clearList();
		progressBar.setVisibility(View.VISIBLE);
		GetRequester dataRequester = new GetRequester() {
			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					try {
						JSONArray array = new JSONArray(result);
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							String id = obj.getString("discountid");
							String orderSubtotalString = obj.getString("minprice");
							String discountString = obj.getString("discount");
							String discountTypeString = obj.getString("discount_type");
							String membershipid = obj.getString("membershipid");
							String membership = null;
							if (membershipid.equals("none")) {
								membership = "All";
							} else if (membershipid.equals("1")) {
								membership = "Premium";
							} else {
								membership = "Wholesale";
							}
							addDiscountToList(id, orderSubtotalString, discountString, discountTypeString, membership);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					showConnectionErrorMessage();
				}
				progressBar.setVisibility(View.GONE);
				discountsListView.onRefreshComplete();
			}
		};

		dataRequester.execute("http://54.213.38.9/xcart/api.php?request=discounts");
	}

	private void addDiscountToList(final String id, final String subtotal, final String discount,
			final String discountType, final String membership) {
		adapter.add(new Discount(id, subtotal, discount, discountType, membership));
	}

	private void editClick(Discount itemToEdit) {
		Intent intent = new Intent(getBaseContext(), DiscountEditor.class);
		intent.putExtra("id", itemToEdit.getId());
		intent.putExtra("orderSub", itemToEdit.getOrderSubtotal());
		intent.putExtra("discount", itemToEdit.getDiscount());
		intent.putExtra("discountType", itemToEdit.getDiscountType());
		intent.putExtra("membership", itemToEdit.getMembership());
		startActivityForResult(intent, 1);
	}

	private void deleteClick(final String id) {
		LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.confirmation_dialog, null);
		((TextView) view.findViewById(R.id.confirm_question)).setText("Are you sure you want to delete this discount?");
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
				deleteDiscount(id);
			}
		});

		dialog.show();
	}

	private void deleteDiscount(String id) {
		String response;
		try {
			response = new GetRequester().execute("http://54.213.38.9/xcart/api.php?request=delete_discount&id=" + id)
					.get();
		} catch (Exception e) {
			response = null;
		}
		if (response != null) {
			Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
			updateDiscountsTable();
		} else {
			showConnectionErrorMessage();
		}
	}

	private void setupListViewAdapter() {
		adapter = new DiscountsListAdapter(this, R.layout.discount_item, new ArrayList<Discount>());
		discountsListView = (PullToRefreshListView) findViewById(R.id.discounts);

		discountsListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				updateDiscountsTable();
			}
		});

		discountsListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showActionDialog(((Discount) view.getTag()));
			}
		});

		discountsListView.setAdapter(adapter);
	}

	private void showActionDialog(final Discount item) {
		LinearLayout action_view = (LinearLayout) getLayoutInflater().inflate(R.layout.action_dialog, null);
		final CustomDialog dialog = new CustomDialog(this, action_view);

		ListView actionList = (ListView) action_view.findViewById(R.id.action_list);

		String[] actions = { "Edit", "Delete", "Cancel" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.action_item, R.id.textItem, actions);

		actionList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					editClick(item);
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

	private void clearList() {
		adapter.clear();
	}

	public void addNewDiscountClick(View v) {
		Intent intent = new Intent(this, DiscountAdder.class);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == noUpdateCode) {
			setNeedDownloadValue(false);
		}
	}

	private ProgressBar progressBar;
	private DiscountsListAdapter adapter;
	private PullToRefreshListView discountsListView;
	private final int noUpdateCode = 4;
}
