package com.example.demo.controller;

import java.security.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.exception.*;
import com.example.demo.service.*;

import jakarta.validation.*;

@Controller
public class BoardController {
	@Autowired
	private BoardService boardservice;
	
	
	@PreAuthorize("isAuthenticated")
	@GetMapping("/board/write")
	public ModelAndView ckEditor() {
		return new ModelAndView("board/write");
	}
	
	@PreAuthorize("isAuthenticated")
	@PostMapping("/board/write")
	public ModelAndView ckEditor작성(@Valid BoardDto.Create dto, BindingResult br, Principal principal) {
		Long bno=boardservice.write(dto,principal.getName());
		return new ModelAndView("redirect:/board/read?bno="+bno);
	}
	
	@GetMapping("/board/read")
	public ModelAndView read(Long bno, Principal principal) {
		String loginId= principal==null? null:principal.getName();
		board board= boardservice.read(bno, loginId);
		return new ModelAndView("board/read").addObject("result",board);
	}
	
	@GetMapping({"/","/board/list"})
	public ModelAndView list(@RequestParam(defaultValue = "1") Integer pageno) {
		return new ModelAndView("board/list").addObject("result",boardservice.findall(pageno));
	}
	
	@ExceptionHandler(FailException.class)
	public ModelAndView failExceptionHandler(FailException e) {
		return new ModelAndView("error/error").addObject("message",e.getMessage());
	}
}
