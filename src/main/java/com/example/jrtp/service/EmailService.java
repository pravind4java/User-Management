package com.example.jrtp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String fromEmail;

	private Logger logger = LoggerFactory.getLogger(EmailService.class);

	public boolean isEmailSend(String to, String subject, String body) {

		SimpleMailMessage mail = new SimpleMailMessage();

		try {

			mail.setTo(to);
			mail.setSubject(subject);
			mail.setText(body);
			mail.setFrom(fromEmail);
			mailSender.send(mail);
			logger.info("Email send successfully to: {}", to);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
}
