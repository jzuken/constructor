package com.example.adminshop;

public class Discount {

	public Discount(String id, String orderSubtotal, String discount, String discountType, String membership) {
		this.id = id;
		this.orderSubtotal = orderSubtotal;
		this.discount = discount;
		this.discountType = discountType;
		this.membership = membership;
	}

	public String getId() {
		return id;
	}

	public String getOrderSubtotal() {
		return orderSubtotal;
	}

	public String getDiscount() {
		return discount;
	}

	public String getDiscountType() {
		return discountType;
	}

	public String getMembership() {
		return membership;
	}

	private String id;
	private String orderSubtotal;
	private String discount;
	private String discountType;
	private String membership;
}
