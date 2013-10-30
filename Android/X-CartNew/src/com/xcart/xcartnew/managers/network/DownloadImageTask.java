package com.xcart.xcartnew.managers.network;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	public DownloadImageTask(ImageView image, ProgressBar progressBar) {
		this.image = image;
		this.progressBar = progressBar;
	}

	protected Bitmap doInBackground(String... urls) {
		String url = urls[0];
		HttpClient client = new SSLDefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);
		try {
			HttpResponse response = client.execute(getRequest);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					return bitmap;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void onPostExecute(Bitmap bitmap) {
		image.setImageBitmap(bitmap);
		progressBar.setVisibility(View.GONE);
	}

	private ImageView image;
	private ProgressBar progressBar;
}