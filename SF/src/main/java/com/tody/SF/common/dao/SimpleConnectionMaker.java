package com.tody.SF.common.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionMaker {
	public Connection makeNewConnection() throws ClassNotFoundException, SQLException{
		 Class.forName("oracle.jdbc.driver.OracleDriver");
		 Connection c = DriverManager.getConnection(
		 "jdbc:oracle:thin:@localhost:1521:orcl",
		 "C##hansu",
		 "tngh1228");
		 
		 return c;
	}
}
