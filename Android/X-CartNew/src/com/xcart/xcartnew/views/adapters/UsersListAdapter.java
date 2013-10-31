package com.xcart.xcartnew.views.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xcart.xcartnew.R;
import com.xcart.xcartnew.model.User;

public class UsersListAdapter extends BaseArrayAdapter<User> {

    public UsersListAdapter(Context context, int resource, List<User> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            row = inflater.inflate(layoutResourceId, parent, false);
            UserHolder holder = new UserHolder();
            row.setTag(holder);
        }

        UserHolder holder = (UserHolder) row.getTag();
        holder.user = items.get(position);

        holder.name = (TextView) row.findViewById(R.id.user_name);
        if (position == 0) {
            holder.name.setBackgroundResource(R.drawable.top_rounded_subtitle);
        }
        holder.login = (TextView) row.findViewById(R.id.user_login);
        holder.phone = (TextView) row.findViewById(R.id.user_phone);
        holder.lastLogin = (TextView) row.findViewById(R.id.last_login);
        setupItem(holder);
        return row;
    }

    private void setupItem(UserHolder holder) {
        holder.name.setText(holder.user.getName());
        holder.login.setText(holder.user.getLogin());
        holder.phone.setText(holder.user.getPhone());
        holder.lastLogin.setText(holder.user.getLastLogin());
    }

    public static class UserHolder {
        User user;
        TextView name;
        TextView login;
        TextView phone;
        TextView lastLogin;

        public User getUser() {
            return user;
        }
    }
}
