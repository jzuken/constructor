package com.example.adminshop;

public class Order {

	public Order(String id, String date, String products, String status, String totalPrice, String details) {
		this.id = id;
		this.date = date;
		this.products = products;
		this.status = status;
		this.totalPrice = totalPrice;
		this.details = details;
	}

	public String getId() {
		return id;
	}

	public String getDate() {
		return date;
	}

	public String getProducts() {
		return products;
	}
	
	public String getStatus() {
		return status;
	}

	public String getTotalPrice() {
		return totalPrice;
	}
	
	public String getDetails() {
		return details;
	}
	
	private String id;
	private String date;
	private String products;
	private String status;
	private String totalPrice;
	private String details;
}
