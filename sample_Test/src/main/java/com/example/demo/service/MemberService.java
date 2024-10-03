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
public class MemberService {
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private MemberDao memberDao;
	@Value("${sample.board.image.folder}")
	private String imageSaveFolder;
	@Value("${sample.board.image.default}")
	private String defaultImage;
	
	/*프사를 업로드한 경우 이미지 이름을 아이디로 변경후 저장
	  프사를 올리지 않는경우 default.png로 저장
	  path : 파일이나 폴더의 경로
	  paths , Arrays, collections : 클래스 이름에 s 가 붙으면 그 클래스들 다루는 함수를 모아놓은 유틸리티 클래스
	 */
	public void join(Create dto) {
		String encodedpassword = encoder.encode(dto.getPassword());
	
		String 확장자 = "JPG";
		MultipartFile profile = dto.getProfile();
	
		boolean is프사업로드 = profile!=null && profile.isEmpty()==false;
		if(is프사업로드==true) {
			확장자 = FilenameUtils.getExtension(profile.getOriginalFilename());
		}
		String 프로필이름 = dto.getUsername()+"."+ 확장자;
	
		Member member =dto.toEntity(프로필이름,encodedpassword);
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
	
	public boolean Id사용가능(String username) {
		return !memberDao.existsById(username);
	}
	public Optional<String> Id찾기(String email){
		return memberDao.findByUsernameByemail(email);
	}
	// 비밀번호 찾기위해 임시비밀번호를 발급
	@Transactional
	public boolean password_Reset(String username, String email) {
		Optional<Member> result= memberDao.findById(username);
		if(result.isEmpty())
			return false;
		String newpassword = RandomStringUtils.randomAlphabetic(20);
		String newEncodedPassword= encoder.encode(newpassword);
		Member member = result.get();
		member.changePassword(newEncodedPassword);
		return true;
	}
	
	// 내정보를 비밀번호를 입력하고 확인
	public boolean password_check(String password, String username) {
		String encodedPassword= memberDao.findById(username).get().getPassword();
		return encoder.matches(password, encodedPassword);
	}
	
	public Member 내정보보기(String username) {
		return memberDao.findById(username).get();
	}
	
	
	public void 탈퇴(String username) {
		memberDao.deleteById(username);
	}
}

