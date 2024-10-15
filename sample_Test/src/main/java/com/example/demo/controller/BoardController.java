package com.example.demo.controller;

import java.security.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.example.demo.dto.*;
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
		boardservice.write(dto,principal.getName());
		return null;
	}
}
