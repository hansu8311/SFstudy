package com.tody.SF.pointcut;

public interface TargetInterface {
	void hello();
	void hello(String a);
	int minus (int a, int b) throws RuntimeException;
	int plus(int a, int b);
}
