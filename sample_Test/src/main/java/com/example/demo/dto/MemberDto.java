package com.example.demo.dto;

import java.time.*;

import org.springframework.format.annotation.*;
import org.springframework.web.multipart.*;

import com.example.demo.entity.*;


import jakarta.validation.constraints.*;
import lombok.*;

public class MemberDto {
   // MemberDto 객체는 필요없다. 객체 생성 금지
   private MemberDto() {}
   
   @Data
   public static class Create {
      @NotEmpty
      @Pattern(regexp="^[a-z0-9]{6,10}$")
      private String username;
      
      @NotEmpty
      private String password;
      
      @NotEmpty
      @Email
      private String email;
      
      // <input type="date"> -> 2010-10-20 -> 스프링이 못 받는다
      @DateTimeFormat(pattern="yyyy-MM-dd") // 입력받는 어노테이션
      private LocalDate birthday;
      
      // 파일 업로드 형식
      private MultipartFile profile;
      
      public Member toEntity(String profileName, String encodedPassword) {
         return new Member(username, encodedPassword, email, LocalDate.now(), birthday, profileName, 0, true, Role.user);
      }
   }
   
   @Data
   public static class UpdatePassword {
      private String oldPassword;
      private String newPassword;
   }
   
   @Data
   @AllArgsConstructor
   public static class Read {
      private String email;
      private LocalDate joinday;
      private LocalDate birthday;
      private long days;
      private String profile;
      private String role;
   }
   
   @Data
   public static class Update{
	  @NotEmpty
	  @Email
      private String email;
	  // LocalDate 는 검증이 지원되지 않는다
	  // 검증할려면 String 으로 받아서 검증후 LocalDate로 변환
	  
	  @NotEmpty
	  @Pattern(regexp="^[0-9]{4}-[0-9]{2}-[0-9]{2}$")
      private String birthday;
   }
}






