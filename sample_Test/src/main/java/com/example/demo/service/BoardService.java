package com.example.demo.service;


import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.example.demo.dao.*;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.exception.*;


@Service
public class BoardService {
	@Autowired
	private BoardDao boardDao;

	
	@Autowired
	private MemberBoardDao memberBoardDao;
	
	/*boardDao에서 sequence로 일련번호를 생성해서 글번호로 사용하게한다
	  boardDao에서 생성한 sequence를 서비스에서 사용하게할려면
	  마이바티스의  selectkey  를 사용한다
	  */
	
	public Long write(BoardDto.Create dto, String loginId) {
		board board = dto.toEntity(loginId);
		boardDao.save(board);
		return board.getBno();
		
	}

	public board read(Long bno, String loginId) {
		if(loginId!=null) {
			boolean 읽었는가= memberBoardDao.existsByBnoAndUsername(bno,loginId);
			if(읽었는가==false) {
				memberBoardDao.save(bno,loginId);
				boardDao.increaseReadCnt(bno);
			}
		}
		board board=boardDao.findById(bno).orElseThrow(()->new FailException("글을 찾을 수 없습니다."));
		if(board.isDeleted()==true)
			return board.글삭제();
		return board;
//		return boardDao.findById(bno).orElseThrow(()->new FailException("글을 찾을 수 없습니다."));
	}
	
	public List<BoardDto.read> findall(){
		return boardDao.findall();
	}
}
