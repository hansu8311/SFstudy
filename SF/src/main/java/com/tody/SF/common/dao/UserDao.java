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
	public void deleteAll() throws SQLException{
		StatementStrategy st = new DeleteAllStatement();
		jdbcContextWithStatementStrategy(st);
	}
	public int getCount() throws ClassNotFoundException, SQLException{
		
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement("SELECT count(*) AS TCOUNT FROM users");
			
			rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			throw e;
		}finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if(ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			if(c != null) {
				try {
					c.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException{
		Connection c = null;
		 PreparedStatement ps = null;
		try {
			 c = dataSource.getConnection();

			 ps = stmt.makePreparedStatement(c);
			 
			 ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			if(ps!=null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
				
			}
			if(c != null) {
				try {
					c.close();
				} catch (SQLException e) {
				}
			}
		}
	
		 
		 ps.close(); 
		 c.close();
		 
	}
//	private PreparedStatement makeStatement(Connection c) throws SQLException{
//		PreparedStatement ps;
//		ps = c.prepareStatement("DELETE FROM Users");
//		return ps;
//	}
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
