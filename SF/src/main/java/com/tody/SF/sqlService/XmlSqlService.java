package com.tody.SF.sqlService;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.tody.SF.common.dao.UserDaoJdbc;
import com.tody.SF.common.dao.Interface.UserDao;
import com.tody.SF.exception.SqlRetrievalFailureException;
import com.tody.SF.sqlService.jaxb.SqlType;
import com.tody.SF.sqlService.jaxb.Sqlmap;

public class XmlSqlService implements SqlService, SqlRegistry, SqlReader{
	private Map<String, String> sqlMap = new HashMap<String, String>();
	
	private String sqlmapFile;
	
	private SqlRegistry sqlRegistry;
	private SqlReader sqlReader;
	
	public void setSqlmapFile(String sqlmpaFile) {
		this.sqlmapFile = sqlmpaFile;
	}
	
	public void setSqlRegistry (SqlRegistry  sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}
	
	public void setSqlReader(SqlReader sqlReader) {
		this.sqlReader = sqlReader;
	}
	@PostConstruct
	public void loadSql() {
		this.sqlReader.read(this.sqlRegistry);
	}
	

	//SqlService
	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		try {
			return this.sqlRegistry.findSql(key);
		} catch (SqlNotFoundException e) {
			throw new SqlRetrievalFailureException(e);
		}
		
	}
	//SqlRegistry
	@Override
	public void registerSql(String key, String sql) {
		sqlMap.put(key, sql);
	}

	@Override
	public String findSql(String key) {
		String sql = this.sqlMap.get(key);
		
		if(sql == null)
			throw new SqlRetrievalFailureException(key +" 에 대한 SQL을 찾을 수 없습니다.");
		else
			return sql; 
	}
	//SqlReader
	@Override
	public void read(SqlRegistry sqlRegistry) {
		String contextPath = Sqlmap.class.getPackage().getName();
		
		try {
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is = UserDaoJdbc.class.getResourceAsStream(this.sqlmapFile);
			Sqlmap sqlmap =(Sqlmap) unmarshaller.unmarshal(is);
					
			for(SqlType sql : sqlmap.getSql()) {
				sqlRegistry.registerSql(sql.getKey(), sql.getValue());
			}
	
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

}
