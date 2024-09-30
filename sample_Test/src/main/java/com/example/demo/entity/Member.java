package com.example.demo.entity;

import java.time.*;

import lombok.*;


@AllArgsConstructor
@Getter
@NoArgsConstructor
@ToString
@Builder
public class Member {
	private String username;
	private String password;
	private String email;
	private LocalDate joinday;
	private LocalDate birthday;
	private String profile;
	private Integer loginFailCount;
	private Boolean enabled;
	private Role role;
}
