package com.tody.SF.common.service;

import java.util.List;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.tody.SF.common.dao.Interface.UserDao;
import com.tody.SF.common.dao.Interface.UserService;
import com.tody.SF.common.dto.Level;
import com.tody.SF.common.dto.User;

public class UserServiceImpl implements UserService{
	UserDao userDao;
	MailSender mailSender;
	
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	@Override
	public void add(User user) {
		if(user.getLevels() == null) {
			user.setLevels(Level.BASIC);
		}
		userDao.add(user);
	}
	
	@Override
	public void upgradeLevels() {
		List<User> users = userDao.getAll();
		
		for (User user : users) {
			if(canUpgradeLevel(user)) {
				upgradeLevel(user);
			}
		}
	}
	private boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevels();
		switch (currentLevel) {
		case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
		case SILVER: return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
		case GOLD: return false;
		default: throw new IllegalArgumentException("Unknown Level : " + currentLevel);
		}
	}
	
	protected void upgradeLevel(User user) {
		user.upgrageLevel();
		userDao.update(user);
		sendUpgradeEmail(user);
	}
	
	private void sendUpgradeEmail(User user) {
		//Properties props = new Properties();
		//props.put("mail.smtp.host", "mail.ksug.org");
		//Session s = Session.getInstance(props, null);
		
		//MimeMessage message = new MimeMessage(s);	
			
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("useradmin@kung.org");
		mailMessage.setSubject("Upgrade안내");
		mailMessage.setText("사용자님의 등급이 " + user.getLevels().name() + "로 업그레이드되었습니다.");
			
		this.mailSender.send(mailMessage);
	}
	@Override
	public User get(String id) {
		return userDao.get(id);
	}
	@Override
	public List<User> getAll() {
		return userDao.getAll();
	}
	@Override
	public void deleteAll() {
		userDao.deleteAll();
	}
	@Override
	public void update(User user) {
		userDao.update(user);
	}
}
