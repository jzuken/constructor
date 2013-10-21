package com.xcart.xcartnew.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xcart.xcartnew.R;
import com.xcart.xcartnew.managers.DialogManager;
import com.xcart.xcartnew.managers.network.GetRequester;
import com.xcart.xcartnew.managers.network.HttpManager;

public class ChangeStatus extends PinSupportNetworkActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_status);
		orderIdTitle = (TextView) findViewById(R.id.order_id_title);
		orderIdTitle.setText(getResources().getString(R.string.order_id_number) + " "
				+ getIntent().getStringExtra("orderId"));
		notFinished = (RadioButton) findViewById(R.id.not_finished_button);
		notFinished.setChecked(true);
		queued = (RadioButton) findViewById(R.id.queued_button);
		processed = (RadioButton) findViewById(R.id.processed_button);
		complete = (RadioButton) findViewById(R.id.complete_button);
		declined = (RadioButton) findViewById(R.id.declined_button);
		failed = (RadioButton) findViewById(R.id.failed_button);
		backordered = (RadioButton) findViewById(R.id.backordered_button);
		activeButtonBySymbol(StatusSymbols.valueOf(getIntent().getStringExtra("status")));
		SharedPreferences authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
		sid = authorizationData.getString("sid", "");
		dialogManager = new DialogManager(getSupportFragmentManager());
	}

	private enum StatusSymbols {
		I, Q, P, B, D, F, C
	}

	private void activeButtonBySymbol(StatusSymbols symbol) {
		switch (symbol) {
		case I:
			notFinished.setChecked(true);
			break;
		case Q:
			queued.setChecked(true);
			break;
		case P:
			processed.setChecked(true);
			break;
		case B:
			backordered.setChecked(true);
			break;
		case D:
			declined.setChecked(true);
			break;
		case F:
			failed.setChecked(true);
			break;
		case C:
			complete.setChecked(true);
			break;
		default:
			notFinished.setChecked(true);
		}
	}

	private String getSymbolStatus() {
		if (notFinished.isChecked()) {
			return "I";
		} else if (queued.isChecked()) {
			return "Q";
		} else if (processed.isChecked()) {
			return "P";
		} else if (backordered.isChecked()) {
			return "B";
		} else if (declined.isChecked()) {
			return "D";
		} else if (failed.isChecked()) {
			return "F";
		} else {
			return "C";
		}
	}

	public void saveClick(View v) {
		dialogManager.showProgressDialog(R.string.updating_status, PROGRESS_DIALOG);
		try {
			new GetRequester() {
				@Override
				protected String doInBackground(Void... params) {
					return new HttpManager(sid).changeStatus(getIntent().getStringExtra("orderId"), getSymbolStatus());
				}

				@Override
				protected void onPostExecute(String response) {
					super.onPostExecute(response);

					if (!isActive) {
						return;
					}

					if (response != null) {
						Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
						Intent resultIntent = new Intent();
						resultIntent.putExtra("status", getSymbolStatus());
						setResult(changeStatusResultCode, resultIntent);
						onBackPressed();
					} else {
						showConnectionErrorMessage();
					}

					dialogManager.dismissDialog(PROGRESS_DIALOG);
				}
			}.execute();
		} catch (Exception e) {
			dialogManager.dismissDialog(PROGRESS_DIALOG);
			showConnectionErrorMessage();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		isActive = true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		isActive = false;
	}

	@Override
	protected void onPause() {
		super.onResume();
		dialogManager.dismissDialog(PROGRESS_DIALOG);
	}

	private TextView orderIdTitle;
	private RadioButton notFinished;
	private RadioButton queued;
	private RadioButton processed;
	private RadioButton complete;
	private RadioButton declined;
	private RadioButton failed;
	private RadioButton backordered;
	private String sid;
	public static final int changeStatusResultCode = 100;
	private DialogManager dialogManager;
	private static final String PROGRESS_DIALOG = "Change_status_progress";
	private boolean isActive = false;
}