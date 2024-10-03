package com.example.demo.security;

import java.io.*;
import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.*;
import org.springframework.stereotype.*;

import com.example.demo.dao.*;
import com.example.demo.entity.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.transaction.*;

@Component
public class LoginFailHandler implements AuthenticationFailureHandler{
	@Autowired
	private MemberDao memberDao;
	
	@Transactional
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String username = request.getParameter("username");
		Optional<Member> result= memberDao.findById(username);
		HttpSession session = request.getSession();
		
		if(result.isEmpty()) { // 아이디가 틀린경우
			session.setAttribute("message","ID를 다시한번 확인하세요.");
		}
		else { // 비밀번호가 틀린경우
			Member member = result.get();
			if(member.getEnabled()==false) {
				session.setAttribute("message","계정이 블록 되었습니다. 관리자에게 문의 해주세요");
			}else if(member.getLoginFailCount()==4) {
				member.block();
				session.setAttribute("message","로그인에 5회 실패하면 계정이 블록됩니다. 관리자에게 문의 해주세요");
			} else {
				member.increaseLoginFailCount();
				session.setAttribute("message","로그인에"+ member.getLoginFailCount()+"회 실패하셧습니다.");
			}
		}
		response.sendRedirect("/member/login");
	}
}
/* 로그인 실패시 Redirect해서 member/login 으로 이동후 메세지를 출력
 메세지를 출력하는게 RedirectAttributes => 컨트롤러에서 사용가능하니 세션에다가 오류메세지 저장
*/