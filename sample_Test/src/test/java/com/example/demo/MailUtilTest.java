package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import com.example.demo.util.*;

//@SpringBootTest
public class MailUtilTest {
	@Autowired
	private MailUtil util;
	
//	@org.junit.jupiter.api.Test
	public void Test() {
		assertNotNull(util);
	}
	
//	@org.junit.jupiter.api.Test
	public void sendMailTest() {
		util.sendMail("we8537@naver.com","테스트", "제대로 왔냐 ? ");
	}
	// 가짜 가입확인메일 테스트
//	@org.junit.jupiter.api.Test
	public void failMailTest() {
		String link = "<a href='http://localhost:8083/member/join-check?checkcode=1234'>가입확인</a>";
		util.sendMail("we8537@naver.com","테스트",link);
	}
}
