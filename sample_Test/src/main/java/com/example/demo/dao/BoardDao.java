package com.example.demo.dao;

import java.util.*;

import org.apache.ibatis.annotations.*;

import com.example.demo.dto.*;
import com.example.demo.entity.*;

@Mapper
public interface BoardDao {
	public int save(board board);

	@Select("select bno, title, content, writer, to_char(write_time, 'yyyy-mm-dd hh24:mi:ss') as writeTime, read_cnt, good_cnt, bad_cnt, is_deleted from board where bno=#{bno}")
	public Optional<board> findById(Long bno);
	
	@Update("update board set read_cnt=read_cnt+1 where bno=#{bno} and rownum=1")
	public void increaseReadCnt(Long bno);
	
	public List<BoardDto.BoardList> findAll(Integer startRowNum, Integer endRowNum);
	
	@Select("select count(*) from board")
	public int count();
}
