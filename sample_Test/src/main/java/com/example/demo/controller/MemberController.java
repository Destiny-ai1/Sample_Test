package com.example.demo.controller;

import java.io.*;
import java.nio.file.*;
import java.nio.file.Path;


import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.example.demo.dto.*;
import com.example.demo.service.*;




//@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;
	private final String imageSaveFolder = "c:/upload/";
	
	@GetMapping("/member/join-check")
	public ResponseEntity<String> joinCheck(String checkcode){
		return ResponseEntity.ok(checkcode);
	}
	
	@GetMapping("/")
	public ModelAndView index() {
		return new ModelAndView("index");
	}
	
	@PostMapping("/member/join")
	public ModelAndView join(@ModelAttribute MemberDto.Create dto) {
		boolean result = memberService.join(dto);
		if(result==false) {
			return new ModelAndView("redirect:/");
		}
		return new ModelAndView("redirect:/member/login");
	}
	
	/* Java.io는 한번에 하나씩 실행한다.
	 java.io는 작업속도가 느리니
	 blocking을 사용해서 여러군대에서 다같이 동시수행한다.
	 */
	@GetMapping("/member/profile")
	public ResponseEntity<byte[]> viewprofile(String profile){
		
		try {
			Path filePath = Paths.get(imageSaveFolder+profile);
			byte[] imageBytes = Files.readAllBytes(filePath);
			String mimetype = Files.probeContentType(filePath);
			MediaType mediaType = MediaType.parseMediaType(mimetype);
			return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(imageBytes);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(409).body(null);
	}
}
