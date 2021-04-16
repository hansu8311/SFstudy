package com.tody.SF.sqlService;

import javax.annotation.PostConstruct;

import com.tody.SF.exception.SqlRetrievalFailureException;

public class BaseSqlService implements SqlService{
	protected SqlRegistry sqlRegistry;
	protected SqlReader sqlReader;
		
	public void setSqlRegistry (SqlRegistry  sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}
	public void setSqlReader (SqlReader  sqlReader) {
		this.sqlReader = sqlReader;
	}
	@PostConstruct
	public void loadSql() {
		this.sqlReader.read(this.sqlRegistry);
	}
	
	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		try {
			return this.sqlRegistry.findSql(key);
		} catch (SqlNotFoundException e) {
			throw new SqlRetrievalFailureException(e);
		}
	}

}
