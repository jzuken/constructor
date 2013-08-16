package com.example.adminshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class ProductAdder extends PinSupportActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_product);
		SharedPreferences authorizationData = getSharedPreferences("AuthorizationData", MODE_PRIVATE);
		String name = authorizationData.getString("loggedSiteName", "");
		((TextView) findViewById(R.id.providerName)).setText(name.toCharArray(), 0, name.length());
		allButton = (RadioButton) findViewById(R.id.allRadioButton);
		allButton.setChecked(true);
		premiumButton = (RadioButton) findViewById(R.id.premiumRadioButton);
		wholesaleButton = (RadioButton) findViewById(R.id.wholesaleRadioButton);
	}

	public void applyChangesClick(View v) {

	}

	public void uploadImageClick(View v) {
		Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		startActivityForResult(i, imageLoading);
	}

	public void uploadThumbnailClick(View v) {
		Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		startActivityForResult(i, thumbnailLoading);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (imageLoading == requestCode && resultCode == RESULT_OK && data != null) {
			((TextView) findViewById(R.id.noImageText)).setVisibility(View.GONE);
			String picturePath = getImagePath(data);
			ImageView imageView = (ImageView) findViewById(R.id.productImage);
			imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
		}

		if (thumbnailLoading == requestCode && resultCode == RESULT_OK && data != null) {
			((TextView) findViewById(R.id.noThumbnailText)).setVisibility(View.GONE);
			((TextView) findViewById(R.id.recommendedSizeText)).setVisibility(View.GONE);
			String picturePath = getImagePath(data);
			ImageView imageView = (ImageView) findViewById(R.id.thumbnailImage);
			imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
		}
	}

	private String getImagePath(Intent data) {
		Uri selectedImage = data.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();
		return picturePath;
	}

	private final int imageLoading = 0;
	private final int thumbnailLoading = 1;
	private RadioButton allButton;
	private RadioButton premiumButton;
	private RadioButton wholesaleButton;
}
