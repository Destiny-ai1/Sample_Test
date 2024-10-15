package com.example.demo.service;


import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.example.demo.dao.*;
import com.example.demo.dto.*;
import com.example.demo.entity.*;


@Service
public class BoardService {
	@Autowired
	private BoardDao boardDao;
	
	/*boardDao에서 sequence로 일련번호를 생성해서 글번호로 사용하게한다
	  boardDao에서 생성한 sequence를 서비스에서 사용하게할려면
	  마이바티스의  selectkey  를 사용한다
	  */
	
	public Long write(BoardDto.Create dto, String loginId) {
		board board = dto.toEntity(loginId);
		return board.getBno();
	}
}
