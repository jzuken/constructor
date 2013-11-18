package com.xcart.xcartadmin.views.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xcart.xcartadmin.R;
import com.xcart.xcartadmin.model.Product;

public class ProductsListAdapter extends BaseArrayAdapter<Product> {

    public ProductsListAdapter(Context context, int resource, List<Product> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            row = inflater.inflate(layoutResourceId, parent, false);
            ProductHolder holder = new ProductHolder();
            row.setTag(holder);
        }

        ProductHolder holder = (ProductHolder) row.getTag();

        holder.product = items.get(position);
        holder.name = (TextView) row.findViewById(R.id.product_name);
        holder.sku = (TextView) row.findViewById(R.id.sku);
        holder.inStock = (TextView) row.findViewById(R.id.in_stock);
        holder.price = (TextView) row.findViewById(R.id.price);
        setupItem(holder);
        return row;
    }

    private void setupItem(ProductHolder holder) {
        holder.name.setText(holder.product.getName());
        holder.sku.setText(holder.product.getSku());
        holder.inStock.setText(holder.product.getInStock());
        holder.price.setText("$" + holder.product.getPrice());
    }

    public static class ProductHolder {
        Product product;
        TextView name;
        TextView sku;
        TextView inStock;
        TextView price;

        public Product getProduct() {
            return product;
        }
    }
}
