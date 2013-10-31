package com.xcart.xcartnew.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xcart.xcartnew.R;
import com.xcart.xcartnew.managers.StatusConverter;
import com.xcart.xcartnew.model.Order;
import com.xcart.xcartnew.model.OrderStatus;

import java.util.List;

public class OrdersListAdapter extends BaseArrayAdapter<Order> {

    public OrdersListAdapter(Context context, int resource, List<Order> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            row = inflater.inflate(layoutResourceId, parent, false);
            OrderHolder holder = new OrderHolder();
            row.setTag(holder);
        }

        OrderHolder holder = (OrderHolder) row.getTag();
        holder.order = items.get(position);

        holder.userName = (TextView) row.findViewById(R.id.user_name);
        holder.paid = (TextView) row.findViewById(R.id.paid);
        holder.status = (TextView) row.findViewById(R.id.order_status);
        holder.date = (TextView) row.findViewById(R.id.order_date);
        setupItem(holder);
        return row;
    }

    private void setupItem(OrderHolder holder) {
        holder.userName.setText(holder.order.getUserName() + " (#" + holder.order.getId() + ")");
        holder.paid.setText("$" + holder.order.getPaid());
        OrderStatus statusSymbol = OrderStatus.valueOf(holder.order.getStatus());
        holder.status.setText(StatusConverter.getStatusBySymbol(context, statusSymbol));
        holder.status.setTextColor(StatusConverter.getColorResourceBySymbol(context, statusSymbol));
        holder.date.setText(holder.order.getDate());
    }


    public static class OrderHolder {
        Order order;
        TextView userName;
        TextView paid;
        TextView status;
        TextView date;

        public Order getOrder() {
            return order;
        }
    }
}
