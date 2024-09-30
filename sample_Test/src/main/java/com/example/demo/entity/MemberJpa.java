package com.example.demo.entity;

import java.time.*;


import org.hibernate.annotations.*;

import jakarta.persistence.*;
import lombok.*;



@AllArgsConstructor
@Getter
@NoArgsConstructor
@ToString
@Builder
@Entity
@DynamicUpdate               // 필드에 있는 모든값들을 update 한다 그래서 DynamicUpdate를 사용해서 내가 지정한것만 바꿔라
public class MemberJpa {
	@Id
	private String username;
	private String password;
	private String email;
	private LocalDate joinday;
	private LocalDate birthday;
	private String profile;
	private Integer loginFailCount;
	private Boolean enabled;
	@Enumerated(EnumType.STRING)
	private Role role;
	
	public void update(String password, String email) {
		if(password!=null)
			this.password=password;
		if(email!=null)
			this.email=email;
	}
}
