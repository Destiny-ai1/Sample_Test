package com.example.demo.dao;

import org.apache.ibatis.annotations.*;


@Mapper
public interface MemberBoardDao{
	@Select("select count(*) from member_read_board where bno=#{bno} and username=#{loginId} and rownum=1")
	public boolean existsByBnoAndUsername(@Param("bno") Long bno,@Param("loginId") String loginId); 
	
	@Insert("insert into member_read_board (bno, username) values (#{bno}, #{loginId})")
	public int save(Long bno, String loginId);
}
