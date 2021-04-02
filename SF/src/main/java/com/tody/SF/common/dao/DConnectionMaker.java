package com.tody.SF.common.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker{

	@Override
	public Connection makeConnection() throws ClassNotFoundException, SQLException {
		// D사 Connection 코드
		 Class.forName("oracle.jdbc.driver.OracleDriver");
		 Connection c = DriverManager.getConnection(
		 "jdbc:oracle:thin:@localhost:1521:orcl",
		 "C##hansu",
		 "tngh1228");
		 
		 return c;
	}

}
