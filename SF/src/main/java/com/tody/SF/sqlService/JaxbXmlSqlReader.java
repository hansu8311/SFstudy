package com.tody.SF.sqlService;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.tody.SF.common.dao.UserDaoJdbc;
import com.tody.SF.sqlService.jaxb.SqlType;
import com.tody.SF.sqlService.jaxb.Sqlmap;

public class JaxbXmlSqlReader implements SqlReader{
	private static final String DEFAULT_SQLMAP_FILE = "sqlmap.xml";
	
	private String sqlmapFile = DEFAULT_SQLMAP_FILE;
	
	public void setSqlmapFile(String sqlmpaFile) {
		this.sqlmapFile = sqlmpaFile;
	}
	
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
