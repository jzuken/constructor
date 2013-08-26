package com.example.XCartAdmin;

public class TopProduct {
	public TopProduct(String name, String id, String code, String sold) {
		this.name = name;
		this.id = id;
		this.code = code;
		this.sold = sold;
	}

	public String getName() {
		return name;
	}
	
	public String getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getSold() {
		return sold;
	}

	private String name;
	private String id;
	private String code;
	private String sold;
}
