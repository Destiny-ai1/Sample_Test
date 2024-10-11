package com.example.demo.controller;

import java.security.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.example.demo.dto.*;
import com.example.demo.service.*;

import jakarta.servlet.http.*;


@Controller
public class readmeController{
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberSampleService memberSampleService;
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/member/readme2")
	public ModelAndView readme2(HttpSession session, Principal principal){
		if(session.getAttribute("checkPassword")==null)
			return new ModelAndView("redirect:/member/check-password");
		MemberDto.Read dto = memberService.내정보보기(principal.getName());
		return new ModelAndView("/member/readme2").addObject("result",dto);
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/member/update2")
	public ModelAndView update2(MemberUpdateSampleDto dto, Principal principal) {
		memberSampleService.update2(dto, principal.getName());
		return new ModelAndView("redirect:/member/readme2");
	}
}	
