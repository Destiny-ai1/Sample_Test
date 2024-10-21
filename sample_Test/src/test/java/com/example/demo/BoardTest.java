package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import com.example.demo.dao.*;
import com.example.demo.dto.*;
import com.example.demo.entity.*;

@SpringBootTest
public class BoardTest {
	@Autowired
	private BoardDao boardDao;
	
//	@Test
	public void saveTest() {
		BoardDto.Create dto= new BoardDto.Create("연습","또연습");
		board board = dto.toEntity("Spring");
		boardDao.save(board);
		System.out.println(board);
		assertNotNull(board.getBno());
	}
	
//	@Test
	public void 게시판초기화() {
		List<String> writers = Arrays.asList("spring","summer","winnter");
		for(int i=1; i<=123; i++) {
			BoardDto.Create dto = new BoardDto.Create(i+"번 글", "연습");
			board board = dto.toEntity(writers.get(i%3));
			boardDao.save(board);
		}
	}
	
//	@Test
	public void findAllTest() {
		boardDao.findAll(11, 20).forEach(b->System.out.println(b));
	}
	
	@Test
	public void findByidtest() {
		BoardDto.Read dto = boardDao.findById(61L);
		System.out.println(dto);
	}
}
