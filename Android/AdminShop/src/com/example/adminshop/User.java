package com.example.adminshop;

public class User {
	public User(String id, String name, String login, String type, String lastLogin, String totalOrders) {
		this.id = id;
		this.name = name;
		this.login = login;
		this.type = type;
		this.lastLogin = lastLogin;
		this.totalOrders = totalOrders;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getLogin() {
		return login;
	}
	
	public String getType() {
		return type;
	}
	
	public String getLastLogin() {
		return lastLogin;
	}
	
	public String getTotalOrders() {
		return totalOrders;
	}
	
	private String id;
	private String name;
	private String login;
	private String type;
	private String lastLogin;
	private String totalOrders;
}