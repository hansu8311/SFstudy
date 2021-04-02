package com.tody.SF.common.dao.Interface;

import com.tody.SF.common.dto.User;

public interface UserService {
	void add(User user);
	void upgradeLevels();
}
