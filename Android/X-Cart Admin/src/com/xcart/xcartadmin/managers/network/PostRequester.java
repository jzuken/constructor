package com.xcart.xcartadmin.managers.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.xcart.xcartadmin.managers.network.SSLDefaultHttpClient;

public abstract class PostRequester extends AsyncTask<String, Void, String> {

	public PostRequester(List<NameValuePair> nameValuePairs) {
		this.nameValuePairs = nameValuePairs;
	}

	private List<NameValuePair> nameValuePairs;
}
