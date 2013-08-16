package com.example.adminshop;

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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class PostRequester extends AsyncTask<String, Void, String> {
	
	public PostRequester(List<NameValuePair> nameValuePairs) {
		this.nameValuePairs = nameValuePairs;
	}
	
	@Override
	protected String doInBackground(String... urls) {
		String url = new String(urls[0]);
		HttpClient client = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = client.execute(httppost);
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {
				return EntityUtils.toString(responseEntity);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<NameValuePair> nameValuePairs;
}
