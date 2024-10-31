package com.example.demo;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import com.example.demo.dao.*;

//@SpringBootTest
public class CategoryTest {
	@Autowired
	private CategoryDao dao;
	
//	@Test
	public void findall() {
		dao.findall().forEach(c->System.out.println(c));
	}
}
