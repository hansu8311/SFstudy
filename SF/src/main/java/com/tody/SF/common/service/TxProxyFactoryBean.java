package com.tody.SF.common.service;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

public class TxProxyFactoryBean implements FactoryBean<Object>{
	private Object target;
	private PlatformTransactionManager transactionManager;
	private String pattern;
	Class<?> serviceInterface;
	
	public void setTarget(Object target) {
		this.target = target;
	}
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public void setServiceInterface(Class<?> serviceInterface) {
		this.serviceInterface = serviceInterface;
	}
	
	@Override
	public Object getObject() throws Exception {
		TransactionHandler txHandler = new TransactionHandler();
		txHandler.setPattern(pattern);
		txHandler.setTransactionManager(transactionManager);
		txHandler.setTarget(target);
		
		return Proxy.newProxyInstance(
				getClass().getClassLoader()
				, new Class[] {serviceInterface}
				, txHandler);
	}

	@Override
	public Class<?> getObjectType() {
		return serviceInterface;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
}
