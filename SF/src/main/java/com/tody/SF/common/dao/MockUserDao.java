package com.tody.SF.common.dao;

import java.util.ArrayList;
import java.util.List;

import com.tody.SF.common.dao.Interface.UserDao;
import com.tody.SF.common.dto.User;

public class MockUserDao implements UserDao{
	private List<User> users;
	private List<User> updated = new ArrayList();
	
	public MockUserDao(List<User> users) {
		this.users = users;
	}
	public List<User> getUpdated() {
		return this.updated;		
	}
	@Override
	public List<User> getAll() {
		return this.users;		
	}
	@Override
	public void update(User user) {
		updated.add(user);
	}
	@Override
	public void add(User user) {throw new UnsupportedOperationException();}
	@Override
	public User get(String id) {throw new UnsupportedOperationException();}
	@Override
	public void deleteAll() {throw new UnsupportedOperationException();}
	@Override
	public int getCount() {	throw new UnsupportedOperationException();}
	
}
