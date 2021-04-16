package com.tody.SF.sqlService;

public interface SqlRegistry {
	void registerSql(String key, String sql);
	
	String findSql(String key) throws SqlNotFoundException;
}
