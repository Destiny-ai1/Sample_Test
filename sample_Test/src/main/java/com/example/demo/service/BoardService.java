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

	public BoardDto.Read read(Long bno, String loginId) {
		BoardDto.Read dto=boardDao.findById(bno).orElseThrow(()->new FailException("글을 찾을 수 없습니다."));
		// 3. 삭제된 글이라면 본문 내용을 삭제된 글입니다로 변경
		if(dto.getIsDeleted()==true)
			return dto.글삭제();
				
		// 1. 비로그인이거나 내가 작성한 글이면 조회수 변경 없음
		if(loginId==null || dto.getWriter().equals(loginId))
			return dto;
	
		// 2. 로그인했고 남이 작성한 글인 경우 조회수 증가 작업 시작
		//    (이미 읽은 글이 아닌 경우에만)
		boolean 이미읽었는가 = memberBoardDao.existsByBnoAndUsername(bno, loginId);
		if(이미읽었는가==false) {
			memberBoardDao.save(bno, loginId);
			boardDao.increaseReadCnt(bno);
			dto.조회수증가();
		}
		
		return dto;
	}
	
	@Value("10")
	private int Page_SIZE;
	@Value("5")
	private int BLOCK_SIZE;
	
	public BoardDto.page findall(Integer pageno){
		int countOfBoard = boardDao.count();
		int numberofPage = (countOfBoard-1)/10 + 1;
		
		int prev = (pageno-1)/BLOCK_SIZE * BLOCK_SIZE;
		int start = prev+1;
		int end = prev+ BLOCK_SIZE;
		int next = end+1;
		
		if(end>=numberofPage) {
			end = numberofPage;
			next = 0;
		}
		
		int startRowNum = (pageno-1)* Page_SIZE + 1;
		int endRowNum = startRowNum + Page_SIZE -1;
		List<BoardDto.BoardList> boards = boardDao.findAll(startRowNum, endRowNum);
		
		return new BoardDto.page(prev, start ,end , next, pageno, boards);
	}
}
