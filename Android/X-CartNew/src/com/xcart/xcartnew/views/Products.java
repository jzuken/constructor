package com.xcart.xcartnew.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

import com.xcart.xcartnew.R;
import com.xcart.xcartnew.managers.network.HttpManager;
import com.xcart.xcartnew.managers.network.Requester;
import com.xcart.xcartnew.model.Product;
import com.xcart.xcartnew.views.adapters.ProductsListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Products extends PinSupportNetworkActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);
        setupListViewAdapter();
        String sortOption = getIntent().getStringExtra("sortOption");
        if (sortOption.equals("lowStock")) {
            option = "1";
        } else {
            option = null;
        }
        setupTabs(sortOption);
        settingsData = PreferenceManager.getDefaultSharedPreferences(this);
        setupSearchLine();
    }

    @Override
    protected void withoutPinAction() {
        packAmount = Integer.parseInt(settingsData.getString("products_amount", "10"));
        if (isNeedDownload()) {
            clearList();
            updateProductsList();
        }
        super.withoutPinAction();
    }

    private void updateProductsList() {
        progressBar.setVisibility(View.VISIBLE);
        synchronized (lock) {
            isDownloading = true;
        }
        hasNext = false;
        final String from = String.valueOf(currentAmount);
        requester = new Requester() {

            @Override
            protected String doInBackground(Void... params) {
                return new HttpManager(getBaseContext()).getProducts(from,
                        String.valueOf(packAmount), searchWord, option);
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
                            String id = obj.getString("productid");
                            String name = obj.getString("product");
                            String productCode = obj.getString("productcode");
                            String sku = productCode.substring(3, productCode.length());
                            String inStock = obj.getString("avail");
                            String price = obj.getString("list_price");
                            addProductToList(id, name, sku, inStock, price);
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

    private void addProductToList(final String id, final String name, final String sku, final String inStock,
                                  final String price) {
        adapter.add(new Product(id, name, sku, inStock, price));
    }

    private void setupListViewAdapter() {
        adapter = new ProductsListAdapter(this, R.layout.product_item, new ArrayList<Product>());
        productsListView = (ListView) findViewById(R.id.products_list);
        LayoutInflater inflater = getLayoutInflater();

        View listFooter = inflater.inflate(R.layout.on_demand_footer, null, false);
        progressBar = (ProgressBar) listFooter.findViewById(R.id.progress_bar);
        productsListView.addFooterView(listFooter, null, false);

        productsListView.setFooterDividersEnabled(false);

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

        productsListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setNeedDownloadValue(false);
                lastPositionClicked = position;
                Intent intent = new Intent(getBaseContext(), ProductInfo.class);
                Product product = ((ProductsListAdapter.ProductHolder) view.getTag()).getProduct();
                intent.putExtra("id", product.getId());
                intent.putExtra("name", product.getName());
                startActivityForResult(intent, 1);
            }
        });

        productsListView.setAdapter(adapter);
    }

    private void hideKeyboard(EditText edit) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    private void clearList() {
        adapter.clear();
        currentAmount = 0;
    }

    private void setupSearchLine() {
        productsSearchLine = (EditText) findViewById(R.id.search_line);
        productsSearchLine.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    searchWord = productsSearchLine.getText().toString();
                    hideKeyboard(productsSearchLine);
                    clearList();
                    updateProductsList();
                    return true;
                }
                return false;
            }
        });
    }

    private void setupTabs(String sortOption) {
        optionTabHost = (CustomTabHost) findViewById(android.R.id.tabhost);
        optionTabHost.setup();
        optionTabHost.addEmptyTab("low_stock", getResources().getString(R.string.low_stock_text), -1);
        optionTabHost.addEmptyTab("", getResources().getString(R.string.all), 1);
        if (sortOption.equals("all")) {
            optionTabHost.setCurrentTab(1);
        }
        TabWidget tabWidget = (TabWidget) findViewById(android.R.id.tabs);
        LinearLayout.LayoutParams currentLayout = (LinearLayout.LayoutParams) tabWidget.getChildAt(0).getLayoutParams();
        currentLayout.setMargins(0, 0, 1, 0);
        tabWidget.requestLayout();

        optionTabHost.setOnTabChangedListener(new OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                option = tabId.equals("low_stock") ? "1" : null;
                cancelRequest();
                clearList();
                updateProductsList();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ProductInfo.changePriceResultCode) {
            ProductsListAdapter.ProductHolder productHolder = ((ProductsListAdapter.ProductHolder) productsListView.getChildAt(lastPositionClicked).getTag());
            productHolder.getProduct().setPrice(data.getStringExtra("price"));
            adapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private ProgressBar progressBar;
    private ProductsListAdapter adapter;
    private int currentAmount;
    private CustomTabHost optionTabHost;
    private String option;
    private boolean isDownloading;
    private boolean hasNext;
    private int packAmount;
    private final int startItemCount = 4;
    private String searchWord = "";
    private ListView productsListView;
    private Object lock = new Object();
    private SharedPreferences settingsData;
    private EditText productsSearchLine;
    private int lastPositionClicked;
}
