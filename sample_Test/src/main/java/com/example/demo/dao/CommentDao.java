package com.example.demo.dao;

import java.util.*;

import org.apache.ibatis.annotations.*;

import com.example.demo.entity.*;


@Mapper
public interface CommentDao {
	
	@Insert("insert into comments(cno, content, writer, write_time, bno) values(comments_seq.nextval,#{content},#{writer},sysdate,#{bno})")
	public Integer saveComment(Long bno, String content, String writer);

	@Select("select * from comments where bno=#{bno} order by cno desc")
	public List<Comment> findByBno(Long bno);
	
	@Delete("delete from comments where cno=#{cno} and writer=#{writer} and rownum=1")
	public Integer deleteByCnoAndWriter(Long cno, String writer);

}




