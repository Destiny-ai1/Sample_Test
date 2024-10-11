package com.example.demo.dto;

import java.time.*;

import org.springframework.format.annotation.*;
import org.springframework.web.multipart.*;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class MemberUpdateSampleDto {
	@NotEmpty
	@Email
	private String email;
	
	private MultipartFile profile;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate birthday;
}
