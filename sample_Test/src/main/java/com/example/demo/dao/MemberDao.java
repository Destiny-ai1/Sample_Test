package com.example.demo.dao;

import java.util.*;

import org.springframework.data.jpa.repository.*;

import com.example.demo.entity.*;


public interface MemberDao extends JpaRepository<Member,String>{
	//Jpa가 설정해둔 방식으로 함수를 만들시 SQL을 생성
	public boolean existsByEmail(String email);
	
	// Jpa에서 함수로 지원되지않는 기능을 만들려면 JPQL 사용
	// Query로 오타량을 줄일수있다. 
	@Query("select m.username from Member m where m.email=:email")
	public Optional<String> findByUsernameByemail(String email);
	
	@Modifying
	@Query("update Member m set m.loginFailCount=0 where m.username=:username")
	public void resetLoginfailCount(String username);
}
