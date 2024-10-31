package com.example.demo.security;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import com.example.demo.dao.*;
import com.example.demo.entity.*;

@Component
public class MyUserDetailsService implements UserDetailsService {
	@Autowired
	private MemberDao memberDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Member m=memberDao.findById(username).orElseThrow(()->new UsernameNotFoundException("사용자를 찾을수 없습니다."));
		return User.builder().username(m.getUsername()).password(m.getPassword()).accountLocked(!m.getEnabled())
				.roles(m.getRole().name()).build();
	}
	
	
}
