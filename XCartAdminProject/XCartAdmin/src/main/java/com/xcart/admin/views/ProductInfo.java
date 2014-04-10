package com.xcart.admin.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xcart.admin.R;
import com.xcart.admin.managers.DialogManager;
import com.xcart.admin.managers.XCartApplication;
import com.xcart.admin.managers.network.DownloadImageTask;
import com.xcart.admin.managers.network.HttpManager;
import com.xcart.admin.managers.network.Requester;
import com.xcart.admin.views.dialogs.CustomDialog;

import org.jraf.android.backport.switchwidget.Switch;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductInfo extends PinSupportNetworkActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.product_full_info);
        productName = getIntent().getStringExtra("name");
        productId = getIntent().getStringExtra("id");
        name = (TextView) findViewById(R.id.product_name);
        name.setText(productName);
        productImage = (ImageView) findViewById(R.id.product_image);
        description = (WebView) findViewById(R.id.description);
        initDescriptionWebView(description);
        fullDescription = (WebView) findViewById(R.id.full_description);
        initDescriptionWebView(fullDescription);
        isVisibleFoolDescr = false;
        fullDescriptionDivider = findViewById(R.id.full_description_divider);
        inStock = (TextView) findViewById(R.id.in_stock);
        initFullDescrLable();
        setupPriceItem();
        setupAvailabilitySwitch();
        setupVariantsSpinner();
        variantsLayout = (LinearLayout) findViewById(R.id.variants_layout);
        variantsDivider = findViewById(R.id.variants_divider);
        options = (TextView) findViewById(R.id.options);
        variantsArray = new JSONArray();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.product_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_about:
                dialogManager.showAboutDialog();
                return true;
            case R.id.action_share:
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, productName);
                String shareMessage = String.format("%s: %s", productName, new HttpManager(this).getProductUrl(productId));
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Insert share chooser title here"));
                return true;
            case R.id.action_edit:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(new HttpManager(this).getProductEditUrl(productId)));
                startActivity(browserIntent);
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

    private void updateData() {
        setProgressBarIndeterminateVisibility(Boolean.TRUE);

        requester = new Requester() {
            @Override
            protected String doInBackground(Void... params) {
                return new HttpManager(getBaseContext()).getProductInfo(productId);
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    try {
                        JSONObject obj = new JSONObject(result);
                        description.loadDataWithBaseURL("", obj.getString("descr"), "text/html", "UTF-8", "");
                        String fullDescriptionText = obj.getString("fulldescr");
                        if (!fullDescriptionText.equals("")) {
                            fullDescription.loadDataWithBaseURL("", fullDescriptionText, "text/html", "UTF-8", "");
                            fullDescrLabel.setVisibility(View.VISIBLE);
                        }
                        String format = XCartApplication.getInstance().getPreferenceManager().getCurrencyFormat();
                        price.setText(String.format(format, obj.getString("price")));
                        priceItem.setClickable(true);
                        inStock.setText(obj.getString("avail"));
                        if (obj.getString("forsale").equals("Y")) {
                            availabilitySwitch.setChecked(true);
                        }
                        isNeedAvailabilityChange = true;
                        availabilitySwitch.setClickable(!XCartApplication.getInstance().isExpired());
                        String imageUrl = obj.getString("image_url");
                        if (!imageUrl.equals(NO_IMAGE_URL)) {
                            new DownloadImageTask(productImage, new DownloadImageTask.Callback() {
                                @Override
                                public void done() {
                                    setProgressBarIndeterminateVisibility(Boolean.FALSE);
                                }
                            }).execute(imageUrl);
                        } else {
                            productImage.setImageDrawable(getResources().getDrawable(R.drawable.no_image));
                            setProgressBarIndeterminateVisibility(Boolean.FALSE);
                        }
                        if (!obj.get("variants").equals(null)) {
                            variantsArray = obj.getJSONArray("variants");
                            for (int i = 0; i < variantsArray.length(); i++) {
                                JSONObject variant = variantsArray.getJSONObject(i);
                                variantsList.add(variant.getString("productcode"));
                            }
                            variantsAdapter.notifyDataSetChanged();
                            variantsSpinner.setSelection(currentVariant, true);
                            showVariants();
                            priceItem.setClickable(false);
                        } else {
                            priceArrow.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        setProgressBarIndeterminateVisibility(Boolean.FALSE);
                    }
                } else {
                    showConnectionErrorMessage();
                    setProgressBarIndeterminateVisibility(Boolean.FALSE);
                }
            }
        };

        requester.execute();
    }

    private void clearData() {
        description.loadUrl("about:blank");
        fullDescription.loadUrl("about:blank");
        hideFullDescr();
        hideVariants();
        variantsList.clear();
        variantsAdapter.clear();
        price.setText("");
        priceArrow.setVisibility(View.GONE);
        options.setText("");
        inStock.setText("");
        productImage.setImageResource(android.R.color.transparent);
        isNeedAvailabilityChange = false;
        availabilitySwitch.setChecked(false);
        priceItem.setClickable(false);
        availabilitySwitch.setClickable(false);
    }

    private void initFullDescrLable() {
        fullDescrLabel = (TextView) findViewById(R.id.full_description_label);
        fullDescrLabel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isVisibleFoolDescr) {
                    showFullDescr();
                } else {
                    hideFullDescr();
                }
            }
        });
    }

    private void hideFullDescr() {
        fullDescription.setVisibility(View.GONE);
        fullDescriptionDivider.setVisibility(View.GONE);
        isVisibleFoolDescr = false;
    }

    private void hideVariants() {
        variantsLayout.setVisibility(View.GONE);
        variantsDivider.setVisibility(View.GONE);
    }

    private void showVariants() {
        variantsLayout.setVisibility(View.VISIBLE);
        variantsDivider.setVisibility(View.VISIBLE);
    }

    private void showFullDescr() {
        fullDescription.setVisibility(View.VISIBLE);
        fullDescriptionDivider.setVisibility(View.VISIBLE);
        isVisibleFoolDescr = true;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initDescriptionWebView(WebView descript) {
        WebSettings descriptionSettings = descript.getSettings();
        descriptionSettings.setJavaScriptEnabled(true);
        descriptionSettings.setDefaultFontSize(14);
    }

    private void setupPriceItem() {
        price = (TextView) findViewById(R.id.price);
        priceArrow = (ImageView) findViewById(R.id.price_arrow);
        priceItem = (RelativeLayout) findViewById(R.id.price_item);
        priceItem.setClickable(false);
        final Context context = this;
        priceItem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (XCartApplication.getInstance().isExpired()) {
                    DialogManager dm = new DialogManager(ProductInfo.this.getSupportFragmentManager());
                    dm.showErrorDialog(R.string.subscription_expired);
                    return;
                }

                priceItem.setClickable(false);
                LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.change_value_dialog, null);
                ((TextView) view.findViewById(R.id.label)).setText(R.string.set_price);
                final EditText priceEditor = (EditText) view.findViewById(R.id.value_editor);
                String temp = price.getText().toString();
                final String oldPrice = temp.substring(1);
                priceEditor.setText(oldPrice);
                final CustomDialog dialog = new CustomDialog(context, view) {
                    @Override
                    public void dismiss() {
                        priceItem.setClickable(true);
                        super.dismiss();
                    }
                };

                Button saveButton = (Button) view.findViewById(R.id.dialog_save_button);
                saveButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideKeyboard(priceEditor);
                        String newPrice = priceEditor.getText().toString();
                        try {
                            Double price = Double.parseDouble(newPrice);
                            if (price > 0) {
                                dialog.dismiss();
                                if (!newPrice.equals(oldPrice)) {
                                    setNewPrice(newPrice);
                                }
                            } else {
                                dialogManager.showErrorDialog(R.string.price_error);
                            }
                        } catch (Exception e) {
                            dialogManager.showErrorDialog(R.string.incorrect_input);
                        }
                    }
                });

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        showKeyboard();
                        priceEditor.selectAll();
                    }
                });
                dialog.show();
            }
        });
    }

    private void setupVariantsSpinner() {
        variantsSpinner = (SameSelectableSpinner) findViewById(R.id.variants_spinner);
        OnItemSelectedListener listener = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
                try {
                    JSONObject variant = variantsArray.getJSONObject(position);
                    String format = XCartApplication.getInstance().getPreferenceManager().getCurrencyFormat();
                    price.setText(String.format(format, variant.getString("price")));
                    inStock.setText(variant.getString("avail"));
                    options.setText(getOptions(variant));
                    currentVariant = position;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        };
        variantsSpinner.setOnItemSelectedListener(listener);
        variantsSpinner.setOnItemSelectedEvenIfUnchangedListener(listener);
        variantsList = new ArrayList<String>();
        variantsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, variantsList);
        variantsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        variantsSpinner.setAdapter(variantsAdapter);
    }

    private String getOptions(JSONObject variant) {
        JSONArray optionsArray;
        try {
            optionsArray = variant.getJSONArray("options_arr");
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

    private void setNewPrice(final String newPrice) {
        try {
            new Requester() {
                @Override
                protected String doInBackground(Void... params) {
                    return new HttpManager(getBaseContext()).updateProductPrice(productId, newPrice);
                }

                @Override
                protected void onPostExecute(String response) {
                    super.onPostExecute(response);
                    if (response != null) {
                        Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
                        String format = XCartApplication.getInstance().getPreferenceManager().getCurrencyFormat();
                        price.setText(String.format(format, newPrice));
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("price", newPrice);
                        setResult(changePriceResultCode, resultIntent);
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

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void setupAvailabilitySwitch() {
        availabilitySwitch = (Switch) findViewById(R.id.availability_switch);
        availabilitySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isNeedAvailabilityChange) {
                    setNewAvailability(productId, isChecked);
                }
            }
        });
    }

    private void setNewAvailability(final String productId, final boolean available) {
        final String availability = available ? "1" : "2";

        try {
            new Requester() {
                @Override
                protected String doInBackground(Void... params) {
                    return new HttpManager(getBaseContext()).changeAvailable(productId, availability);
                }

                @Override
                protected void onPostExecute(String response) {
                    super.onPostExecute(response);
                    if (response != null) {
                        Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        showConnectionErrorMessage();
                    }
                }
            }.execute();
        } catch (Exception e) {
            showConnectionErrorMessage();
        }
    }

    private String productId = "";
    private String productName = "";
    private TextView name;
    private ImageView productImage;
    private WebView description;
    private WebView fullDescription;
    boolean isVisibleFoolDescr;
    private View fullDescriptionDivider;
    private TextView price;
    private ImageView priceArrow;
    private TextView inStock;
    private TextView fullDescrLabel;
    private RelativeLayout priceItem;
    private Switch availabilitySwitch;
    private boolean isNeedAvailabilityChange;
    private static final String NO_IMAGE_URL = "/xcart/default_image.gif";
    private ArrayAdapter<String> variantsAdapter;
    private SameSelectableSpinner variantsSpinner;
    private View variantsDivider;
    private LinearLayout variantsLayout;
    private List<String> variantsList;
    private TextView options;
    public static final int changePriceResultCode = 200;
    private JSONArray variantsArray;
    private int currentVariant;
}
