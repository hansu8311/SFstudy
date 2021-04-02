package com.tody.SF.common.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;


//두 오브젝트(UserDao, DConnectionMaker)사이의 런타임 의존관계를 성정해주는 의존관계 주입 작업을 주도하는 존재 : DI컨테이너
@Configuration
public class DaoFactory {
	
	@Bean
	public UserDaoJdbc userDao() {
		UserDaoJdbc userDao = new UserDaoJdbc();
		userDao.setDataSource(dataSource());
		return userDao;
	}
	
	@Bean 
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(oracle.jdbc.driver.OracleDriver.class);
		dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:orcl");
		dataSource.setUsername("C##hansu");
		dataSource.setPassword("tngh1228");
		
		
		return dataSource;
	}
}
