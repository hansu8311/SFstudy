package com.tody.SF.sqlService;

import java.util.HashMap;
import java.util.Map;

import com.tody.SF.exception.SqlRetrievalFailureException;

public class HashMapSqlRegistry implements SqlRegistry {
	private Map<String, String> sqlMap = new HashMap<String, String>();
	@Override
	public void registerSql(String key, String sql) {
		sqlMap.put(key, sql);
	}

	@Override
	public String findSql(String key) throws SqlNotFoundException {
		String sql = this.sqlMap.get(key);
		
		if(sql == null)
			throw new SqlRetrievalFailureException(key +" 에 대한 SQL을 찾을 수 없습니다.");
		else
			return sql; 
	}
	
}
