package com.xcart.xcartnew;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.xcart.xcartnew.managers.network.HttpManager;

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
        authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
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
        GetRequester dataRequester = new GetRequester() {

            @Override
            protected String doInBackground(Void... params) {
                return new HttpManager(authorizationData.getString("sid", "")).getProducts(from, String.valueOf(packAmount), searchWord, option);
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

        setRequester(dataRequester);
        dataRequester.execute();
        currentAmount += packAmount;
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
                showActionDialog(((Product) view.getTag()));
            }
        });

        productsListView.setAdapter(adapter);
    }

    private void showActionDialog(final Product item) {
        LinearLayout action_view = (LinearLayout) getLayoutInflater().inflate(R.layout.action_dialog, null);
        final CustomDialog dialog = new CustomDialog(this, action_view);

        ListView actionList = (ListView) action_view.findViewById(R.id.action_list);

        String[] actions = {"Full info", "Set price", "Remove", "Cancel"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.action_item, R.id.textItem, actions);

        actionList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showFullInfo(item.getId(), item.getName());
                        dialog.dismiss();
                        break;
                    case 1:
                        editPriceClick(item.getId(), item.getPrice());
                        dialog.dismiss();
                        break;
                    case 2:
                        deleteClick(item.getId());
                        dialog.dismiss();
                        break;
                    case 3:
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }

            }
        });

        actionList.setAdapter(adapter);

        dialog.show();
    }

    private void showFullInfo(final String id, final String name) {
        setNeedDownloadValue(false);
        Intent intent = new Intent(this, ProductInfo.class);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        startActivityForResult(intent, 1);
    }

    private void editPriceClick(final String id, final String oldPrice) {
        LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.change_price_dialog, null);
        final EditText newPriceEditor = (EditText) view.findViewById(R.id.product_price_editor);
        newPriceEditor.setText(oldPrice);
        final CustomDialog dialog = new CustomDialog(this, view);

        ImageButton okButton = (ImageButton) view.findViewById(R.id.dialog_ok_button);
        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(newPriceEditor);
                String newPrice = newPriceEditor.getText().toString();
                try {
                    Double price = Double.parseDouble(newPrice);
                    if (price > 0) {
                        dialog.dismiss();
                        if (!newPrice.equals(oldPrice)) {
                            setNewPrice(id, newPrice);
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "The price can not be zero", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Incorrect input", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void setNewPrice(final String id, final String newPrice) {
        String response;
        try {
            new GetRequester() {
                @Override
                protected String doInBackground(Void... params) {
                    return new HttpManager(authorizationData.getString("sid", "")).updateProductPrice(id, newPrice);
                }

                @Override
                protected void onPostExecute(String response) {
                    super.onPostExecute(response);
                    if (response != null) {
                        Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
                        clearList();
                        updateProductsList();

                    } else {
                        showConnectionErrorMessage();
                    }
                }
            }.execute();
        } catch (Exception e) {
            showConnectionErrorMessage();
        }
    }

    private void hideKeyboard(EditText edit) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    private void deleteClick(final String id) {
        LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.confirmation_dialog, null);
        ((TextView) view.findViewById(R.id.confirm_question)).setText("Are you sure you want to remove this product?");
        final CustomDialog dialog = new CustomDialog(this, view);

        ImageButton noButton = (ImageButton) view.findViewById(R.id.dialog_no_button);
        noButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ImageButton yesButton = (ImageButton) view.findViewById(R.id.dialog_yes_button);
        yesButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteProduct(id);
            }
        });

        dialog.show();
    }

    private void deleteProduct(final String id) {
        String response;
        try {
            new GetRequester() {
                @Override
                protected String doInBackground(Void... params) {
                    return new HttpManager(authorizationData.getString("sid", "")).deleteProduct(id);
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    if (result != null) {
                        Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
                        clearList();
                        updateProductsList();
                    } else {
                        showConnectionErrorMessage();
                    }
                }
            }.execute();
        } catch (Exception e) {
            showConnectionErrorMessage();
        }

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
        optionTabHost.addEmptyTab("low_stock", getResources().getString(R.string.low_stock_text), -1,
                10, 10, 10, 10);
        optionTabHost.addEmptyTab("", getResources().getString(R.string.all), 1, 10, 10, 10, 10);
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
    private SharedPreferences authorizationData;
    private EditText productsSearchLine;
}
