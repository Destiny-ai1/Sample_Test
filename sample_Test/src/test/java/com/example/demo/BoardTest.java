package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.*;


import com.example.demo.dao.*;
import com.example.demo.dto.*;
import com.example.demo.entity.*;

//@SpringBootTest
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
}
