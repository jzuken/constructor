package com.xcart.xcartnew.model;

public class Product {
	public Product(String id, String name, String sku, String inStock, String price) {
		this.id = id;
		this.name = name;
		this.sku = sku;
		this.inStock = inStock;
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSku() {
		return sku;
	}

	public String getInStock() {
		return inStock;
	}

	public String getPrice() {
		return price;
	}

	private String id;
	private String name;
	private String sku;
	private String inStock;
	private String price;
}