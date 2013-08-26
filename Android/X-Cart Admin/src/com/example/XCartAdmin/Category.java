package com.example.XCartAdmin;

public class Category {
	public Category(String name, String id, String sold) {
		this.name = name;
		this.id = id;
		this.sold = sold;
	}

	public String getName() {
		return name;
	}
	
	public String getId() {
		return id;
	}

	public String getSold() {
		return sold;
	}

	private String name;
	private String id;
	private String sold;
}
