package com.example.adminshop;

public class Product {
	public Product(String id, String name, String available, String sold, String price) {
		this.id = id;
		this.name = name;
		this.available = available;
		this.sold = sold;
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

	public String getPrice() {
		return price;
	}

	private String id;
	private String name;
	private String available;
	private String sold;
	private String price;
}