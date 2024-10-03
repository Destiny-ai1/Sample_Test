package com.example.demo;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.web.*;

import com.example.demo.security.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {
	@Autowired
	private LoginSuccessHandler successHandler;
	@Autowired
	private LoginFailHandler failHandler;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.csrf(c->c.disable());
		http.formLogin(c->c.loginPage("/member/login").loginProcessingUrl("/member/login")
				.successHandler(successHandler).failureHandler(failHandler));
		http.logout(c->c.logoutUrl("/member/logout").logoutSuccessUrl("/"));
		return http.build();
	}
}
