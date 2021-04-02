package com.tody.SF.common.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class DumyMailSender implements MailSender {

	@Override
	public void send(SimpleMailMessage simpleMessage) throws MailException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(SimpleMailMessage[] simpleMessages) throws MailException {
		// TODO Auto-generated method stub
		
	}

}
