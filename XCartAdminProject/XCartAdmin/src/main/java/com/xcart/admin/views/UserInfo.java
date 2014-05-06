package com.xcart.admin.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xcart.admin.R;
import com.xcart.admin.managers.XCartApplication;
import com.xcart.admin.managers.network.HttpManager;
import com.xcart.admin.managers.network.Requester;
import com.xcart.admin.model.Order;
import com.xcart.admin.views.adapters.OrdersListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserInfo extends PinSupportNetworkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.user_info);
        adapter = new OrdersListAdapter(this, R.layout.order_item, new ArrayList<Order>());
        ordersListView = (ListView) findViewById(R.id.orders_list);
        LayoutInflater inflater = LayoutInflater.from(this);

        View header = inflater.inflate(R.layout.user_info_header, null, false);
        ordersListView.addHeaderView(header, null, false);

        View listFooter = inflater.inflate(R.layout.on_demand_footer, null, false);
        ordersProgressBar = (ProgressBar) listFooter.findViewById(R.id.progress_bar);
        ordersListView.addFooterView(listFooter, null, false);

        ordersListView.setFooterDividersEnabled(false);
        ordersListView.setHeaderDividersEnabled(false);

        ordersListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount > 0 && firstVisibleItem + visibleItemCount == totalItemCount && !isDownloading && hasNext) {
                    updateOrdersList();
                }
            }
        });

        ordersListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setNeedDownloadValue(false);
                lastPositionClicked = position;
                Intent intent = new Intent(getBaseContext(), OrderInfo.class);
                intent.putExtra("orderId", ((OrdersListAdapter.OrderHolder) view.getTag()).getOrder().getId());
                startActivityForResult(intent, 1);
            }
        });

        ordersListView.setAdapter(adapter);
        setTitle(getIntent().getStringExtra("userName"));
        firstName = (TextView) header.findViewById(R.id.first_name);
        lastName = (TextView) header.findViewById(R.id.last_name);
        email = (TextView) header.findViewById(R.id.email);
        address = (TextView) header.findViewById(R.id.address);
        phone = (TextView) header.findViewById(R.id.phone);
        fax = (TextView) header.findViewById(R.id.fax);
        user_id = getIntent().getStringExtra("userId");
        sendMessageButton = (Button) header.findViewById(R.id.send_message_button);
        callButton = (Button) header.findViewById(R.id.call_button);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_info, menu);
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
        packAmount = XCartApplication.getInstance().getPreferenceManager().getDownloadListLimit();
        if (isNeedDownload()) {
            clearData();
            clearList();
            updateData();
            updateOrdersList();
        }
        super.withoutPinAction();
    }

    public void sendMessageClick(View v) {
        setNeedDownloadValue(false);
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email.getText().toString(), null));
        startActivityForResult(Intent.createChooser(emailIntent, "Send message..."), 3);
    }

    public void callClick(View v) {
        setNeedDownloadValue(false);
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone.getText()));
        startActivityForResult(callIntent, 1);
    }

    private void updateData() {
        if (user_id.equals("0")) {
            Intent intent = getIntent();
            firstName.setText(intent.getStringExtra("firstname"));
            lastName.setText(intent.getStringExtra("lastname"));
            email.setText(intent.getStringExtra("email"));
            address.setText(intent.getStringExtra("b_address") + "\n" + intent.getStringExtra("b_city") + ", "
                    + intent.getStringExtra("b_state") + " " + intent.getStringExtra("b_zipcode") + "\n"
                    + intent.getStringExtra("b_country"));
            String phoneValue = intent.getStringExtra("b_phone");
            if (!isEmpty(phoneValue)) {
                callButton.setClickable(true);
                callButton.setBackgroundResource(R.drawable.right_rounded_green_button);
                phone.setText(phoneValue);
            }
            fax.setText(intent.getStringExtra("b_fax"));
            setProgressBarIndeterminateVisibility(Boolean.FALSE);
            return;
        }
        setProgressBarIndeterminateVisibility(Boolean.TRUE);

        requester = new Requester() {
            @Override
            protected String doInBackground(Void... params) {
                return new HttpManager(getBaseContext()).getUserInfo(user_id);
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    try {
                        JSONObject obj = new JSONObject(result);
                        firstName.setText(obj.getString("firstname"));
                        lastName.setText(obj.getString("lastname"));
                        String emailValue = obj.getString("login");
                        if (!isEmpty(emailValue)) {
                            sendMessageButton.setClickable(true);
                            email.setText(emailValue);
                        }
                        JSONObject addressObj = obj.getJSONObject("address");
                        JSONObject shippingObj = addressObj.getJSONObject("S");
                        address.setText(shippingObj.get("address") + "\n" + shippingObj.getString("city") + ", "
                                + shippingObj.getString("state") + " " + shippingObj.getString("zipcode") + "\n"
                                + shippingObj.getString("country"));
                        String phoneValue = shippingObj.getString("phone");
                        if (!isEmpty(phoneValue)) {
                            callButton.setClickable(true);
                            callButton.setBackgroundResource(R.drawable.right_rounded_green_button);
                            phone.setText(phoneValue);
                        }
                        fax.setText(shippingObj.getString("fax"));
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

    private void updateOrdersList() {
        if (user_id.equals("0")) {
            findViewById(R.id.order_list_header).setVisibility(View.GONE);
            return;
        }
        ordersProgressBar.setVisibility(View.VISIBLE);
        synchronized (lock) {
            isDownloading = true;
        }
        hasNext = false;
        final String from = String.valueOf(currentAmount);
        requester = new Requester() {
            @Override
            protected String doInBackground(Void... params) {
                return new HttpManager(getBaseContext()).getUserOrders(from, String.valueOf(packAmount), user_id);
            }

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
                            String id = obj.getString("orderid");
                            String title = obj.getString("title");
                            if (!isEmpty(title)) {
                                title += " ";
                            }
                            String name = title + obj.getString("firstname") + " " + obj.getString("lastname");
                            String status = obj.getString("status");
                            String fulfilmentStatus = obj.getString("status");
                            String date = obj.getString("month") + "\n" + obj.getString("day");
                            String paid = obj.getString("total");
                            addOrderToList(id, name, paid, status, fulfilmentStatus, date);
                        }
                        currentAmount += packAmount;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showConnectionErrorMessage();
                }
                ordersProgressBar.setVisibility(View.GONE);
                synchronized (lock) {
                    isDownloading = false;
                }
            }
        };

        requester.execute();
    }

    private void addOrderToList(final String id, final String userName, final String paid, final String status, final String fulfilmentStatus, final String date) {
        adapter.add(new Order(id, userName, paid, status, fulfilmentStatus, date));
    }

    private void clearData() {
        firstName.setText("");
        lastName.setText("");
        email.setText("");
        address.setText("");
        phone.setText("");
        fax.setText("");
        sendMessageButton.setClickable(false);
        callButton.setClickable(false);
    }

    private void clearList() {
        adapter.clear();
        currentAmount = 0;
    }

    private boolean isEmpty(String string) {
        return string.equals("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == OrderInfo.CHANGE_STATUS_RESULT_CODE) {
            OrdersListAdapter.OrderHolder orderHolder = ((OrdersListAdapter.OrderHolder) ordersListView.getChildAt(lastPositionClicked).getTag());
            orderHolder.getOrder().setPaymentStatus(data.getStringExtra("status"));
            adapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private ProgressBar ordersProgressBar;
    private TextView userName;
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView address;
    private TextView phone;
    private TextView fax;
    private Button sendMessageButton;
    private Button callButton;
    private OrdersListAdapter adapter;
    private String user_id;
    private Object lock = new Object();
    private int lastPositionClicked;
    private boolean isDownloading;
    private boolean hasNext;
    private int packAmount;
    private int currentAmount;
    ListView ordersListView;
}
