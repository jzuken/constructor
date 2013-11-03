package com.xcart.xcartnew.model;

public class User {
	public User(String id, String name, String login, String type, String lastLogin) {
		this.id = id;
		this.name = name;
		this.login = login;
		this.type = type;
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
	
	public String getType() {
		return type;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	private String id;
	private String name;
	private String login;
	private String type;
	private String lastLogin;
}