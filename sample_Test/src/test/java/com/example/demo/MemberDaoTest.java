package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.*;

import com.example.demo.dao.*;
import com.example.demo.entity.*;

@SpringBootTest
public class MemberDaoTest {
	@Autowired
	private MemberDaoMyBatis memberDao;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
//	@Test
	public void Test() {
		Member m = new Member("Spring", passwordEncoder.encode("1234"),
				"spring@naver.com", LocalDate.now(), LocalDate.of(2024,9,26), "default.png",0,
				false, Role.user);
		assertEquals(1, memberDao.save(m));
	}
}
