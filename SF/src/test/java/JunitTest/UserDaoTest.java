package JunitTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.awt.List;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tody.SF.common.dao.UserDaoJdbc;
import com.tody.SF.common.dao.Interface.UserDao;
import com.tody.SF.common.dto.Level;
import com.tody.SF.common.dto.User;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserDaoTest {
	@Autowired
	private UserDao dao;
	@Autowired DataSource dataSource;
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setup() {
		//ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");

		this.user1 = new User("kimhansu", "김한수", "kimhansu1",Level.BASIC, 1, 0,"test@naver.com");
		this.user2 = new User("kimjuhee", "김주희", "kimjuhee1",Level.SILVER, 55, 10,"test@naver.com");
		this.user3 = new User("kimsoyoun", "김소연", "kimsoyoun1",Level.GOLD, 100, 40,"test@naver.com");
		//this.dao = context.getBean("userDao", UserDao.class);
	}
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException{

		//UserDao dao = new DaoFactory().userDao();//DaoFactory를 이용한 예시
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		User userget1 = dao.get(user1.getId());
		checkSameUser(userget1,user1);
		
		User userget2 = dao.get(user2.getId());
		checkSameUser(userget2,user2);
	}
	@Test
	public void count() throws SQLException, ClassNotFoundException{		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(user1);		
		assertThat(dao.getCount(), is(1));
		
		dao.add(user2);		
		assertThat(dao.getCount(), is(2));
		
		dao.add(user3);		
		assertThat(dao.getCount(), is(3));
	}
	@Test(expected = EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException, ClassNotFoundException{
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.get("fail");

	}
	@Test
	public void getAll() throws SQLException, ClassNotFoundException{		
		dao.deleteAll();
		
		java.util.List<User> users0 = dao.getAll();
		assertThat(users0.size(), is(0));
		
		dao.add(user1);
		java.util.List<User> users1 = dao.getAll();
		assertThat(users1.size(), is(1));
		checkSameUser(user1,users1.get(0));
		
		dao.add(user2);
		java.util.List<User> users2 = dao.getAll();
		assertThat(users2.size(), is(2));
		checkSameUser(user1,users2.get(0));
		checkSameUser(user2,users2.get(1));
		
		dao.add(user3);
		java.util.List<User> users3 = dao.getAll();
		assertThat(users3.size(), is(3));
		checkSameUser(user1,users3.get(0));
		checkSameUser(user2,users3.get(1));
		checkSameUser(user3,users3.get(2));
		
	}
	@Test(expected = DataAccessException.class)
	public void duplicateKey() {
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user1);
		
		
	}
	
	@Test
	public void update() {
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user2);
		
		user1.setName("오민규");
		user1.setPassword("1231231");
		user1.setLevels(Level.GOLD);
		user1.setLogin(100);
		user1.setRecommend(888);
		
		dao.update(user1);
		
		User user1update = dao.get(user1.getId());
		checkSameUser(user1, user1update);
		User user2same = dao.get(user2.getId());
		checkSameUser(user2, user2same);
		
		
	}
//	@Test
//	public void sqlExceptionTranslate() {
//		dao.deleteAll();
//		try {
//			dao.add(user1);
//			dao.add(user1);	
//		}catch(DuplicateKeyException ex){
//			SQLException sqlEx = (SQLException)ex.getRootCause();
//			SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
//			
//			assertThat(set.translate(null, null, sqlEx), is(DuplicateKeyException.class));
//		}
//
//	}
	private void checkSameUser(User user1, User user2) {
		assertThat(user1.getId(), is(user2.getId()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
		assertThat(user1.getLevels(), is(user2.getLevels()));
		assertThat(user1.getLogin(), is(user2.getLogin()));
		assertThat(user1.getRecommend(), is(user2.getRecommend()));
	}
}
