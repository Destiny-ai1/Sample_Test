package com.example.demo.security;

import java.io.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.*;
import org.springframework.stereotype.*;

import com.example.demo.dao.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

//@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	@Autowired
	private MemberDaoMyBatis memberDao;
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String username =request.getParameter("username");
		memberDao.resetLoginfailCount(username);
		response.sendRedirect("/");
	}
}
