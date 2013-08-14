package com.example.adminshop;

public class Order {

	public Order(String id, String title, String date, String products, String status, String totalPrice) {
		this.id = id;
		this.title = title;
		this.date = date;
		this.products = products;
		this.status = status;
		this.totalPrice = totalPrice;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
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
	
	private String id;
	private String title;
	private String date;
	private String products;
	private String status;
	private String totalPrice;
}
