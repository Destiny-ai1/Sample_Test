package com.example.demo.controller;

import java.io.*;
import java.nio.file.*;
import java.nio.file.Path;
import java.security.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.validation.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.support.*;

import com.example.demo.dto.*;
import com.example.demo.enums.*;
import com.example.demo.service.*;

import jakarta.servlet.http.*;
import jakarta.validation.*;



@Validated
@Controller
public class MemberController {
	@Autowired
	private MemberService memberService;
	private final String imageSaveFolder = "c:/upload/";
	
	@GetMapping("/member/join-check")
	public ResponseEntity<String> joinCheck(String checkcode){
		return ResponseEntity.ok(checkcode);
	}
	
	@PreAuthorize("isAnonymous()")
	@GetMapping("/member/login")
	public ModelAndView login() {
		return new ModelAndView("member/login");
	}
	
	@PreAuthorize("isAnonymous()")
	@GetMapping("/member/join")
	public ModelAndView join() {
		return new ModelAndView("member/join");
	}
	
	@PreAuthorize("isAnonymous()")
	@PostMapping("/member/join")                                 
	public ModelAndView join(@ModelAttribute @Valid MemberDto.Create dto, BindingResult br) {
		 memberService.join(dto);
		 return new ModelAndView("redirect:/member/login");
	}		//Valid 검증하는 어노테이션 후에 BindingResult가 Vaild 뒤에 와야된다
	
	//찾기 
	@PreAuthorize("isAnonymous()")
	@GetMapping("/member/find")
	public ModelAndView find() {
		return new ModelAndView("member/find");
	}
	
	//임시 비밀번호로 로그인하면 비밀번호 변경 page로 보내자
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/member/update-password")
	public ModelAndView update_password(HttpSession session){
	/* 1회성 메세지를 출력할려면 스프링 RedirectAttributes
	   출력할 메세지를 저장하는 곳이 loginSuccesHandler(스프링 컨트롤러가 아니여서 RedirectAttributes 사용불가능) 
	   session에 출력할 메세지를 담은 다음 화면으로 이동시킨다.
	   이동한 화면 컨트롤러에서 session에서 메세지를 꺼내서 modelandView()로 이동시킨다(session에서는 삭제)
	 */
		if(session.getAttribute("message")!=null) {
			String message = (String)session.getAttribute("message");
			session.removeAttribute("message");
			return new ModelAndView("/member/update-password").addObject("message",message);
		}
		return new ModelAndView("member/update-password");
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/member/update-password")
	public ModelAndView updatePassword(@Valid MemberDto.UpdatePassword dto, BindingResult br,
	         Principal principal, RedirectAttributes ra) {
	      
	      PasswordChangeResult result = memberService.updatePassword(dto, principal.getName());
	      if(result==PasswordChangeResult.SUCCESS)
	         return new ModelAndView("redirect:/");
	      else if(result==PasswordChangeResult.PASSWORD_CHECK_FAIL) {
	         ra.addFlashAttribute("message", "비밀번호를 확인하지 못했습니다");
	         return new ModelAndView("redirect:/member/update-password");
	      } else {
	         ra.addFlashAttribute("message", "새비밀번호와 기존비밀번호는 달라야 합니다");
	         return new ModelAndView("redirect:/member/update-password");
	      }
	   }
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/member/check-password")
	public ModelAndView checkPassword(HttpSession session){
		if(session.getAttribute("checkPassword")!=null)
			return new ModelAndView("redirect:/member/readme");
		return new ModelAndView("/member/check-password");
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/member/check-password")
	public ModelAndView checkPassword(String password, Principal principal,HttpSession session, RedirectAttributes ra){
		boolean result =memberService.password_check(password,principal.getName());
		if(result==true) {
			session.setAttribute("checkPassword",true);
			return new ModelAndView("redirect:/member/readme");
		}
		ra.addFlashAttribute("message","비밀번호를 확인하세요");
		return new ModelAndView("redirect:/member/check-password");
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/member/readme")
	public ModelAndView readme2(HttpSession session, Principal principal){
		if(session.getAttribute("checkPassword")==null)
			return new ModelAndView("redirect:/member/check-password");
		MemberDto.Read dto = memberService.내정보보기(principal.getName());
		return new ModelAndView("/member/readme").addObject("result",dto);
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
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/member/delete")
	public ModelAndView delete(HttpSession session, Principal principal) {
		memberService.탈퇴(principal.getName());
		// 세션에서 서버에 저장되어있는 사용자 정보를 가져온다
		session.invalidate();
		return new ModelAndView("redirect:/");
	}
	
	@GetMapping("/member/delete")
	public ModelAndView delete_2() {
		return new ModelAndView("member/delete");
	}
	
	// 생일을 검증하는 방식
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/member/update")
	public ModelAndView update(@Valid MemberDto.Update dto, BindingResult br, Principal principal) {
		memberService.update(dto, principal.getName());
		return new ModelAndView("redirect:/member/readme");
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ModelAndView cveHandler() {
		return new ModelAndView("error/error").addObject("message", "잘못된 작업입니다.");
	}
}	
