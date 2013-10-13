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
		String urldisplay = urls[0];
		Bitmap mIcon11 = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mIcon11;
	}

	protected void onPostExecute(Bitmap bitmap) {
		image.setImageBitmap(bitmap);
		progressBar.setVisibility(View.GONE);
	}

	private ImageView image;
	private ProgressBar progressBar;
}