package com.xcart.admin.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xcart.admin.R;
import com.xcart.admin.managers.XCartApplication;

public class OrderProductsList extends LinearLayout {

    public OrderProductsList(Context context) {
        super(context);
        this.context = context;
    }

    public OrderProductsList(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void addItem(final String id, final String name, final String price, final String amount) {
        View item = addItemMain(id, name, price, amount);
        this.addView(item);
    }

    public void addItem(final String id, final String name, final String price, final String amount, final String options) {
        View item = addItemMain(id, name, price, amount);
        RelativeLayout optionsLayout = (RelativeLayout) item.findViewById(R.id.product_options_layout);
        TextView productOptions = (TextView) item.findViewById(R.id.product_options);
        productOptions.setText(options);
        optionsLayout.setVisibility(View.VISIBLE);
        this.addView(item);
    }

    private View addItemMain(final String id, final String name, final String price, final String amount) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (this.getChildCount() > 0) {
            this.addView(inflater.inflate(R.layout.divider, null));
        }
        View item = inflater.inflate(R.layout.order_product_item, null);
        RelativeLayout productLayout = (RelativeLayout) item.findViewById(R.id.order_product_layout);
        TextView productName = (TextView) item.findViewById(R.id.product_name);
        TextView productPrice = (TextView) item.findViewById(R.id.product_price);
        TextView productAmount = (TextView) item.findViewById(R.id.product_amount);
        productName.setText(name);
        String format = XCartApplication.getInstance().getPreferenceManager().getCurrencyFormat();
        productPrice.setText(String.format(format, price));
        productAmount.setText(amount);
        productLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PinSupportActivity pinSupActivity = (PinSupportActivity) context;
                pinSupActivity.setNeedDownloadValue(false);
                Intent intent = new Intent(context, ProductInfo.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                pinSupActivity.startActivityForResult(intent, 1);
            }
        });
        return item;
    }

    public void clearList() {
        removeAllViews();
    }

    private Context context;
}
