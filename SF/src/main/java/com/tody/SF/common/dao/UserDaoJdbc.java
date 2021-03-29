package com.tody.SF.common.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.tody.SF.common.dao.Interface.UserDao;
import com.tody.SF.common.dto.Level;
import com.tody.SF.common.dto.User;
import com.tody.SF.exception.DuplicateUserIdException;


public class UserDaoJdbc implements UserDao {
	
	private RowMapper<User> userMapper = new RowMapper<User>() {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setLevels(Level.valueOf(rs.getInt("levels")));
			user.setLogin(rs.getInt("login"));
			user.setRecommend(rs.getInt("recommend"));
			return user;
		}
	};
	
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void add(final User user) throws DuplicateKeyException{
			this.jdbcTemplate.update("insert into users (id, name, password, levels, login, recommend) values(?,?,?,?,?,?)", user.getId(), user.getName(), user.getPassword(),user.getLevels().intValue(),user.getLogin(),user.getRecommend());
	}
	
	public void deleteAll() {
		this.jdbcTemplate.update("delete from users");
	}
	
	public User get(String id) {
		return this.jdbcTemplate.queryForObject("SELECT * FROM users where id = ?", 
				new Object[] {id},
				this.userMapper);
	}

	public int getCount() {		
		return this.jdbcTemplate.queryForInt("SELECT count(*) AS TCOUNT FROM users");
	}
	
	public List<User> getAll() {
		return this.jdbcTemplate.query("SELECT * FROM users order by id", 
		this.userMapper);	
	}

	@Override
	public void update(User user) {
		this.jdbcTemplate.update("update users set name = ?, password = ?, levels = ?, login = ?, recommend = ? where id = ?", user.getName(), user.getPassword(),user.getLevels().intValue(),user.getLogin(),user.getRecommend(), user.getId());
	}

	
}
