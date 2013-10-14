package com.xcart.xcartnew.model;

public class Review {

	public Review(String id, String email, String product, String message) {
		this.id = id;
		this.email = email;
		this.product = product;
		this.message = message;
	}

	public String getId() {
		return id;
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
	private String email;
	private String product;
	private String message;
}
