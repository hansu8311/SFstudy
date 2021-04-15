package com.tody.SF.sqlService;

import com.tody.SF.exception.SqlRetrievalFailureException;

public interface SqlService {
	String getSql(String key) throws SqlRetrievalFailureException;
}
