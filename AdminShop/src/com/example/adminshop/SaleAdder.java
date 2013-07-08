package com.example.adminshop;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class SaleAdder extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_sale);
		allButton = (RadioButton) findViewById(R.id.allRadioButton);
		allButton.setChecked(true);
		premiumButton = (RadioButton) findViewById(R.id.premiumRadioButton);
		wholesaleButton = (RadioButton) findViewById(R.id.wholesaleRadioButton);
		orderSubtotalEditor = (EditText) findViewById(R.id.orderSubtotalEditor);
		discountEditor = (EditText) findViewById(R.id.discountEditor);
		discountTypeSpinner = (Spinner) findViewById(R.id.discountTypeEditor);

		String[] data = { "Percent, %", "Absolute, $" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		discountTypeSpinner.setAdapter(adapter);
		discountTypeSpinner.setPrompt("Title");
		discountTypeSpinner.setSelection(0);
		discountTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				discountType = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	public void addSaleOkClick(View v) {
		if (Float.valueOf(discountEditor.getText().toString()) > 100.0 && discountType == 0) {
			Toast.makeText(getBaseContext(),"Скидка не может быть больше 100%", Toast.LENGTH_SHORT).show();
		}
	}

	private RadioButton allButton;
	private RadioButton premiumButton;
	private RadioButton wholesaleButton;
	private EditText orderSubtotalEditor;
	private EditText discountEditor;
	private Spinner discountTypeSpinner;
	private int discountType;
}
