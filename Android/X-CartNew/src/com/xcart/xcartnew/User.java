package com.xcart.xcartnew;

public class User {
	public User(String id, String name, String login, String phone, String lastLogin) {
		this.id = id;
		this.name = name;
		this.login = login;
		this.lastLogin = lastLogin;
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
	
	public String getPhone() {
		return phone;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	private String id;
	private String name;
	private String login;
	private String phone;
	private String lastLogin;
}