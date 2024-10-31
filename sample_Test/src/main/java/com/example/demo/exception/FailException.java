package com.example.demo.exception;

import lombok.*;

/*에러 페이지 나오면 exception 으로 
 예외처리 시켜서 사용자에게는 보여줄 목적, 개발자에겐 오류 메세지
 */

@Getter
@AllArgsConstructor
public class FailException extends RuntimeException {
	String message;
}
