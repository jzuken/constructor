package com.xcart.admin.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.xcart.admin.model.Product;
import com.xcart.admin.views.adapters.ProductsListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Products extends PinSupportNetworkActivity {

    String sortOption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);
        setupListViewAdapter();
        sortOption = getIntent().getStringExtra("sortOption");
        if (sortOption.equals("lowStock")) {
            option = "1";
        } else {
            option = null;
        }
        setupTabs(sortOption);
        setupSearchLine();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.products, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("sortOption", sortOption);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        sortOption = savedInstanceState.getString("sortOption");
    }

    @Override
    protected void withoutPinAction() {
        packAmount = XCartApplication.getInstance().getPreferenceManager().getDownloadListLimit();
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
                return new HttpManager(getBaseContext()).getProducts(from, String.valueOf(packAmount), searchWord,
                        option);
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    try {
                        JSONArray array = new JSONArray(result);
                        int length = array.length();
                        if (length == 0 && currentAmount == 0) {
                            progressBar.setVisibility(View.GONE);
                            noProducts.setVisibility(View.VISIBLE);
                            return;
                        }
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
                            String price = obj.getString("price");
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

        View listFooter = inflater.inflate(R.layout.on_demand_footer_with_message, null, false);
        progressBar = (ProgressBar) listFooter.findViewById(R.id.progress_bar);
        noProducts = (TextView) listFooter.findViewById(R.id.no_content_message);
        noProducts.setText(R.string.no_products);
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
                lastClickedProduct = ((ProductsListAdapter.ProductHolder) view.getTag()).getProduct();
                Intent intent = new Intent(getBaseContext(), ProductInfo.class);
                intent.putExtra("id", lastClickedProduct.getId());
                intent.putExtra("name", lastClickedProduct.getName());
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
        noProducts.setVisibility(View.GONE);
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
            lastClickedProduct.setPrice(data.getStringExtra("price"));
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
    private EditText productsSearchLine;
    private Product lastClickedProduct;
    private TextView noProducts;
}
