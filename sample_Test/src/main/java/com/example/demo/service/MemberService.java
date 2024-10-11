package com.example.demo.service;

import java.io.*;
import java.time.*;
import java.util.*;

import org.apache.commons.io.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;
import org.springframework.web.multipart.*;
import com.example.demo.dao.*;
import com.example.demo.dto.*;
import com.example.demo.dto.MemberDto.*;
import com.example.demo.entity.*;
import com.example.demo.enums.*;
import com.example.demo.util.*;

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
	@Autowired
	private MailUtil mailUtil;
	
	@Value("${sample.board.profile.url}")
	private String url;
	
	/*프사를 업로드한 경우 이미지 이름을 아이디로 변경후 저장
	  프사를 올리지 않는경우 default.png로 저장
	  path : 파일이나 폴더의 경로
	  paths , Arrays, collections : 클래스 이름에 s 가 붙으면 그 클래스들 다루는 함수를 모아놓은 유틸리티 클래스
	 */
	public void join(Create dto) {
		String encodedpassword = encoder.encode(dto.getPassword());
	
		String 확장자 = "jfif";
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
	public boolean password_Reset(String username) {
		Optional<Member> result= memberDao.findById(username);
		if(result.isEmpty())
			return false;
		String newpassword = RandomStringUtils.randomAlphabetic(20);
		String newEncodedPassword= encoder.encode(newpassword);
		Member member = result.get();
		member.changePassword(newEncodedPassword);
		mailUtil.sendMail(member.getEmail(), "임시비밀번호 를 입력해주세요","임시비밀번호 :"+newpassword);
		return true;
	}
	
	// 내정보를 비밀번호를 입력하고 확인
	public boolean password_check(String password, String username) {
		String encodedPassword= memberDao.findById(username).get().getPassword();
		return encoder.matches(password, encodedPassword);
	}
	
	public MemberDto.Read 내정보보기(String username) {
		Member member= memberDao.findById(username).get();
			return member.toReadDto(url);
	}
	
	
	public void 탈퇴(String username) {
		Member member=memberDao.findById(username).get();
		File file = new File(imageSaveFolder,member.getProfile());
		if(file.exists())
			file.delete();
		memberDao.delete(member);
	}

	@Transactional
	   public PasswordChangeResult updatePassword(MemberDto.UpdatePassword dto, String loginId) {
	      if(dto.getOldPassword().equals(dto.getNewPassword())==true)
	         return PasswordChangeResult.SAME_AS_OLD_PASSWORD;
	      Member member = memberDao.findById(loginId).get();
	   
	      boolean result = encoder.matches(dto.getOldPassword(), member.getPassword());
	      if(result==false)
	         return PasswordChangeResult.PASSWORD_CHECK_FAIL;
	      
	      String newEncodedPassword = encoder.encode(dto.getNewPassword());
	      member.changePassword(newEncodedPassword);
	      return PasswordChangeResult.SUCCESS;
	      
	   }
	@Transactional
	public void update(MemberDto.Update dto, String username) {
		String bithdayString = dto.getBirthday();
		String[] date = bithdayString.split("-");
		
		int year = Integer.parseInt(date[0]);
		int momth = Integer.parseInt(date[1]);
		int day = Integer.parseInt(date[2]);
		
		
		LocalDate birthday = LocalDate.of(year, momth, day);
		Member member = memberDao.findById(username).get();
		member.update(dto.getEmail(),birthday);
	}
}

