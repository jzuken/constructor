package com.xcart.admin.views;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;

import com.xcart.admin.R;

public class AboutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String LINK_PATTERN = "<a href='%s'> %s </a>";

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_about, container, false);

            TextView mobileAdmin = ((TextView)rootView.findViewById(R.id.xcart_mobile_admin));
            mobileAdmin.setMovementMethod(LinkMovementMethod.getInstance());
            mobileAdmin.setText(Html.fromHtml(String.format(LINK_PATTERN, getString(R.string.xcart_mobile_admin_link), getString(R.string.xcart_mobile_admin))));

            TextView help = ((TextView)rootView.findViewById(R.id.xcart_help));
            help.setMovementMethod(LinkMovementMethod.getInstance());
            help.setText(Html.fromHtml(String.format(LINK_PATTERN, getString(R.string.xcart_help_link), getString(R.string.xcart_help))));

            return rootView;
        }
    }

}
