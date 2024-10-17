package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.*;

import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.enums.*;

@SpringBootTest
public class MemberDaoTest {
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private MemberBoardDao memberBoardDao;
	
//	@org.junit.jupiter.api.Test
	public void Test() {
		Member m = new Member("Spring", passwordEncoder.encode("1234"),
				"spring@naver.com", LocalDate.now(), LocalDate.of(2024,9,26), "default.jpg",0,
				false, Role.USER);
		assertEquals(1, memberDao.save(m));
	}
	
//	@org.junit.jupiter.api.Test
	public void passwordReset() {
		Member m= memberDao.findById("spring").get();
		m.changePassword(passwordEncoder.encode("111111"));
	}
	
//	@org.junit.jupiter.api.Test
	public void test2() {
		assertNotNull(memberBoardDao);
	}
	
//	@org.junit.jupiter.api.Test
	public void test3() {
		int result = memberBoardDao.save(115L, "Spring");
        assertEquals(1, result);
	}
	
//	@org.junit.jupiter.api.Test
	public void test4() {
		memberBoardDao.save(115L, "spring");
		assertEquals(true,memberBoardDao.existsByBnoAndUsername(111L, "spring"));
		assertEquals(false,memberBoardDao.existsByBnoAndUsername(222L, "summer"));
	}
}
