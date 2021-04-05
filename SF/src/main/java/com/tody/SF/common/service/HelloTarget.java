package com.tody.SF.common.service;

import com.tody.SF.common.dao.Interface.Hello;

public class HelloTarget implements Hello{

	@Override
	public String sayHello(String name) {
		return "Hello " + name;
	}

	@Override
	public String sayHi(String name) {
		return "Hi " + name;
	}

	@Override
	public String sayThankYou(String name) {
		return "Thank You " + name;
	}
	

}
