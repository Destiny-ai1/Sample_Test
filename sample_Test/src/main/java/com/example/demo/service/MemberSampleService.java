package com.example.demo.service;

import java.io.*;

import org.apache.commons.io.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;

import com.example.demo.dao.*;
import com.example.demo.dto.*;
import com.example.demo.entity.*;

import jakarta.transaction.*;

@Service
public class MemberSampleService {
	@Autowired
	private MemberDao memberDao;
	@Value("${sample.board.image.folder}")
	private String imageSaveFolder;
	
	@Transactional
	public void update2(MemberUpdateSampleDto dto, String loginId) {
		Member member = memberDao.findById(loginId).get();
		
		// 프사를 변경해서 업로드했다면 isEmpty는 false, 프사를 변경하지 않았다면 isEmtpty는 true
		if(dto.getProfile().isEmpty()==false) {
			// 새로운 프사의 이름 문자열을 만든다
			MultipartFile newProfile = dto.getProfile();
			String 확장자 =  FilenameUtils.getExtension(newProfile.getOriginalFilename());
			String 프로필이름 = loginId + "." + 확장자;
			
			// 기존 프사와 새 프사의 이름이 일치하는 지 체크
			// 사용자가 spring일 때 기존 프사, 새프사가 모두 확장자가 jpg라면 파일명이 같다
			boolean 기존프로필삭제여부 = (member.getProfile().equals(프로필이름)==false);
			
			try {
				// 새프사를 저장
				File file = new File(imageSaveFolder, 프로필이름);
				newProfile.transferTo(file);
				if(기존프로필삭제여부==true) {
					// 기존프사가 새프사와 이름이 다를 경우 삭제
					File deleteProfile = new File(imageSaveFolder, member.getProfile());
					if(deleteProfile.exists())
						deleteProfile.delete();
				}
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
			// 프사를 변경한 경우
			member.update2(dto.getEmail(), dto.getBirthday(), 프로필이름);
		} else {
			// 프사를 변경하지 않은 경우
			member.update2(dto.getEmail(), dto.getBirthday(), null);
		}
	}
}
