package com.tody.SF;

public interface LineCallback<T> {
	T doSomethingWithLine(String line, T value);
}
