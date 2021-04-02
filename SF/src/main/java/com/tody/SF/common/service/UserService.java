package com.tody.SF.common.service;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.mail.Message;
import javax.mail.MessagingException;

import com.tody.SF.common.dao.Interface.UserDao;
import com.tody.SF.common.dto.Level;
import com.tody.SF.common.dto.User;

public class UserService {
	public UserDao userDao;
	
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;
	
	
	private PlatformTransactionManager transactionManager;
	private MailSender mailSender;
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void upgradeLevels() throws Exception{
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			upgradeLevelsInternal();
			
			this.transactionManager.commit(status);
		} catch (Exception e) {
			this.transactionManager.rollback(status);
			throw e;
		} 

	}
 
	public void add(User user) {
		if(user.getLevels() == null) {
			user.setLevels(Level.BASIC);
		}
		userDao.add(user);
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
	
	protected void upgrageLevel(User user) {
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
	private void upgradeLevelsInternal() {
		List<User> users = userDao.getAll();
		
		for (User user : users) {
			if(canUpgradeLevel(user)) {
				upgrageLevel(user);
			}
		}
	}
}
