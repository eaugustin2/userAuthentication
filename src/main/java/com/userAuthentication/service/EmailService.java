package com.userAuthentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired //injecting JavaMailSender into this service class
	private JavaMailSender javaMailSender; //sends emails
	
	@Async
	public void sendEmail(SimpleMailMessage email) {
		javaMailSender.send(email);
	}
	
}
