package com.tody.SF.common.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tody.SF.common.dto.User;

public class UserDao {
	public void add(User user) throws ClassNotFoundException, SQLException{
		 Class.forName("oracle.jdbc.driver.OracleDriver");
		 Connection c = DriverManager.getConnection(
		 "jdbc:oracle:thin:@localhost:1521:orcl",
		 "C##hansu",
		 "tndh1228");
		 
		 PreparedStatement ps = c.prepareStatement(
				 "insert into users (id, name, password) values(?,?,?)");
		 ps.setString(1, user.getId());
		 ps.setString(1, user.getName());
		 ps.setString(1, user.getPassword());
		 
		 ps.executeUpdate();
		 ps.close();
		 c.close();
	}
	
	public void get(String id) throws ClassNotFoundException, SQLException{
		 Class.forName("oracle.jdbc.driver.OracleDriver");
		 Connection c = DriverManager.getConnection(
		 "jdbc:oracle:thin:@localhost:1521:orcl",
		 "C##hansu",
		 "tndh1228");
		 
		 PreparedStatement ps = c.prepareStatement(
				 "SELET * FRO users whrer id = ?");
		 ps.setString(1, id);
	
		 ResultSet rs = ps.executeQuery();
		 
		 rs.next();
		 
		 User user = new User();
		 
		 user.setId(rs.getString("id"));
		 user.setName(rs.getString("name"));
		 user.setPassword(rs.getString("password"));
		 
		 rs.close();
		 ps.close();
		 c.close();
	}
}
