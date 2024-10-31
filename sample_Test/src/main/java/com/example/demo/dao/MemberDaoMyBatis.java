package com.example.demo.dao;


import java.util.*;

import org.apache.ibatis.annotations.*;

import com.example.demo.entity.*;

//@Mapper
public interface MemberDaoMyBatis {
	// ID 사용여부
	@Select("select count(username) from member where username=#{username}")
	public int countByUsername(String username);
	
	//회원가입
	@Insert("insert into member values(#{username},#{password},#{email},#{joinday},#{birthday},#{profile},0,1,#{role})")
	public int save(Member member);
	
	//로그인실패
	@Update("update member set login_fail_count=login_fail_count+1 where username=#{username}")
	public int increaseLoginFailCount(String username);
	
	//로그인 실패시 블럭
	@Update("update member set login_fail_count=5,enabled=0 where username=#{username}")
	public int block(String username);
	
	//로그인 성공
	@Update("update member set login_fail_count=0 where username=#{username}")
	public int resetLoginfailCount(String username);
	
	// 내정보 불러오기
	@Select("select* from member where username=#{username}")
	public Optional<Member> findByUsername(String username);
	
	//아이디 찾는 방법
	@Select("select username from member where email=#{email}")
	public Optional<String> findUsernameByEmail(String email);
	
	//비밀번호 변경
	@Update("update member set passwrod=#{password} where username=#{username}")
	public int updatePassword(String password, String username);
	
	//사용자 정보 변경 (비밀번호와 이메일 변경)
	@Update("update member set password=#{password},email=#{email} where username=#{username}")
	public int update(String password, String email, String username);
	
	//사용자 삭제
	@Delete("delete from member where username=#{username}")
	public int deleteUsername(String username);
}
