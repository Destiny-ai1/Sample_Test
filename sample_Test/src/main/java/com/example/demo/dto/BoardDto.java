package com.example.demo.dto;

import java.time.*;


import com.example.demo.entity.*;

import jakarta.validation.constraints.*;
import lombok.*;

public class BoardDto {
	public BoardDto() {}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Create{
		@NotEmpty
		private String title;
		private String content;
		
		
		public board toEntity(String loginId) {
			return new board(null,title,content,loginId,LocalDateTime.now(),0,0,0,false);
		}
	}
	
	@Getter
	@ToString
	public static class read{
		private Long bno;
		private String title;
		private String writer;
		private String writeTime;
		private String readCnt;
	}
}
