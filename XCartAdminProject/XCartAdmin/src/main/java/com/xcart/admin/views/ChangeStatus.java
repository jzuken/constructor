package com.xcart.admin.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xcart.admin.R;
import com.xcart.admin.managers.LogManager;
import com.xcart.admin.managers.network.HttpManager;
import com.xcart.admin.managers.network.Requester;
import com.xcart.admin.model.OrderStatus;

public class ChangeStatus extends PinSupportNetworkActivity {

    private static final LogManager LOG = new LogManager(PinSupportNetworkActivity.class.getName());

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
        saveButton = (Button) findViewById(R.id.save_button);
        activeButtonBySymbol(OrderStatus.valueOf(getIntent().getStringExtra("status")));
    }

    private void activeButtonBySymbol(OrderStatus symbol) {
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
        saveButton.setEnabled(false);
        dialogManager.showProgressDialog(R.string.updating_status, PROGRESS_DIALOG);
        final String statusSymbol = getSymbolStatus();
        try {
            new Requester() {
                @Override
                protected String doInBackground(Void... params) {
                    return new HttpManager(getBaseContext()).changeStatus(getIntent().getStringExtra("orderId"),
                            statusSymbol);
                }

                @Override
                protected void onPostExecute(String response) {
                    super.onPostExecute(response);

                    LOG.d("onPostExecute");
                    dialogManager.dismissDialog(PROGRESS_DIALOG);

                    if (response != null) {
                        Toast.makeText(getBaseContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("status", statusSymbol);
                        setResult(changeStatusResultCode, resultIntent);
                        finish();
                    } else {
                        showConnectionErrorMessage();
                        saveButton.setEnabled(true);
                    }
                }
            }.execute();
        } catch (Exception e) {
            dialogManager.dismissDialog(PROGRESS_DIALOG);
            showConnectionErrorMessage();
            saveButton.setEnabled(true);
        }
    }

    private TextView orderIdTitle;
    private RadioButton notFinished;
    private RadioButton queued;
    private RadioButton processed;
    private RadioButton complete;
    private RadioButton declined;
    private RadioButton failed;
    private RadioButton backordered;
    private Button saveButton;
    public static final int changeStatusResultCode = 100;
    private static final String PROGRESS_DIALOG = "Change_status_progress";
}
