package com.tody.SF.common.dto;

public class User {
	String id;
	String name;
	String password;
	
	Level levels;
	int login;
	int recommend;
	

	public User() {
	}
	public User(String id, String name, String password, Level levels, int login, int recommend) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.levels = levels;
		this.login = login;
		this.recommend = recommend;
		
	}
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Level getLevels() {
		return levels;
	}
	public void setLevels(Level levels) {
		this.levels = levels;
	}
	public int getLogin() {
		return login;
	}
	public void setLogin(int login) {
		this.login = login;
	}
	public int getRecommend() {
		return recommend;
	}
	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}
	
}
