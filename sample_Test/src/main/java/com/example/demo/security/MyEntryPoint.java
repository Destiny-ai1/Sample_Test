package com.example.demo.security;

import java.io.*;

import org.springframework.security.core.*;
import org.springframework.security.web.*;
import org.springframework.stereotype.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

@Component
public class MyEntryPoint implements AuthenticationEntryPoint{
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		
		
		//ajax 요청일 경우 XMLHttpRequest란 값을 가지고
		// 아닐경우 null 을 가진다
		String ajaxHeader = request.getHeader("X-Requested-With");
		//boolean isAjax = ajaxHeader.equals("XMLHttpRequest");
		boolean isAjax = "XMLHttpRequest".equals(ajaxHeader);
		if(isAjax==true) {
			response.setStatus(401);
		}else {
			response.sendRedirect("/member/login");
		}
	}
}
