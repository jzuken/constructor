package com.xcart.admin.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.xcart.admin.R;
import com.xcart.admin.managers.XCartApplication;
import com.xcart.admin.managers.network.HttpManager;
import com.xcart.admin.managers.network.Requester;
import com.xcart.admin.model.User;
import com.xcart.admin.views.adapters.UsersListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Users extends PinSupportNetworkActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users);
        setupListViewAdapter();
        setupSearchLine();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.users, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void withoutPinAction() {
        packAmount = XCartApplication.getInstance().getPreferenceManager().getDownloadListLimit();
        if (isNeedDownload()) {
            clearList();
            updateUsersList();
        }
        super.withoutPinAction();
    }

    private void updateUsersList() {
        progressBar.setVisibility(View.VISIBLE);
        synchronized (lock) {
            isDownloading = true;
        }
        hasNext = false;

        final String from = String.valueOf(currentAmount);
        requester = new Requester() {

            @Override
            protected String doInBackground(Void... params) {
                return new HttpManager(getBaseContext()).getUsers(from, String.valueOf(packAmount), searchWord,
                        getCurrentSort());
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
                            String id = obj.getString("id");
                            String title = obj.getString("title");
                            if (!title.equals("")) {
                                title += " ";
                            }
                            String name = title + obj.getString("firstname") + " " + obj.getString("lastname");
                            String login = obj.getString("login");
                            String typeSymbol = obj.getString("usertype");

                            String lastLogin = obj.getString("last_login");
                            if (lastLogin.equals("Jan-01-1970")) {
                                lastLogin = "Never logged in";
                            }
                            addUserToList(id, name, login, typeSymbol, lastLogin);
                        }
                        currentAmount += packAmount;
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

        requester.execute();
    }

    private void clearList() {
        adapter.clear();
        currentAmount = 0;
    }

    private String getCurrentSort() {
        switch (currentSortOption) {
            case 0:
                return "login_date";
            case 1:
                return "order_date";
            case 2:
                return "orders";
            case 3:
                return "none";
            default:
                return null;
        }
    }

    private void addUserToList(final String id, final String name, final String login, final String typeSymbol,
                               final String lastLogin) {
        adapter.add(new User(id, name, login, typeSymbol, lastLogin));
    }

    private void setupListViewAdapter() {
        adapter = new UsersListAdapter(this, R.layout.user_item, new ArrayList<User>());
        usersListView = (ListView) findViewById(R.id.users_list);
        LayoutInflater inflater = getLayoutInflater();

        View listFooter = inflater.inflate(R.layout.on_demand_footer, null, false);
        progressBar = (ProgressBar) listFooter.findViewById(R.id.progress_bar);
        usersListView.addFooterView(listFooter, null, false);

        usersListView.setFooterDividersEnabled(false);

        usersListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount > startItemCount && firstVisibleItem + visibleItemCount == totalItemCount
                        && !isDownloading && hasNext) {
                    updateUsersList();
                }
            }
        });

        usersListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setNeedDownloadValue(false);
                Intent intent = new Intent(getBaseContext(), UserInfo.class);
                User user = ((UsersListAdapter.UserHolder) view.getTag()).getUser();
                intent.putExtra("userName", user.getName());
                intent.putExtra("userId", user.getId());
                startActivityForResult(intent, 1);
            }
        });

        usersListView.setAdapter(adapter);
    }

    private void setupSearchLine() {
        usersSearchLine = (EditText) findViewById(R.id.search_line);
        usersSearchLine.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    searchWord = usersSearchLine.getText().toString();
                    hideKeyboard(usersSearchLine);
                    clearList();
                    updateUsersList();
                    return true;
                }
                return false;
            }
        });
    }

    private void hideKeyboard(EditText edit) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    private ProgressBar progressBar;
    private UsersListAdapter adapter;
    private int currentAmount;
    private boolean isDownloading;
    private int currentSortOption;
    private boolean hasNext;
    private int packAmount;
    private final int startItemCount = 4;
    private ListView usersListView;
    private Object lock = new Object();
    private EditText usersSearchLine;
    private String searchWord = "";
}
