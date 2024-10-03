package com.example.demo.dto;

import java.time.*;

import org.springframework.format.annotation.*;
import org.springframework.web.multipart.*;

import com.example.demo.entity.*;

import jakarta.validation.constraints.*;
import lombok.*;

public class MemberDto {
	//memberDto란 객체를 생성을 금지시킨다.
	private MemberDto() {}
	
	@Data
	public static class Create{
		@NotEmpty                       //필수입력 체크
		@Pattern(regexp = "^[a-z0-9]{6,10}$")
		private String username;
		
		@NotEmpty
		@Pattern(regexp = "^[a-z0-9]{10,20}$")
		private String password;
		
		@NotEmpty
		@Email
		private String email;
		@DateTimeFormat(pattern="yyyy-MM-dd")
		private LocalDate birthday;
		private MultipartFile profile;

		public Member toEntity(String profileName, String encodedpassword) {
			return new Member(username,encodedpassword,email,LocalDate.now(),birthday,profileName,0,true,Role.user);
		}
		
	}
	
	@Data
	public static class Update{
		@Pattern(regexp = "^[a-z0-9]{10,20}$")
		private String oldpassword;
		
		@Pattern(regexp = "^[a-z0-9]{10,20}$")
		private String newpassword;
		@Email
		private String email;
		private MultipartFile profile;
	}
	
	@Data
	public static class Read{
		private String email;
		private LocalDate joinday;
		private LocalDate birthday;
		private String profile;
		private long days;
		private String role;
	}
}
