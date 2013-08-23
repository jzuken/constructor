package com.example.adminshop;

public class Product {
	public Product(String id, String name, String available, String sold, String freeShippingValue, String price) {
		this.id = id;
		this.name = name;
		this.available = available;
		this.sold = sold;
		this.freeSheepingValue = freeShippingValue;
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAvailable() {
		return available;
	}

	public String getSold() {
		return sold;
	}
	
	public String getFreeShippingValue() {
		return freeSheepingValue;
	}

	public String getPrice() {
		return price;
	}

	private String id;
	private String name;
	private String available;
	private String sold;
	private String freeSheepingValue;
	private String price;
}