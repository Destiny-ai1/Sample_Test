package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.security.crypto.password.*;

import com.example.demo.dao.*;
import com.example.demo.entity.*;



//@SpringBootTest
public class MamberDaoWithXmlTest {
	@Autowired
	public MemberDaoBatisWithXml dao;
	
	@Autowired
	public PasswordEncoder encoder;
	
//	@Test
	public void intest() {
		assertNotNull(dao);
	}
/* invalid bound statement 에러는 파일이름이나 xml 설정 오타오류 ★*/
//	@Test
	public void countByUsername() {
		System.out.println(dao.countByUsername("spring"));
	}
	
//	@Test
	public void updateTest() {
		dao.update(Member.builder().loginFailCount(2).username("spring").build());
		dao.update(Member.builder().loginFailCount(5).enabled(false).username("spring").build());
		dao.update(Member.builder().password(encoder.encode("1234")).username("spring").build());
		dao.update(Member.builder().enabled(true).username("spring").build());
	}
}
