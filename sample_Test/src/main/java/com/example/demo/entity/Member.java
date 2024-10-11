package com.example.demo.entity;

import java.time.*;
import java.time.temporal.*;

import org.hibernate.annotations.*;

import com.example.demo.dto.*;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@Getter
@NoArgsConstructor
@ToString
@Builder
@Entity
@DynamicUpdate
public class Member {
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
	
	public void changePassword(String password) {
		this.password= password;
	}

	public void block() {
		this.loginFailCount=5;
		this.enabled=false;
	}
	public void increaseLoginFailCount() {
		this.loginFailCount++;
	}
	
	public MemberDto.Read toReadDto(String url){
		long days = ChronoUnit.DAYS.between(joinday, LocalDate.now());
		return new MemberDto.Read(email, joinday, birthday, days , url + profile, role.name());
	}

	public void update(String email, LocalDate birthday) {
		this.email = email;
		this.birthday = birthday;
	}

	public void update2(String email2, LocalDate birthday2, String 프로필이름) {
		this.email = email2;
		this.birthday= birthday2;
		this.profile = 프로필이름;
	}

}
