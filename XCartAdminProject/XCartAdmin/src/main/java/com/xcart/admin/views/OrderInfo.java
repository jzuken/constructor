package com.xcart.admin.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.xcart.admin.managers.XCart5StatusConverter;
import com.xcart.admin.managers.XCartApplication;
import com.xcart.admin.managers.network.HttpManager;
import com.xcart.admin.managers.network.Requester;
import com.xcart.admin.views.dialogs.ChangeStatusDialog;
import com.xcart.admin.views.dialogs.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

public class OrderInfo extends PinSupportNetworkActivity {

    public static final int CHANGE_STATUS_RESULT_CODE = 100;
    private static final LogManager LOG = new LogManager(OrderInfo.class.getName());
    private static final String PROGRESS_DIALOG = "progress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderIdValue = getIntent().getStringExtra("orderId");
        if (orderIdValue == null) {
            orderIdValue = getIntent().getData().getQueryParameter("Number");
        }

        setContentView(R.layout.order_info);
        setTitle(getResources().getString(R.string.order_id_number) + orderIdValue);
        paymentMethod = (TextView) findViewById(R.id.payment_method);
        deliveryMethod = (TextView) findViewById(R.id.delivery_method);
        billingInfo = (TextView) findViewById(R.id.billing_info);
        shippingInfo = (TextView) findViewById(R.id.shipping_info);
        bPhone = (TextView) findViewById(R.id.b_phone);
        bFax = (TextView) findViewById(R.id.b_fax);
        sPhone = (TextView) findViewById(R.id.s_phone);
        sFax = (TextView) findViewById(R.id.s_fax);
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
        setupTrackingNumberItem();
        cameFromPPH = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        handlePassedData();
    }

    private void handlePassedData() {
        Uri data = getIntent().getData();

        if (data != null) {
            String host = data.getHost();

            if ("paymentResult".equals(host)) {
                cameFromPPH = true;

                final String orderNumber = data.getQueryParameter("Number");
                Set<String> params = data.getQueryParameterNames();
                final JSONObject jsonObject = new JSONObject();

                for (String name : params) {
                    try {
                        jsonObject.put(name, data.getQueryParameter(name));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    new Requester() {
                        @Override
                        protected String doInBackground(Void... params) {
                            return new HttpManager(getBaseContext()).changeStatusPPH(orderNumber, "Unknown", jsonObject.toString());
                        }

                        @Override
                        protected void onPostExecute(String response) {
                            super.onPostExecute(response);

                            if (response != null) {
                                updateData();
                            } else {
                                showConnectionErrorMessage();
                            }
                        }
                    }.execute();
                } catch (Exception e) {
                    showConnectionErrorMessage();
                }
            }
        }
    }


    ///

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
        final String orderId = orderIdValue;
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

                        String config = XCartApplication.getInstance().getPreferenceManager().getConfig();
                        JSONObject configObject = new JSONObject(config);

                        //TODO:
                        if (XCartApplication.getInstance().getPreferenceManager().getXCartVersion().equals("XCart4")) {
                            statusSymbol = obj.getString("status");
                            JSONArray statusesJsonArray = configObject.getJSONObject("Order:statuses").getJSONArray("status");
                            final XCart5StatusConverter statusesConverter = new XCart5StatusConverter(statusesJsonArray);
                            status.setText(statusesConverter.getStatusBySymbol(statusSymbol));

                            ArrayList<String> availStatuses = new ArrayList<String>();
                            int position = -1;
                            for (int i = 0; i < statusesJsonArray.length(); i++) {
                                JSONObject status = statusesJsonArray.getJSONObject(i);
                                if (status.getBoolean("avail")) {
                                    availStatuses.add(status.getString("name"));
                                    if (status.getString("code").equals(statusSymbol)) {
                                        position = i;
                                    }
                                }
                            }

                            String[] availStatusesArray = new String[availStatuses.size()];
                            availStatusesArray = availStatuses.toArray(availStatusesArray);

                            final int finalPosition = position;
                            final String[] finalAvailStatusesArray = availStatusesArray;
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
                                            setNewStatus(selectedStatus, statusesConverter.getSymbolByStatus(selectedStatus), "status");
                                        }
                                    }, statusSymbol, finalAvailStatusesArray, finalPosition);
                                }
                            });
                        } else {
                            paymentStatusSymbol = obj.getString("payment_status");
                            fulfilmentStatusSymbol = obj.getString("fulfilment_status");
                            JSONArray paymentStatusesJsonArray = configObject.getJSONObject("Order:statuses").getJSONArray("payment_status");
                            JSONArray fulfilmentStatusesJsonArray = configObject.getJSONObject("Order:statuses").getJSONArray("fulfilment_status");
                            final XCart5StatusConverter paymentStatusesConverter = new XCart5StatusConverter(paymentStatusesJsonArray);
                            final XCart5StatusConverter fulfilmentStatusesConverter = new XCart5StatusConverter(fulfilmentStatusesJsonArray);

                            paymentStatus.setText(paymentStatusesConverter.getStatusBySymbol(paymentStatusSymbol));
                            fulfilmentStatus.setText(fulfilmentStatusesConverter.getStatusBySymbol(fulfilmentStatusSymbol));

                            ArrayList<String> availPaymentStatuses = new ArrayList<String>();
                            int paymentPosition = -1;
                            for (int i = 0; i < paymentStatusesJsonArray.length(); i++) {
                                JSONObject status = paymentStatusesJsonArray.getJSONObject(i);
                                if (status.getBoolean("avail")) {
                                    availPaymentStatuses.add(status.getString("name"));
                                    if (status.getString("code").equals(paymentStatusSymbol)) {
                                        paymentPosition = i;
                                    }
                                }
                            }

                            String[] availPaymentStatusesArray = new String[availPaymentStatuses.size()];
                            availPaymentStatusesArray = availPaymentStatuses.toArray(availPaymentStatusesArray);

                            final int finalPaymentPosition = paymentPosition;
                            final String[] finalAvailPaymentStatusesArray = availPaymentStatusesArray;
                            paymentStatusItem.setOnClickListener(new OnClickListener() {

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
                                            setNewStatus(selectedStatus, paymentStatusesConverter.getSymbolByStatus(selectedStatus), "payment_status");
                                        }
                                    }, statusSymbol, finalAvailPaymentStatusesArray, finalPaymentPosition);
                                }
                            });

                            ArrayList<String> availFulfilmentStatuses = new ArrayList<String>();
                            int fulfilmentPosition = -1;
                            for (int i = 0; i < fulfilmentStatusesJsonArray.length(); i++) {
                                JSONObject status = fulfilmentStatusesJsonArray.getJSONObject(i);
                                if (status.getBoolean("avail")) {
                                    availFulfilmentStatuses.add(status.getString("name"));
                                    if (status.getString("code").equals(fulfilmentStatusSymbol)) {
                                        fulfilmentPosition = i;
                                    }
                                }
                            }

                            String[] availFulfilmentStatusesArray = new String[availFulfilmentStatuses.size()];
                            availFulfilmentStatusesArray = availFulfilmentStatuses.toArray(availFulfilmentStatusesArray);

                            final int finalFulfilmentPosition = fulfilmentPosition;
                            final String[] finalAvailFulfilmentStatusesArray = availFulfilmentStatusesArray;
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
                                            setNewStatus(selectedStatus, fulfilmentStatusesConverter.getSymbolByStatus(selectedStatus), "fulfilment_status");
                                        }
                                    }, statusSymbol, finalAvailFulfilmentStatusesArray, finalFulfilmentPosition);
                                }
                            });
                        }

                        trackingNumber.setText(obj.getString("tracking"));

                        String paymentMethodText = obj.getString("payment_method");
                        paymentMethod.setText(paymentMethodText);
                        if (paymentMethodText.equals("PayPal Here") && obj.has("pph_url")) {
                            paymentMethod.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        processByPayPalHere(obj.getString("payment_method"), obj.getString("status"));
                                    } catch (JSONException e) {
                                        LOG.e(e.getMessage(), e);
                                    }
                                }
                            });
                        }

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
                        bFax.setText(obj.getString("b_fax"));

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
                        sFax.setText(obj.getString("s_fax"));
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


                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = sdf.format(new Date(Long.parseLong(obj.getString("date")) * 1000));
                        ((TextView) findViewById(R.id.date)).setText(date);

                        //final Calendar cal = Calendar.getInstance();
                        //cal.setTimeInMillis(Long.parseLong(obj.getString("date")));
                        //Date date = cal.getTime();
                        //((TextView) findViewById(R.id.date)).setText(date.toString());


                        statusItem.setClickable(true);
                        trackingNumberItem.setClickable(true);
                        customerItem.setClickable(true);

                        processByPayPalHere(obj.getString("payment_method"), obj.getString("status"));
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

    private void processByPayPalHere(String paymentMethod, String paymentStatus) {
        if (!cameFromPPH && (paymentMethod != null && paymentMethod.equals("PayPal Here")) && (paymentStatus != null && paymentStatus.equals("Q"))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(OrderInfo.this);
            builder.setMessage(R.string.dialog_process_by_paypal_here)
                    .setPositiveButton(R.string.process, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            boolean isPayPalHereInstalled = isPackageInstalled("com.paypal.here", OrderInfo.this);
                            if (isPayPalHereInstalled) {
                                try {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getString("pph_url")));
                                    startActivity(browserIntent);
                                } catch (JSONException e) {
                                    LOG.e(e.getMessage(), e);
                                }
                            } else {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("market://details?id=pname:com.paypal.here"));
                                startActivity(intent);
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            setNewStatus("Declined", "D", "status");
                        }
                    });
            builder.create().show();
        }
        cameFromPPH = false;
    }


    //TODO:
    private boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void clearData() {
        status.setText("");
        paymentStatus.setText("");
        fulfilmentStatus.setText("");
        trackingNumber.setText("");
        paymentMethod.setText("");
        deliveryMethod.setText("");
        customer.setText("");
        billingInfo.setText("");
        bPhone.setText("");
        bFax.setText("");
        shippingInfo.setText("");
        sPhone.setText("");
        sFax.setText("");
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
        status = (TextView) findViewById(R.id.status);
        statusItem = (RelativeLayout) findViewById(R.id.status_item);
        paymentStatus = (TextView) findViewById(R.id.payment_status);
        fulfilmentStatus = (TextView) findViewById(R.id.fulfilment_status);
        paymentStatusItem = (RelativeLayout) findViewById(R.id.payment_status_item);
        fulfilmentStatusItem = (RelativeLayout) findViewById(R.id.fulfilment_status_item);

        try {
            String config = XCartApplication.getInstance().getPreferenceManager().getConfig();
            JSONObject configObject = new JSONObject(config);

            if (XCartApplication.getInstance().getPreferenceManager().getXCartVersion().equals("XCart4")) {

                /*
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
                */
            } else {

                findViewById(R.id.status_item).setVisibility(View.GONE);
                findViewById(R.id.payment_status_item).setVisibility(View.VISIBLE);
                findViewById(R.id.payment_status_separator).setVisibility(View.VISIBLE);
                findViewById(R.id.fulfilment_status_item).setVisibility(View.VISIBLE);


/*
                paymentStatusItem.setOnClickListener(new OnClickListener() {

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
                */
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    public void setNewStatus(final String selectedStatus, final String statusCode, final String statusType) {
        dialogManager.showProgressDialog(R.string.updating_status, PROGRESS_DIALOG);
        try {
            new Requester() {
                @Override
                protected String doInBackground(Void... params) {
                    return new HttpManager(getBaseContext()).changeStatus(orderIdValue, statusCode, statusType);
                }

                @Override
                protected void onPostExecute(String response) {
                    super.onPostExecute(response);

                    dialogManager.dismissDialog(PROGRESS_DIALOG);

                    if (response != null) {
                        Toast.makeText(getBaseContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                        if (statusType.equals("status")) {
                            status.setText(selectedStatus);
                        } else if (statusType.equals("payment_status")) {
                            paymentStatus.setText(selectedStatus);
                        } else if (statusType.equals("fulfilment_status")) {
                            fulfilmentStatus.setText(selectedStatus);
                        }
                        if (!statusType.equals("fulfilment_status")) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("status", selectedStatus);
                            setResult(CHANGE_STATUS_RESULT_CODE, resultIntent);
                            statusSymbol = statusCode;
                        }
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
                if (userId.equals("0")) {
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
    private TextView status;
    private TextView paymentStatus;
    private TextView fulfilmentStatus;
    private TextView trackingNumber;
    private TextView paymentMethod;
    private TextView deliveryMethod;
    private TextView customer;
    private TextView shippingInfo;
    private TextView billingInfo;
    private TextView bPhone;
    private TextView bFax;
    private TextView sPhone;
    private TextView sFax;
    private OrderProductsList itemsList;
    private TextView customerNotes;
    private TextView subtotal;
    private TextView discount;
    private TextView couponSaving;
    private TextView shippingCost;
    private TextView paymentMethodSurcharge;
    private TextView total;
    private RelativeLayout statusItem;
    private RelativeLayout paymentStatusItem;
    private RelativeLayout fulfilmentStatusItem;
    private RelativeLayout trackingNumberItem;
    private RelativeLayout customerItem;
    private String statusSymbol;
    private String paymentStatusSymbol;
    private String fulfilmentStatusSymbol;
    private String userId;
    private String userName;
    private Boolean cameFromPPH;
}
