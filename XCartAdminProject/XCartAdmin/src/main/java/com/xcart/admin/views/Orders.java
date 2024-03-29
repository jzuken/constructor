package com.xcart.admin.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
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

public class Orders extends PinSupportNetworkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.orders);
        setupListViewAdapter();
        setupPeriodTabs();
        setupSearchLine();
        period = "today";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.orders, menu);
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
            clearList();
            updateOrdersList();
        }
        super.withoutPinAction();
    }

    private void updateOrdersList() {
        progressBar.setVisibility(View.VISIBLE);
        synchronized (lock) {
            isDownloading = true;
        }
        hasNext = false;
        final String from = String.valueOf(currentAmount);
        requester = new Requester() {
            @Override
            protected String doInBackground(Void... params) {
                return new HttpManager(getBaseContext()).getLastOrders(from, String.valueOf(packAmount), period,
                        searchWord, null);
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    try {
                        JSONArray array = new JSONArray(result);
                        int length = array.length();
                        if (length == 0 && currentAmount == 0) {
                            progressBar.setVisibility(View.GONE);
                            noOrders.setVisibility(View.VISIBLE);
                            return;
                        }
                        if (length == packAmount) {
                            hasNext = true;
                        }
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = array.getJSONObject(i);
                            String id = obj.getString("orderid");
                            String title = obj.getString("title");
                            if (!title.equals("")) {
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
                progressBar.setVisibility(View.GONE);
                synchronized (lock) {
                    isDownloading = false;
                }
            }
        };

        requester.execute();
    }

    private void addOrderToList(final String id, final String userName, final String paid, final String status, String fulfilmentStatus, final String date) {
        adapter.add(new Order(id, userName, paid, status, fulfilmentStatus, date));
    }

    private void setupListViewAdapter() {
        adapter = new OrdersListAdapter(this, R.layout.order_item, new ArrayList<Order>());
        ordersListView = (ListView) findViewById(R.id.orders_list);

        LayoutInflater inflater = getLayoutInflater();
        View listFooter = inflater.inflate(R.layout.on_demand_footer_with_message, null, false);
        progressBar = (ProgressBar) listFooter.findViewById(R.id.progress_bar);
        noOrders = (TextView) listFooter.findViewById(R.id.no_content_message);
        noOrders.setText(R.string.no_orders);
        ordersListView.addFooterView(listFooter, null, false);

        ordersListView.setFooterDividersEnabled(false);

        ordersListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount > 0 && firstVisibleItem + visibleItemCount == totalItemCount && !isDownloading
                        && hasNext) {
                    updateOrdersList();
                }
            }
        });

        ordersListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setNeedDownloadValue(false);
                lastClickedOrder = (Order) parent.getItemAtPosition(position);
                Intent intent = new Intent(getBaseContext(), OrderInfo.class);
                intent.putExtra("orderId", lastClickedOrder.getId());
                startActivityForResult(intent, 1);
            }
        });

        ordersListView.setAdapter(adapter);
    }

    private void setupPeriodTabs() {
        periodTabHost = (CustomTabHost) findViewById(android.R.id.tabhost);
        periodTabHost.setup();
        periodTabHost.addEmptyTab("today", getResources().getString(R.string.today), -1);
        periodTabHost.addEmptyTab("week", getResources().getString(R.string.this_week), 0);
        periodTabHost.addEmptyTab("month", getResources().getString(R.string.this_month), 0);
        periodTabHost.addEmptyTab("all", getResources().getString(R.string.all), 1);

        TabWidget tabWidget = (TabWidget) findViewById(android.R.id.tabs);
        final int tabChildrenCount = tabWidget.getChildCount();
        View currentView;
        for (int i = 0; i < tabChildrenCount - 1; i++) {
            currentView = tabWidget.getChildAt(i);
            LinearLayout.LayoutParams currentLayout = (LinearLayout.LayoutParams) currentView.getLayoutParams();
            currentLayout.setMargins(0, 0, 1, 0);
        }
        tabWidget.requestLayout();

        periodTabHost.setOnTabChangedListener(new OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                period = tabId;
                cancelRequest();
                clearList();
                updateOrdersList();
            }
        });
    }

    private void setupSearchLine() {
        ordersSearchLine = (EditText) findViewById(R.id.search_line);
        ordersSearchLine.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    searchWord = ordersSearchLine.getText().toString();
                    hideKeyboard(ordersSearchLine);
                    clearList();
                    updateOrdersList();
                    return true;
                }
                return false;
            }
        });
    }

    private void hideKeyboard(EditText edit) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    private void clearList() {
        adapter.clear();
        currentAmount = 0;
        noOrders.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == OrderInfo.CHANGE_STATUS_RESULT_CODE) {
            lastClickedOrder.setPaymentStatus(data.getStringExtra("status"));
            adapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private ProgressBar progressBar;
    private TextView noOrders;
    private OrdersListAdapter adapter;
    private ListView ordersListView;
    private CustomTabHost periodTabHost;
    private String period;
    private Object lock = new Object();
    private int currentAmount;
    private boolean isDownloading;
    private boolean hasNext;
    private int packAmount;
    private String searchWord = "";
    private EditText ordersSearchLine;
    private Order lastClickedOrder;
}
