package com.example.demo.dao;

import java.util.*;

import org.apache.ibatis.annotations.*;

import com.example.demo.entity.*;

@Mapper
public interface MemberDaoBatisWithXml {
	public int countByUsername(String username);
	
	public int update(Member member);	
	
	public Optional<Member> findByUsername(String username);
	
	public Optional<String> findByUsernameByemail(String email);
	
	public int deleteByUsername(String username);
	
	public int save(Member member);
}
