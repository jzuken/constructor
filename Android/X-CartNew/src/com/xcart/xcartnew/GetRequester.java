package com.xcart.xcartnew;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class GetRequester extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... urls) {
		String url = new String(urls[0]);
		HttpClient client = new SSLDefaultHttpClient();
		HttpGet get = new HttpGet(url);

		try {
			HttpResponse responseGet = client.execute(get);
			HttpEntity resEntityGet = responseGet.getEntity();
			if (resEntityGet != null) {
				return EntityUtils.toString(resEntityGet);
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
}