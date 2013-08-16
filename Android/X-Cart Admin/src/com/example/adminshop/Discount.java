package com.example.adminshop;

public class Discount {

	public Discount(String id, String title, String orderSubtotal, String discount, String discountType,
			String membership) {
		this.id = id;
		this.title = title;
		this.orderSubtotal = orderSubtotal;
		this.discount = discount;
		this.discountType = discountType;
		this.membership = membership;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
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
	private String title;
	private String orderSubtotal;
	private String discount;
	private String discountType;
	private String membership;
}
