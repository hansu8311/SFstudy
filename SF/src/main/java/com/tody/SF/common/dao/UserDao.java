package com.tody.SF.common.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.tody.SF.common.dto.User;

public class UserDao {
	
	private RowMapper<User> userMapper = new RowMapper<User>() {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			return user;
		}
	};
	
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {

	}
	
	public void add(final User user) throws SQLException{
		this.jdbcTemplate.update("insert into users (id, name, password) values(?,?,?)", user.getId(), user.getName(), user.getPassword());
	}
	
	public void deleteAll() {
		this.jdbcTemplate.update("delete from users");
	}
	
	public User get(String id) throws ClassNotFoundException, SQLException{
		return this.jdbcTemplate.queryForObject("SELECT * FROM users where id = ?", 
				new Object[] {id},
				this.userMapper);
	}

	public int getCount() throws ClassNotFoundException, SQLException{		
		return this.jdbcTemplate.queryForInt("SELECT count(*) AS TCOUNT FROM users");
	}
	
	public List<User> getAll() throws ClassNotFoundException, SQLException{
		return this.jdbcTemplate.query("SELECT * FROM users order by id", 
		this.userMapper);	
	}
}
