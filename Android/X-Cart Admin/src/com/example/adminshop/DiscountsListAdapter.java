package com.example.adminshop;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DiscountsListAdapter extends ArrayAdapter<Discount> {

	public DiscountsListAdapter(Context context, int resource, List<Discount> items) {
		super(context, resource, items);
		this.context = context;
		this.layoutResourceId = resource;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);

		DiscountHolder holder = new DiscountHolder();
		holder.discount = items.get(position);

		holder.orderSubtotal = (TextView) row.findViewById(R.id.order_subtotal);
		holder.discountValue = (TextView) row.findViewById(R.id.discount);
		holder.discountType = (TextView) row.findViewById(R.id.discount_type);
		holder.membership = (TextView) row.findViewById(R.id.membership);
		row.setTag(holder.discount);
		setupItem(holder);
		return row;
	}

	private void setupItem(DiscountHolder holder) {
		holder.orderSubtotal.setText(holder.discount.getOrderSubtotal());
		holder.discountValue.setText(holder.discount.getDiscount());
		holder.discountType.setText(holder.discount.getDiscountType());
		holder.membership.setText(holder.discount.getMembership());
	}

	private static class DiscountHolder {
		Discount discount;
		TextView orderSubtotal;
		TextView discountValue;
		TextView discountType;
		TextView membership;
	}

	private List<Discount> items;
	private int layoutResourceId;
	private Context context;
}
