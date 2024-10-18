package com.example.demo.entity;

import java.time.*;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class board {
	private Long bno;
	private String title;
	private String content;
	private String writer;
	private LocalDateTime writeTime;
	private Integer readCnt;
	private Integer goodCnt;
	private Integer badCnt;
	
	
	private boolean isDeleted;
		
	public board 글삭제() {
		this.content = "삭제된글입니다";
		return this;
	}

	public void increaseReadCnt() {
		this.readCnt++;
	}
}
