package com.xcart.admin.views.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xcart.admin.R;
import com.xcart.admin.managers.XCart5StatusConverter;
import com.xcart.admin.managers.XCartApplication;
import com.xcart.admin.model.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OrdersListAdapter extends BaseArrayAdapter<Order> {

    private String format;
    JSONObject configObject = null;

    public OrdersListAdapter(Context context, int resource, List<Order> items) {
        super(context, resource, items);
        this.format = XCartApplication.getInstance().getPreferenceManager().getCurrencyFormat();
        try {
            String config = XCartApplication.getInstance().getPreferenceManager().getConfig();
            configObject = new JSONObject(config);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        try {
            setupItem(holder);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return row;
    }

    private void setupItem(OrderHolder holder) throws JSONException {
        holder.userName.setText(holder.order.getUserName() + " (#" + holder.order.getId() + ")");
        holder.paid.setText(String.format(format, holder.order.getPaid()));
        //TODO:
        if (XCartApplication.getInstance().getPreferenceManager().getXCartVersion().equals("XCart4")) {
            JSONArray statusesJsonArray = configObject.getJSONObject("Order:statuses").getJSONArray("status");
            XCart5StatusConverter statusesConverter = new XCart5StatusConverter(statusesJsonArray);
            holder.status.setText(statusesConverter.getStatusBySymbol(holder.order.getStatus()));
        } else {
            JSONArray paymentStatusesJsonArray = configObject.getJSONObject("Order:statuses").getJSONArray("payment_status");
            XCart5StatusConverter statusesConverter = new XCart5StatusConverter(paymentStatusesJsonArray);
            holder.status.setText(statusesConverter.getStatusBySymbol(holder.order.getStatus()));
        }
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
