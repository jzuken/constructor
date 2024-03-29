package com.xcart.admin.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xcart.admin.R;
import com.xcart.admin.managers.DialogManager;
import com.xcart.admin.managers.LogManager;
import com.xcart.admin.managers.StatusConverter;
import com.xcart.admin.managers.XCartApplication;
import com.xcart.admin.managers.network.HttpManager;
import com.xcart.admin.managers.network.Requester;
import com.xcart.admin.model.OrderStatus;
import com.xcart.admin.views.dialogs.ChangeStatusDialog;
import com.xcart.admin.views.dialogs.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderInfo extends PinSupportNetworkActivity {

    public static final int CHANGE_STATUS_RESULT_CODE = 100;
    private static final LogManager LOG = new LogManager(OrderInfo.class.getName());
    private static final String PROGRESS_DIALOG = "progress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.order_info);
        orderIdValue = getIntent().getStringExtra("orderId");
        setTitle(getResources().getString(R.string.order_id_number) + getIntent().getStringExtra("orderId"));
        paymentMethod = (TextView) findViewById(R.id.payment_method);
        deliveryMethod = (TextView) findViewById(R.id.delivery_method);
        billingInfo = (TextView) findViewById(R.id.billing_info);
        shippingInfo = (TextView) findViewById(R.id.shipping_info);
        bPhone = (TextView) findViewById(R.id.b_phone);
        sPhone = (TextView) findViewById(R.id.s_phone);
        itemsList = (OrderProductsList) findViewById(R.id.items_list);
        customerNotes = (TextView) findViewById(R.id.customer_notes);
        subtotal = (TextView) findViewById(R.id.subtotal);
        discount = (TextView) findViewById(R.id.discount);
        couponSaving = (TextView) findViewById(R.id.coupon_saving);
        shippingCost = (TextView) findViewById(R.id.shipping_cost);
        paymentMethodSurcharge = (TextView) findViewById(R.id.payment_method_surcharge);
        total = (TextView) findViewById(R.id.total);
        setupCustomerItem();
        setupStatusItem();
        setupFulfilmentStatusItem();
        setupTrackingNumberItem();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.order_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void withoutPinAction() {
        if (isNeedDownload()) {
            clearData();
            updateData();
        }
        super.withoutPinAction();
    }

    private JSONObject obj = null;

    private void updateData() {
        setProgressBarIndeterminateVisibility(Boolean.TRUE);
        final String orderId = getIntent().getStringExtra("orderId");
        requester = new Requester() {

            @Override
            protected String doInBackground(Void... params) {
                return new HttpManager(getBaseContext()).getOrderInfo(orderId);
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    try {
                        obj = new JSONObject(result);

                        statusSymbol = obj.getString("status");
                        paymentStatus.setText(StatusConverter.getStatusBySymbol(getBaseContext(),
                                OrderStatus.valueOf(statusSymbol)));
                        paymentStatus.setTextColor(StatusConverter.getColorResourceBySymbol(getBaseContext(),
                                OrderStatus.valueOf(statusSymbol)));

                        //TODO:
                        fulfilmentSymbol = obj.getString("status");
                        fulfilmentStatus.setText(StatusConverter.getStatusBySymbol(getBaseContext(),
                                OrderStatus.valueOf(fulfilmentSymbol)));
                        fulfilmentStatus.setTextColor(StatusConverter.getColorResourceBySymbol(getBaseContext(),
                                OrderStatus.valueOf(fulfilmentSymbol)));

                        trackingNumber.setText(obj.getString("tracking"));
                        paymentMethod.setText(obj.getString("payment_method"));
                        deliveryMethod.setText(obj.getString("shipping"));
                        String title = obj.getString("title");
                        if (!title.equals("false")) {
                            title += " ";
                        } else {
                            title = "";
                        }
                        userName = title + obj.getString("firstname") + " " + obj.getString("lastname");
                        customer.setText(userName);
                        userId = obj.getString("userid");

                        String bTitle = obj.getString("b_title");
                        if (!bTitle.equals("false")) {
                            bTitle += " ";
                        } else {
                            bTitle = "";
                        }
                        billingInfo.setText(bTitle + obj.getString("b_firstname") + " " + obj.getString("b_lastname")
                                + "\n" + obj.getString("b_address") + obj.getString("b_city") + ", "
                                + obj.getString("b_state") + " " + obj.getString("b_zipcode") + "\n"
                                + obj.getString("b_country"));
                        bPhone.setText(obj.getString("b_phone"));

                        String sTitle = obj.getString("s_title");
                        if (!sTitle.equals("false")) {
                            sTitle += " ";
                        } else {
                            sTitle = "";
                        }
                        shippingInfo.setText(sTitle + obj.getString("s_firstname") + " " + obj.getString("s_lastname")
                                + "\n" + obj.getString("s_address") + obj.getString("s_city") + ", "
                                + obj.getString("s_state") + " " + obj.getString("s_zipcode") + "\n"
                                + obj.getString("s_country"));
                        sPhone.setText(obj.getString("s_phone"));
                        String customerNotesString = obj.getString("customer_notes");
                        if (customerNotesString.equals("")) {
                            customerNotes.setText(R.string.no_notes);
                        } else {
                            customerNotes.setText(customerNotesString);
                        }
                        String format = XCartApplication.getInstance().getPreferenceManager().getCurrencyFormat();
                        subtotal.setText(String.format(format, obj.getString("subtotal")));
                        discount.setText(String.format(format, obj.getString("discount")));
                        couponSaving.setText(String.format(format, obj.getString("coupon_discount")));
                        shippingCost.setText(String.format(format, obj.getString("shipping_cost")));
                        paymentMethodSurcharge.setText(String.format(format, obj.getString("payment_surcharge")));
                        total.setText(String.format(format, obj.getString("total")));
                        JSONArray products = obj.getJSONArray("details");
                        for (int i = 0; i < products.length(); i++) {
                            JSONObject detObj = products.getJSONObject(i);
                            String id = detObj.getString("productid");
                            String name = detObj.getString("product");
                            String price = detObj.getString("price");
                            String amount = detObj.getString("amount");
                            JSONArray optionsArray = detObj.optJSONArray("product_options");
                            if (!(optionsArray == null)) {
                                String options = getOptions(optionsArray);
                                itemsList.addItem(id, name, price, amount, options);
                            } else {
                                itemsList.addItem(id, name, price, amount);
                            }
                        }

                        statusItem.setClickable(true);
                        trackingNumberItem.setClickable(true);
                        customerItem.setClickable(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showConnectionErrorMessage();
                }
                setProgressBarIndeterminateVisibility(Boolean.FALSE);
            }
        };

        requester.execute();
    }

    private void clearData() {
        paymentStatus.setText("");
        fulfilmentStatus.setText("");
        trackingNumber.setText("");
        paymentMethod.setText("");
        deliveryMethod.setText("");
        customer.setText("");
        billingInfo.setText("");
        bPhone.setText("");
        shippingInfo.setText("");
        sPhone.setText("");
        itemsList.clearList();
        customerNotes.setText("");
        subtotal.setText("");
        discount.setText("");
        couponSaving.setText("");
        shippingCost.setText("");
        paymentMethodSurcharge.setText("");
        total.setText("");
        statusItem.setClickable(false);
        trackingNumberItem.setClickable(false);
        customerItem.setClickable(false);
    }

    private String getOptions(JSONArray optionsArray) {
        try {
            StringBuilder optionsBuilder = new StringBuilder();
            int optionsLength = optionsArray.length();
            for (int j = 0; j < optionsLength - 1; j++) {
                JSONObject option = optionsArray.getJSONObject(j);
                optionsBuilder.append(option.getString("class"));
                optionsBuilder.append(": ");
                optionsBuilder.append(option.getString("option_name"));
                optionsBuilder.append("\n");
            }
            JSONObject option = optionsArray.getJSONObject(optionsLength - 1);
            optionsBuilder.append(option.getString("class"));
            optionsBuilder.append(": ");
            optionsBuilder.append(option.getString("option_name"));
            return optionsBuilder.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setupStatusItem() {
        paymentStatus = (TextView) findViewById(R.id.paymentStatus);
        statusItem = (RelativeLayout) findViewById(R.id.status_item);
        statusItem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (XCartApplication.getInstance().isExpired()) {
                    DialogManager dm = new DialogManager(OrderInfo.this.getSupportFragmentManager());
                    dm.showErrorDialog(R.string.subscription_expired);
                    return;
                }
                setNeedDownloadValue(false);
                new DialogManager(OrderInfo.this.getSupportFragmentManager()).showStatusDialog(new ChangeStatusDialog.Callback() {
                    @Override
                    public void save(String selectedStatus) {
                        setNewStatus(selectedStatus);
                    }
                }, statusSymbol);
            }
        });
    }

   private void setupFulfilmentStatusItem() {
       fulfilmentStatus = (TextView) findViewById(R.id.fulfilmentStatus);
       fulfilmentStatusItem = (RelativeLayout) findViewById(R.id.fulfilment_status_item);
       fulfilmentStatusItem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (XCartApplication.getInstance().isExpired()) {
                    DialogManager dm = new DialogManager(OrderInfo.this.getSupportFragmentManager());
                    dm.showErrorDialog(R.string.subscription_expired);
                    return;
                }
                setNeedDownloadValue(false);
                new DialogManager(OrderInfo.this.getSupportFragmentManager()).showStatusDialog(new ChangeStatusDialog.Callback() {
                    @Override
                    public void save(String selectedStatus) {
                        setNewFulfilmentStatus(selectedStatus);
                    }
                }, fulfilmentSymbol);
            }
        });
    }

    private void setupTrackingNumberItem() {
        trackingNumber = (TextView) findViewById(R.id.tracking_number);
        trackingNumberItem = (RelativeLayout) findViewById(R.id.tracking_number_item);
        final Context context = this;
        trackingNumberItem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (XCartApplication.getInstance().isExpired()) {
                    DialogManager dm = new DialogManager(OrderInfo.this.getSupportFragmentManager());
                    dm.showErrorDialog(R.string.subscription_expired);
                    return;
                }
                trackingNumberItem.setClickable(false);
                LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.change_value_dialog, null);
                ((TextView) view.findViewById(R.id.label)).setText(R.string.change_tracking_number);
                InputFilter filter = new InputFilter() {
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                                               int dend) {
                        for (int i = start; i < end; i++) {
                            if (!Character.isLetterOrDigit(source.charAt(i))) {
                                return "";
                            }
                        }
                        return null;
                    }
                };
                final EditText numberEditor = (EditText) view.findViewById(R.id.value_editor);
                numberEditor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_FILTER
                        | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                numberEditor.setFilters(new InputFilter[]{filter, new InputFilter.AllCaps(),
                        new InputFilter.LengthFilter(16)});
                final String oldNumber = trackingNumber.getText().toString();
                numberEditor.setText(oldNumber);
                final CustomDialog dialog = new CustomDialog(context, view) {
                    @Override
                    public void dismiss() {
                        trackingNumberItem.setClickable(true);
                        super.dismiss();
                    }
                };

                Button saveButton = (Button) view.findViewById(R.id.dialog_save_button);
                saveButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideKeyboard(numberEditor);
                        String newNumber = numberEditor.getText().toString();
                        dialog.dismiss();
                        if (!newNumber.equals(oldNumber)) {
                            setNewTrackingNumber(newNumber);
                        }
                    }
                });

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        showKeyboard();
                        numberEditor.selectAll();
                    }
                });

                dialog.show();
            }
        });
    }

    private void setNewTrackingNumber(final String newNumber) {
        dialogManager.showProgressDialog(R.string.updating_tracking_number, PROGRESS_DIALOG);
        try {
            new Requester() {
                @Override
                protected String doInBackground(Void... params) {
                    return new HttpManager(getBaseContext()).changeTrackingNumber(orderIdValue, newNumber);
                }

                @Override
                protected void onPostExecute(String response) {
                    super.onPostExecute(response);
                    dialogManager.dismissDialog(PROGRESS_DIALOG);
                    if (response != null) {
                        Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
                        trackingNumber.setText(newNumber);
                    } else {
                        showConnectionErrorMessage();
                    }
                }
            }.execute();
        } catch (Exception e) {
            dialogManager.dismissDialog(PROGRESS_DIALOG);
            showConnectionErrorMessage();
        }
    }

    public void setNewStatus(final String selectedStatus) {
        dialogManager.showProgressDialog(R.string.updating_status, PROGRESS_DIALOG);
        try {
            new Requester() {
                @Override
                protected String doInBackground(Void... params) {
                    return new HttpManager(getBaseContext()).changeStatus(getIntent().getStringExtra("orderId"), selectedStatus);
                }

                @Override
                protected void onPostExecute(String response) {
                    super.onPostExecute(response);

                    dialogManager.dismissDialog(PROGRESS_DIALOG);

                    if (response != null) {
                        Toast.makeText(getBaseContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                        paymentStatus.setText(StatusConverter.getStatusBySymbol(OrderInfo.this, OrderStatus.valueOf(selectedStatus)));
                        paymentStatus.setTextColor(StatusConverter.getColorResourceBySymbol(getBaseContext(), OrderStatus.valueOf(selectedStatus)));
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("status", selectedStatus);
                        setResult(CHANGE_STATUS_RESULT_CODE, resultIntent);
                        statusSymbol = selectedStatus;
                    } else {
                        showConnectionErrorMessage();
                    }
                }
            }.execute();
        } catch (Exception e) {
            dialogManager.dismissDialog(PROGRESS_DIALOG);
            showConnectionErrorMessage();
        }
    }

    public void setNewFulfilmentStatus(final String selectedStatus) {
        dialogManager.showProgressDialog(R.string.updating_status, PROGRESS_DIALOG);
        try {
            new Requester() {
                @Override
                protected String doInBackground(Void... params) {
                    return new HttpManager(getBaseContext()).changeStatus(getIntent().getStringExtra("orderId"), selectedStatus);
                }

                @Override
                protected void onPostExecute(String response) {
                    super.onPostExecute(response);

                    dialogManager.dismissDialog(PROGRESS_DIALOG);

                    if (response != null) {
                        Toast.makeText(getBaseContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                        fulfilmentStatus.setText(StatusConverter.getStatusBySymbol(OrderInfo.this, OrderStatus.valueOf(selectedStatus)));
                        fulfilmentStatus.setTextColor(StatusConverter.getColorResourceBySymbol(getBaseContext(), OrderStatus.valueOf(selectedStatus)));
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("status", selectedStatus);
                        setResult(CHANGE_STATUS_RESULT_CODE, resultIntent);
                        fulfilmentSymbol = selectedStatus;
                    } else {
                        showConnectionErrorMessage();
                    }
                }
            }.execute();
        } catch (Exception e) {
            dialogManager.dismissDialog(PROGRESS_DIALOG);
            showConnectionErrorMessage();
        }
    }

    private void hideKeyboard(EditText edit) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void setupCustomerItem() {
        customer = (TextView) findViewById(R.id.customer);
        customerItem = (RelativeLayout) findViewById(R.id.customer_item);
        customerItem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setNeedDownloadValue(false);
                Intent intent = new Intent(getBaseContext(), UserInfo.class);
                intent.putExtra("userId", userId);
                intent.putExtra("userName", userName);
                if(userId.equals("0")){
                    try {
                        intent.putExtra("firstname", obj.getString("firstname"));
                        intent.putExtra("lastname", obj.getString("lastname"));
                        intent.putExtra("email", obj.getString("email"));
                        intent.putExtra("b_address", obj.getString("b_address"));
                        intent.putExtra("b_city", obj.getString("b_city"));
                        intent.putExtra("b_state", obj.getString("b_state"));
                        intent.putExtra("b_zipcode", obj.getString("b_zipcode"));
                        intent.putExtra("b_country", obj.getString("b_country"));
                        intent.putExtra("b_phone", obj.getString("b_phone"));
                        intent.putExtra("b_fax", obj.getString("b_fax"));

                    } catch (JSONException e) {
                        LOG.e(e.getMessage(), e);
                    }
                }
                startActivityForResult(intent, 1);
            }
        });
    }

    private String orderIdValue = "";
    private TextView paymentStatus;
    private TextView fulfilmentStatus;
    private TextView trackingNumber;
    private TextView paymentMethod;
    private TextView deliveryMethod;
    private TextView customer;
    private TextView shippingInfo;
    private TextView billingInfo;
    private TextView bPhone;
    private TextView sPhone;
    private OrderProductsList itemsList;
    private TextView customerNotes;
    private TextView subtotal;
    private TextView discount;
    private TextView couponSaving;
    private TextView shippingCost;
    private TextView paymentMethodSurcharge;
    private TextView total;
    private RelativeLayout statusItem;
    private RelativeLayout fulfilmentStatusItem;
    private RelativeLayout trackingNumberItem;
    private RelativeLayout customerItem;
    private String statusSymbol;
    private String fulfilmentSymbol;
    private String userId;
    private String userName;
}
