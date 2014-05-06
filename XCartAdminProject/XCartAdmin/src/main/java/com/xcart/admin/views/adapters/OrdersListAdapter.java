package com.xcart.admin.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xcart.admin.R;
import com.xcart.admin.managers.StatusConverter;
import com.xcart.admin.managers.XCartApplication;
import com.xcart.admin.model.Order;
import com.xcart.admin.model.OrderStatus;

import java.util.List;

public class OrdersListAdapter extends BaseArrayAdapter<Order> {

    private String format;

    public OrdersListAdapter(Context context, int resource, List<Order> items) {
        super(context, resource, items);
        this.format = XCartApplication.getInstance().getPreferenceManager().getCurrencyFormat();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            row = inflater.inflate(layoutResourceId, parent, false);
            OrderHolder holder = new OrderHolder((TextView) row.findViewById(R.id.user_name), (TextView) row.findViewById(R.id.paid), (TextView) row.findViewById(R.id.order_status), (TextView) row.findViewById(R.id.order_fulfilment_status), (TextView) row.findViewById(R.id.order_date));
            row.setTag(holder);
        }

        OrderHolder holder = (OrderHolder) row.getTag();
        holder.order = items.get(position);
        setupItem(holder, holder.order);
        return row;
    }

    private void setupItem(OrderHolder holder, Order order) {
        holder.userName.setText(order.getUserName() + " (#" + order.getId() + ")");
        holder.paid.setText(String.format(format, order.getPaid()));
        OrderStatus statusSymbol = OrderStatus.valueOf(order.getStatus());
        holder.status.setText(StatusConverter.getStatusBySymbol(context, statusSymbol));
        holder.status.setTextColor(StatusConverter.getColorResourceBySymbol(context, statusSymbol));
        holder.fulfilmentStatus.setText(order.getFulfilmentStatus());
        holder.date.setText(order.getDate());
    }


    public static class OrderHolder {
        Order order;
        TextView userName;
        TextView paid;
        TextView status;
        TextView fulfilmentStatus;
        TextView date;

        public Order getOrder() {
            return order;
        }

        public OrderHolder(TextView userName, TextView paid, TextView status, TextView fulfilmentStatus, TextView date) {
            this.userName = userName;
            this.paid = paid;
            this.status = status;
            this.fulfilmentStatus = fulfilmentStatus;
            this.date = date;
        }
    }
}
