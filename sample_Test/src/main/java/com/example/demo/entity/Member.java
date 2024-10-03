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
}
