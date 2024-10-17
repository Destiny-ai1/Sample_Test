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
	public void update(MemberDto.Update dto, String loginId) {
		Member member = memberDao.findById(loginId).get();
		
		// 서버에서 프사를 변경하면 dto.getProfile()에 MultipartFile이 들어있고, 프사를 변경하지 않으면 비어있다
		// 프론트에 <input type='file' name='profile'>이 존재할 경우 MultipartFile profile이 절대 null이 되지 않는다
		if(dto.getProfile().isEmpty()==false) {
			// 새프사를 저장할 이름(사용자 아이디 + 확장자)을 계산
			MultipartFile newProfile = dto.getProfile();
			String 확장자 =  FilenameUtils.getExtension(newProfile.getOriginalFilename());
			String 새프로필이름 = loginId + "." + 확장자;
			
			// 기존 프사와 새 프사는 파일의 이름은 사용자 아이디와 같다. 확장자는 다를 수 있다
			// 기존 프사와 새 프사의 확장자가 다르다면 기존 프사를 삭제한다
			boolean 기존프사삭제여부 = (member.getProfile().equals(새프로필이름)==false);
			
			try {
				// 폴더명과 파일명으로 File 객체를 생성 -> 파일이 없으면 새로 만들고 있으면 덮어 쓴다
				File file = new File(imageSaveFolder, 새프로필이름);
				newProfile.transferTo(file);
				if(기존프사삭제여부==true) {
					File deleteProfile = new File(imageSaveFolder, member.getProfile());
					if(deleteProfile.exists())
						deleteProfile.delete();
				}
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
			// 프사를 변경한 경우
			member.update(dto.getEmail(), dto.getBirthday(), 새프로필이름);
		} else {
			// 프사를 변경하지 않은 경우
			member.update(dto.getEmail(), dto.getBirthday(), null);
		}
	}
}

