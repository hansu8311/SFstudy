package com.tody.SF.common.service;



import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.tody.SF.common.dao.Interface.Hello;

public class UppercaseHandler implements InvocationHandler{
	Object target;
	
	public UppercaseHandler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String ret = (String)method.invoke(target, args);
		if(ret instanceof String && method.getName().startsWith("say")) {
			return ((String)ret).toUpperCase();
		}
		else {
			return ret;
		}
	}
	


}
