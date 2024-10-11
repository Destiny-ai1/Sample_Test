package com.example.demo.controller.rest;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.*;

import jakarta.validation.*;
import jakarta.validation.constraints.*;


/* rest 방식은 주로 명사를 가지고 주소를 구성
 rest를 사용하면 get, post 뿐만 아니라 put,patch, delete까지 사용가능
 예시 ) 
 get	products/123	123번째 상품을 읽어라
 post	products/123	123번째 상품을 작성해라
 put	products/123	123번째 상품을 변경해라(전체를 변경)
 patch	products/123	123번째 상품을 변경해라(지정한 부분을 부분변경)
 delete	products/123	123번째 상품을 삭제해라
*/ 
//응답을 ResponseBody or ResponseEntity ( + 상태코드)로 사용
@RestController
@Validated
public class MemberRestController {
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/member/username/available")
	public ResponseEntity<?> 아이디사용가능(@NotEmpty @Valid  String username){
		boolean result = memberService.Id사용가능(username);
		if(result==true)
			return ResponseEntity.ok(true);
		return ResponseEntity.status(409).body(false);
	}
	
	@GetMapping("/member/username")
	public ResponseEntity<?> 아이디찾기(@Valid @NotEmpty String email){
		Optional<String> result =memberService.Id찾기(email);
		if(result.isEmpty())
			return ResponseEntity.status(409).body("아이디를 찾을 수 없습니다");
		return ResponseEntity.ok(result.get());
	}
	
	@PatchMapping("/member/reset-password")
	public ResponseEntity<?> 비밀번호리셋(@Valid @NotEmpty String username){
		boolean result= memberService.password_Reset(username);
		if(result==true)
			return ResponseEntity.ok(true);
		return ResponseEntity.status(409).body(null);
	}
	// 예외 처리하는 컨트롤러
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> CVEHandler(ConstraintViolationException e){
		System.out.println(e.getMessage());
		return ResponseEntity.status(409).body(e.getMessage());
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/aaa")
	public ResponseEntity<?> aaa() {
		return ResponseEntity.ok("메롱");
	}
}
