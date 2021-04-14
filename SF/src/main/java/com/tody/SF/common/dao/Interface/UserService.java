package com.tody.SF.common.dao.Interface;

import java.util.List;

import com.tody.SF.common.dto.User;

public interface UserService {
	void add(User user);
	User get(String id);
	List<User> getAll();
	void deleteAll();
	void update(User user); 
	void upgradeLevels();
}
