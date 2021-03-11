package com.tody.SF.common.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import com.tody.SF.common.dto.User;

public class UserDao {
	private DataSource dataSource;//초기에 설정하면 사용중에는 바뀌지 않는 읽기전용 인스턴스 변수
	
//	public UserDao(ConnectionMaker connectionMaker) {
//		//commectionMaker = new DConnectionMaker();
//		this.commectionMaker = connectionMaker;
//	}
	//수정자 메소드 생성
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public void add(User user) throws SQLException{
		
		 Connection c = dataSource.getConnection();
		 
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
		
		 Connection c = dataSource.getConnection();
		 
		 PreparedStatement ps = c.prepareStatement(
				 "SELECT * FROM users where id = ?");
		 ps.setString(1, id);
	
		 ResultSet rs = ps.executeQuery();
		 
		 User user = null;
		 if(rs.next()) {
			 user = new User();
			 user.setId(rs.getString("id"));
			 user.setName(rs.getString("name"));
			 user.setPassword(rs.getString("password"));
		 }
		 
		 rs.close();
		 ps.close();
		  
		 c.close();
		 
		 if(user==null) throw new EmptyResultDataAccessException(1);
		 
		 return user;
	}
	public void deleteAll() throws ClassNotFoundException, SQLException{
		
		 Connection c = dataSource.getConnection();
		 
		 PreparedStatement ps = c.prepareStatement(
				 "DELETE FROM Users");
		 ps.executeUpdate();	
		 
		 ps.close(); 
		 c.close();
		 
	}
	public Integer getCount() throws ClassNotFoundException, SQLException{
		
		 Connection c = dataSource.getConnection();
		 
		 PreparedStatement ps = c.prepareStatement(
				 "SELECT count(*) AS TCOUNT FROM users");
		 
		 ResultSet rs = ps.executeQuery();
		 
		 rs.next();
		 
		 Integer count = rs.getInt(1);
		 
		 rs.close();
		 ps.close();
		 c.close();
		 
		 return count;
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
