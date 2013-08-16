package com.example.adminshop;

public class Review {

	public Review(String id, String title, String email, String product, String message) {
		this.id = id;
		this.title = title;
		this.email = email;
		this.product = product;
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getEmail() {
		return email;
	}

	public String getProduct() {
		return product;
	}

	public String getMessage() {
		return message;
	}

	private String id;
	private String title;
	private String email;
	private String product;
	private String message;
}
