package com.example.demo.service;

import java.io.*;
import java.util.*;

import org.apache.commons.io.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;
import org.springframework.web.multipart.*;

import com.example.demo.dao.*;
import com.example.demo.dto.MemberDto.*;
import com.example.demo.entity.*;

import jakarta.transaction.*;

@Service
public class MemberServiceJpa {
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private MemberDaojpa memberDao;
	private final String imageSaveFolder = "c:/upload";
	private final String defaultImage = "default.jpg";
	
	public void join(Create dto) {
		String encodedpassword = encoder.encode(dto.getPassword());
	
		String 확장자 = "JPG";
		MultipartFile profile = dto.getProfile();
	
		boolean is프사업로드 = profile!=null && profile.isEmpty()==false;
		if(is프사업로드==true) {
			확장자 = FilenameUtils.getExtension(profile.getOriginalFilename());
		}
		String 프로필이름 = dto.getUsername()+"."+ 확장자;
	
		MemberJpa member = MemberJpa.builder().username(dto.getUsername()).password(dto.getPassword()).email(dto.getEmail())
				.birthday(dto.getBirthday()).profile(프로필이름).build();
		try {
			memberDao.save(member);
			File target = new File(imageSaveFolder, 프로필이름);
			if(is프사업로드) {
				profile.transferTo(target);
			}else {
				File source = new File(imageSaveFolder,defaultImage);
				FileCopyUtils.copy(source, target);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean ID사용가능(String username) {
		return !memberDao.existsById(username);
	}
	public Optional<String> ID찾기(String email){
		return memberDao.findByUsernameByemail(email);
	}
	
	// 비밀번호 찾기위해 임시비밀번호를 발급
	@Transactional // 여러개의 sql로 이루어진걸 사용하면 함수가 하나로 묶인다.
	public boolean password_Reset(String username, String email) {
		Optional<MemberJpa> result= memberDao.findById(username);
		if(result.isEmpty())
			return false;
		String newpassword = RandomStringUtils.randomAlphabetic(20);
		String newEncodedPassword= encoder.encode(newpassword);
		MemberJpa member = result.get();
		member.update(newEncodedPassword,null);
		return true;
	}
	// 내정보를 비밀번호를 입력하고 확인
	public boolean password_check(String password, String username) {
		String encodedPassword= memberDao.findById(username).get().getPassword();
		return encoder.matches(password, encodedPassword);
	}
	
	public MemberJpa 내정보보기(String username) {
		return memberDao.findById(username).get();
	}
	
	@Transactional
	public boolean 내정보변경(String oldPassword, String newPassword, String username, String email) {
		MemberJpa member = memberDao.findById(username).get();
		boolean passwordCheck = encoder.matches(oldPassword, member.getPassword());
		if(passwordCheck==false)
			return false;
		String newEncodedPassword = encoder.encode(newPassword);
		member.update(newEncodedPassword,email);
		return true;
	}
	
	@Transactional
	public void 탈퇴(String username) {
		memberDao.deleteById(username);
	}
}
