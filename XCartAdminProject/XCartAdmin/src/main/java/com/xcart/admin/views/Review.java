package com.xcart.admin.views;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.xcart.admin.R;

public class Review extends PinSupportActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_message);
        TextView email = (TextView) findViewById(R.id.review_user);
        email.setText(getIntent().getStringExtra("email"));
        TextView product = (TextView) findViewById(R.id.review_product);
        product.setText(getIntent().getStringExtra("product"));
        TextView message = (TextView) findViewById(R.id.full_message);
        message.setText(getIntent().getStringExtra("message"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.review, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
