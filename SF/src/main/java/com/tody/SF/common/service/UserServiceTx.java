package com.tody.SF.common.service;

import java.util.List;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.tody.SF.common.dao.Interface.UserService;
import com.tody.SF.common.dto.User;

public class UserServiceTx implements UserService{
	UserService userService;
	private PlatformTransactionManager transactionManager;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	@Override
	public void add(User user) {
		userService.add(user);
	}

	@Override
	public void upgradeLevels() {
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			
			userService.upgradeLevels();			
			
			this.transactionManager.commit(status);
		} catch (RuntimeException e) {
			this.transactionManager.rollback(status);
			throw e;
		} 
	}
	@Override
	public User get(String id) {
		return userService.get(id);
	}
	@Override
	public List<User> getAll() {
		return userService.getAll();
	}
	@Override
	public void deleteAll() {
		userService.deleteAll();
	}
	@Override
	public void update(User user) {
		userService.update(user);
	}

}
