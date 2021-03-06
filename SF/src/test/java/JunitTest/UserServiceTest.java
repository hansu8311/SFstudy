package JunitTest;
import static com.tody.SF.common.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.tody.SF.common.service.UserService.MIN_RECCOMEND_FOR_GOLD;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.tody.SF.common.dao.Interface.UserDao;
import com.tody.SF.common.dao.Interface.UserService;
import com.tody.SF.common.dto.Level;
import com.tody.SF.common.dto.User;
import com.tody.SF.common.service.UserServiceImpl;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class UserServiceTest {
	
	//@Autowired ApplicationContext context;
	@Autowired UserService userService;
	@Autowired UserService testUserService;
	//@Autowired UserServiceImpl userServiceImpl;
	@Autowired UserDao userDao;
	@Autowired PlatformTransactionManager transactionManager;
	//@Autowired MailSender mailSender;
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
		
		UserDao mockUserDao = mock(UserDao.class);
		when(mockUserDao.getAll()).thenReturn(this.users);
		userServiceImpl.setUserDao(mockUserDao);
		
		MailSender mockMailSender = mock(MailSender.class);
		userServiceImpl.setMailSender(mockMailSender);
				
		userServiceImpl.upgradeLevels(); 
		
		verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao).update(users.get(1)); 
		assertThat(users.get(1).getLevels(), is(Level.SILVER));
		verify(mockUserDao).update(users.get(3));
		assertThat(users.get(3).getLevels(), is(Level.GOLD));

		ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
		
		verify(mockMailSender, times(2)).send(mailMessageArg.capture()); 
		
		List<SimpleMailMessage>  mailMessages = mailMessageArg.getAllValues();
		assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
		assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));
		
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
		private String id = "kimsoyoun";

		protected void upgradeLevel(User user) {
			if(user.getId().equals(this.id)) throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
		
		public List<User> getAll(){
			for(User user : super.getAll()) {
				super.update(user);
			}
			return null;
		}
	}
	static class TestUserServiceException extends RuntimeException {
	}
//	
//	@Test
//	public void upgradeAllOrNothing()throws Exception{
//
//		userDao.deleteAll();
//		for(User user : users) 	{userDao.add(user);}
//		
//		try {
//			this.testUserService.upgradeLevels();
//			fail("TestUserServiceException expected");
//		} catch (TestUserServiceException e) {
//		}
//		
//		checkLevelUpgraded(users.get(1), false);
//	}
	@Test
	public void readOnlyTransactionAttribute(){

		this.testUserService.getAll();
	}
	@Test
	public void transactionSync(){

		userService.deleteAll();
		userService.add(users.get(0));
		userService.add(users.get(1));	

	}

}
