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
	private JdbcContext jdbcContext;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setJdbcContext(JdbcContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}
	
	public void add(final User user) throws SQLException{
		 jdbcContext.workWithStatementStrategy(
				 new StatementStrategy() {//익명내부클래스
					@Override
					public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
						PreparedStatement ps = c.prepareStatement("insert into users (id, name, password) values(?,?,?)");
						ps.setString(1, user.getId());
						ps.setString(2, user.getName());
						ps.setString(3, user.getPassword());
						
						return ps;
					}
				});
	}
	
	public void deleteAll() throws SQLException{
		jdbcContext.workWithStatementStrategy(
				 new StatementStrategy() {
					@Override
					public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
						return c.prepareStatement("DELETE FROM Users");
					}
				});
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
}
