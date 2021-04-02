package JunitTest;
import static com.tody.SF.common.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.tody.SF.common.service.UserService.MIN_RECCOMEND_FOR_GOLD;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.tody.SF.common.dao.MockUserDao;
import com.tody.SF.common.dao.Interface.UserDao;
import com.tody.SF.common.dao.Interface.UserService;
import com.tody.SF.common.dto.Level;
import com.tody.SF.common.dto.User;
import com.tody.SF.common.service.MockMailSender;

import com.tody.SF.common.service.UserServiceImpl;
import com.tody.SF.common.service.UserServiceTx;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {

	@Autowired UserService userService;
	@Autowired UserServiceImpl userServiceImpl;
	@Autowired UserDao userDao;
	@Autowired PlatformTransactionManager transactionManager;
	@Autowired MailSender mailSender;
	List<User> users;
	@Before
	public void setup() {
		users = Arrays.asList(
				new User("kimha", "김하", "kimhansu1",Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0,"test@naver.com"),
				new User("kimju", "김주", "kimjuhee1",Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0,"test@naver.com"),
				new User("kimso", "김소", "kimsoyoun1",Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD-1,"test@naver.com"),
				new User("kimsoyoun", "김소연", "kimsoyoun1",Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD,"test@naver.com"),
				new User("kimsoyoung", "김소영", "kimsoyoun1",Level.GOLD, 100, 100,"test@naver.com"));
	}
	
	@Test
	@DirtiesContext
	public void upgradeLevels() throws Exception{
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		MockUserDao mockUserDao = new MockUserDao(this.users);
		userServiceImpl.setUserDao(mockUserDao);
				
		MockMailSender mockMailSender = new MockMailSender();
		userServiceImpl.setMailSender(mockMailSender);
				
		userServiceImpl.upgradeLevels();
		
		List<User> updated = mockUserDao.getUpdated();
		assertThat(updated.size(), is(2));
		
		checkUserAndLevel(updated.get(0), "kimju",Level.SILVER);
		checkUserAndLevel(updated.get(1), "kimsoyoun",Level.GOLD);
		
		List<String> request = mockMailSender.getRequests();
		assertThat(request.size(), is(2));
		assertThat(request.get(0), is(users.get(1).getEmail()));
		assertThat(request.get(1), is(users.get(3).getEmail()));
		
	}
	
	@Test
	public void add() {
		userDao.deleteAll();
		
		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevels(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevels(), is(userWithLevel.getLevels()));
		assertThat(userWithoutLevelRead.getLevels(), is(userWithoutLevel.getLevels()));
		
	}
	
	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId());
		if(upgraded) {
			assertThat(userUpdate.getLevels(), is(user.getLevels().nextLevel()));
		}else {
			assertThat(userUpdate.getLevels(), is(user.getLevels()));
		}
		
	}
	private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
			assertThat(updated.getId(), is(expectedId));
			assertThat(updated.getLevels(), is(expectedLevel));
		
	}
	
	static class TestUserService extends UserServiceImpl {
		private String id;
		
		private TestUserService(String id) {
			this.id = id;
		}
		protected void upgradeLevel(User user) {
			if(user.getId().equals(this.id)) throw new TestUserServiceException();
			super.upgrageLevel(user);
		}
	}
	static class TestUserServiceException extends RuntimeException {
	}
	@Test
	public void upgradeAllOrNothing()throws Exception{
		UserServiceImpl testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(this.userDao);
		testUserService.setMailSender(mailSender);
		
		UserServiceTx  txUserService = new UserServiceTx();
		txUserService.setTransactionManager(transactionManager);
		txUserService.setUserService(testUserService);
		
		userDao.deleteAll();
		for(User user : users) 	{userDao.add(user);}
		
		try {
			txUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException e) {
		}
		
		checkLevelUpgraded(users.get(1), false);
	}
	

}
