package com.example.demo.util;

import org.springframework.beans.factory.annotation.*;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.*;

import jakarta.mail.internet.*;

@Component
public class MailUtil {
	@Autowired
	private JavaMailSender mailSender;
	private String 보내는사람 ="admin@icia.com";
	
	public void sendMail(String 받는사람, String 제목, String 내용) {
		//Mime : 메이을 파일 첨부할때, 파일을 구별하기 위해 만들어진 표준
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message,false,"utf-8");
			helper.setFrom(보내는사람);
			helper.setTo(받는사람);
			helper.setSubject(제목);
			helper.setText(내용,true);
			mailSender.send(message);
		}catch (jakarta.mail.MessagingException e) {
			e.printStackTrace();
		}
	}
}
