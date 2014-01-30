package com.xcart.admin.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xcart.admin.R;
import com.xcart.admin.managers.LogManager;
import com.xcart.admin.managers.network.HttpManager;
import com.xcart.admin.managers.network.Requester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DashboardActivity extends PinSupportNetworkActivity {

    private static final LogManager LOG = new LogManager(DashboardActivity.class.getSimpleName());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.dashboard);
        initTodaySales();
        initLowStock();
        initVisitorsToday();
        initProductsSold();
        initReviewsToday();
        initLastOrders();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void withoutPinAction() {
        updateData();
        super.withoutPinAction();
    }

    private void updateData() {
        setProgressBarIndeterminateVisibility(Boolean.TRUE);
        requester = new Requester() {

            @Override
            protected String doInBackground(Void... voids) {
                return new HttpManager(getBaseContext()).getDashboard();
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    try {
                        JSONObject obj = new JSONObject(result);
                        String todaySalesValue = roundTodaySales(obj.getString("today_sales"));
                        todaySales.setText(todaySalesValue);
                        lowStock.setText(obj.getString("low_stock"));
                        visitorsToday.setText(obj.getString("today_visitors"));
                        reviewsToday.setText(obj.getString("reviews_today"));
                        productsSold.setText(obj.getString("today_sold"));
                        lastOrdersCount.setText("(" + obj.getString("today_orders_count") + ")");
                        JSONArray orders = obj.getJSONArray("today_orders");
                        int ordersLength = orders.length();
                        for (int i = 0; i < ordersLength; i++) {
                            JSONObject order = orders.getJSONObject(i);
                            String title = order.getString("title");
                            if (!title.equals("")) {
                                title += " ";
                            }
                            String name = title + order.getString("firstname") + " " + order.getString("lastname");
                            customers[i].setText(name);
                            customersPaid[i].setText("$" + order.getString("total"));
                        }

                        for (int j = ordersLength; j < 3; j++) {
                            customers[j].setText("");
                            customersPaid[j].setText("");
                        }
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

    private String roundTodaySales(String todaySalesValue) {
        Double todaySalesDouble = Double.parseDouble(todaySalesValue);
        if (todaySalesDouble >= 1000) {
            if (todaySalesDouble >= 100000) {
                todaySalesDouble /= 1000;
                return todaySalesValue = String.valueOf(Math.round(todaySalesDouble)) + "K";
            }
            return todaySalesValue = String.valueOf(Math.round(todaySalesDouble));
        }

        return todaySalesValue;
    }

    private void clearData() {
        todaySales.setText("");
        lowStock.setText("");
        visitorsToday.setText("");
        productsSold.setText("");
        reviewsToday.setText("");
        lastOrdersCount.setText("");
        for (int i = 0; i < 3; i++) {
            customers[i].setText("");
            customersPaid[i].setText("");
        }
    }

    private void initTodaySales() {
        todaySalesLayout = (LinearLayout) findViewById(R.id.today_sales_layout);
        todaySales = (TextView) findViewById(R.id.today_sales);

        todaySalesLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setNeedDownloadValue(false);
                Intent completeOrders = new Intent(getBaseContext(), Orders.class);
                completeOrders.putExtra("sortOption", "complete");
                startActivityForResult(completeOrders, 1);
            }
        });
    }

    private void initLowStock() {
        lowStockLayout = (LinearLayout) findViewById(R.id.low_stock_layout);
        lowStock = (TextView) findViewById(R.id.low_stock);

        lowStockLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setNeedDownloadValue(false);
                Intent lowStockProducts = new Intent(getBaseContext(), Products.class);
                lowStockProducts.putExtra("sortOption", "lowStock");
                startActivityForResult(lowStockProducts, 1);
            }
        });
    }

    private void initVisitorsToday() {
        visitorsTodayLayout = (LinearLayout) findViewById(R.id.visitors_today_layout);
        visitorsToday = (TextView) findViewById(R.id.visitors_today);

        visitorsTodayLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setNeedDownloadValue(false);
                Intent todayUsers = new Intent(getBaseContext(), Users.class);
                todayUsers.putExtra("sortOption", "today");
                startActivityForResult(todayUsers, 1);
            }
        });
    }

    private void initProductsSold() {
        productsSoldLayout = (LinearLayout) findViewById(R.id.products_sold_today_layout);
        productsSold = (TextView) findViewById(R.id.products_sold_today);

        productsSoldLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setNeedDownloadValue(false);
                Intent todayProducts = new Intent(getBaseContext(), Products.class);
                todayProducts.putExtra("sortOption", "all");
                startActivityForResult(todayProducts, 1);
            }
        });
    }

    private void initReviewsToday() {
        reviewsTodayLayout = (LinearLayout) findViewById(R.id.reviews_today_layout);
        reviewsToday = (TextView) findViewById(R.id.reviews_today);

        reviewsTodayLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setNeedDownloadValue(false);
                Intent todayReviews = new Intent(getBaseContext(), Reviews.class);
                todayReviews.putExtra("sortOption", "today");
                startActivityForResult(todayReviews, 1);
            }
        });
    }

    private void initLastOrders() {
        lastOrdersLayout = (RelativeLayout) findViewById(R.id.last_orders_layout);
        lastOrdersCount = (TextView) findViewById(R.id.last_orders_count);

        lastOrdersLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setNeedDownloadValue(false);
                Intent lastOrders = new Intent(getBaseContext(), Orders.class);
                lastOrders.putExtra("sortOption", "all");
                startActivityForResult(lastOrders, 1);
            }
        });

        customers[0] = (TextView) findViewById(R.id.first_customer);
        customersPaid[0] = (TextView) findViewById(R.id.first_customer_paid);
        customers[1] = (TextView) findViewById(R.id.second_customer);
        customersPaid[1] = (TextView) findViewById(R.id.second_customer_paid);
        customers[2] = (TextView) findViewById(R.id.third_customer);
        customersPaid[2] = (TextView) findViewById(R.id.third_customer_paid);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_about:
                dialogManager.showAboutDialog();
                return true;
            case R.id.share:
                //create the send intent
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

                //set the type
                shareIntent.setType("text/plain");

                //add a subject
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Insert Subject Here");

                //build the body of the message to be shared
                String shareMessage = "Insert message body here.";

                //add the message
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);

                //start the chooser for sharing
                startActivity(Intent.createChooser(shareIntent,
                        "Insert share chooser title here"));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.closing_application)
                .setMessage(R.string.close_application_question)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DashboardActivity.super.onBackPressed();
                    }

                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private LinearLayout todaySalesLayout;
    private TextView todaySales;
    private LinearLayout lowStockLayout;
    private TextView lowStock;
    private LinearLayout visitorsTodayLayout;
    private TextView visitorsToday;
    private LinearLayout productsSoldLayout;
    private TextView productsSold;
    private LinearLayout reviewsTodayLayout;
    private TextView reviewsToday;
    private RelativeLayout lastOrdersLayout;
    private TextView lastOrdersCount;
    private TextView[] customers = new TextView[3];
    private TextView[] customersPaid = new TextView[3];
}
