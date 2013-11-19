package com.xcart.admin.managers.network;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;

import java.util.List;

public abstract class PostRequester extends AsyncTask<String, Void, String> {

    public PostRequester(List<NameValuePair> nameValuePairs) {
        this.nameValuePairs = nameValuePairs;
    }

    private List<NameValuePair> nameValuePairs;
}
