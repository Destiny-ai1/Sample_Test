package com.example.demo.dto;

import java.time.*;
import java.util.*;

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
	public static class BoardList{
		private Long bno;
		private String title;
		private String writer;
		private String writeTime;
		private String readCnt;
	}
	
	@Getter
	@ToString
	@AllArgsConstructor
	public static class page{
		private Integer prev;
		private Integer start;
		private Integer end;
		private Integer next;
		private Integer pageno;
		private List<BoardList> boards;
	}
	
	@Data
	public static class Read{
		private Long bno;
		private String title;
		private String content;
		private String writer;
		private String writeTime;
		private Integer readCnt;
		private Integer goodCnt;
		private Integer badCnt;
		private Boolean isDeleted;
		
		private List<CommentDto.Read> comments;
		
		public BoardDto.Read 글삭제() {
			this.content = "삭제된글입니다";
			return this;
		}

		public void 조회수증가() {
			this.readCnt++;
		}
	}
}
