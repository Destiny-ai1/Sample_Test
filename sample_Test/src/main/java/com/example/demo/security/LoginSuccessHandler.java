package com.example.demo.security;

import java.io.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.*;
import org.springframework.stereotype.*;

import com.example.demo.dao.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.transaction.*;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	@Autowired
	private MemberDao memberDao;
	
	@Transactional
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String username =request.getParameter("username");
		String password =request.getParameter("password");		
		memberDao.resetLoginfailCount(username);
		
		//임시비밀번호로 로그인 성공
		if(password.length()==20) {
			HttpSession session = request.getSession();
			session.setAttribute("message","임시비밀번호로 로그인 하셧습니다. 비밀번호를 변경해주세요");
			session.setAttribute("isTemporaryPasswordUsed", true);
			response.sendRedirect("/member/update-password");
		}else {
			response.sendRedirect("/");
		}
	}
}
