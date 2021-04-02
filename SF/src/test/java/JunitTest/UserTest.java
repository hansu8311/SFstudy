package JunitTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tody.SF.common.dto.Level;
import com.tody.SF.common.dto.User;

public class UserTest {

	User user;
	
	@Before
	public void setup() {
		user = new User();
	}
	
	@Test
	public void upgradeLevel() {
		Level[] levels = Level.values();
		
		for(Level level : levels) {
			if(level.nextLevel() == null) continue;
			user.setLevels(level);
			user.upgrageLevel();
			assertThat(user.getLevels(), is(level.nextLevel()));
		}
		
	}
	
	@Test(expected = IllegalStateException.class)
	public void cannotUpgradeLevel() {
		Level[] levels = Level.values();
		
		for(Level level : levels) {
			if(level.nextLevel() != null) continue;
			user.setLevels(level);
			user.upgrageLevel();
		}
		
	}
}
