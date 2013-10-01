package com.xcart.xcartnew;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class ChangeStatus extends PinSupportNetworkActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_status);
		orderIdTitle = (TextView) findViewById(R.id.order_id_title);
		orderIdTitle.setText(getResources().getString(R.string.order_id_colon) + " "
				+ getIntent().getStringExtra("orderId"));
		notFinished = (RadioButton) findViewById(R.id.not_finished_button);
		notFinished.setChecked(true);
		queued = (RadioButton) findViewById(R.id.queued_button);
		processed = (RadioButton) findViewById(R.id.processed_button);
		complete = (RadioButton) findViewById(R.id.complete_button);
		declined = (RadioButton) findViewById(R.id.declined_button);
		failed = (RadioButton) findViewById(R.id.failed_button);
		backordered = (RadioButton) findViewById(R.id.backordered_button);
	}

	public void saveClick(View v) {
		onBackPressed();
	}

	private TextView orderIdTitle;
	private RadioButton notFinished;
	private RadioButton queued;
	private RadioButton processed;
	private RadioButton complete;
	private RadioButton declined;
	private RadioButton failed;
	private RadioButton backordered;
}
