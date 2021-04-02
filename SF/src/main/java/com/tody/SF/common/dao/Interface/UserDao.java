package com.tody.SF.common.dao.Interface;

import java.util.List;

import com.tody.SF.common.dto.User;

public interface UserDao {
	void add(User user);
	User get(String id);
	List<User> getAll();
	void deleteAll();
	int getCount();
	void update(User user);
}
