package com.tody.SF.common.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tody.SF.common.dto.User;

public class UserDao {
	private SimpleConnectionMaker simpleCommectionMaker;
	
	public UserDao() {
		simpleCommectionMaker = new SimpleConnectionMaker();
	}
	public void add(User user) throws ClassNotFoundException, SQLException{
		
		 Connection c = simpleCommectionMaker.makeNewConnection();
		 
		 PreparedStatement ps = c.prepareStatement(
				 "insert into users (id, name, password) values(?,?,?)");
		 ps.setString(1, user.getId());
		 ps.setString(2, user.getName());
		 ps.setString(3, user.getPassword());
		 
		 ps.executeUpdate();
		 
		 ps.close();
		 c.close();
	}
	
	public User get(String id) throws ClassNotFoundException, SQLException{
		 Connection c = simpleCommectionMaker.makeNewConnection();
		 
		 PreparedStatement ps = c.prepareStatement(
				 "SELECT * FROM users where id = ?");
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
		 
		 return user;
	}
//	// 중복되는 DB Connection 메소드 추출
//	public Connection getConnection() throws ClassNotFoundException, SQLException{
//		 Class.forName("oracle.jdbc.driver.OracleDriver");
//		 Connection c = DriverManager.getConnection(
//		 "jdbc:oracle:thin:@localhost:1521:orcl",
//		 "C##hansu",
//		 "tngh1228");
//		 
//		 return c;
//	} --> SimpleConnectionMaker클래스 makeNewConnection로 분리
}
