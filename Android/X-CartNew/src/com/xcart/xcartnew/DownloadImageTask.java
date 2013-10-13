package com.xcart.xcartnew;

import java.io.InputStream;

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
		Bitmap resultBitmap = null;
		try {
			InputStream inStream = new java.net.URL(url).openStream();
			resultBitmap = BitmapFactory.decodeStream(inStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultBitmap;
	}

	protected void onPostExecute(Bitmap bitmap) {
		image.setImageBitmap(bitmap);
		progressBar.setVisibility(View.GONE);
	}

	private ImageView image;
	private ProgressBar progressBar;
}