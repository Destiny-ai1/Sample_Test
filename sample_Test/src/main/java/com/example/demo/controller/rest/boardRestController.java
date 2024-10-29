package com.example.demo.controller.rest;

import java.io.*;
import java.util.*;

import org.apache.commons.io.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import com.example.demo.util.*;

@RestController
public class boardRestController {
	@Value("${sample.board.image.folder}")
	private String imageSaveFolder;
	@Value("${sample.board.image.url}")
	private String imageUrl;
	@Autowired
	private ImageUtil imageUtil;
	
	@PostMapping("/api/boards/image")
	public ResponseEntity<?> ckimageupload(MultipartFile upload){
		String originalFilename= upload.getOriginalFilename();
		String 확장자= FilenameUtils.getExtension(originalFilename);
		// 1.업로드한 이미지의 확장자를 잘라낸다
		// 2.랜덤한 이미지 이름을 생성 
		String 저장할이미지이름 = UUID.randomUUID()+"."+확장자;
		File file= new File(imageSaveFolder,저장할이미지이름);
		try {
			upload.transferTo(file);
			// 업로드한 이미지(MultipartFile)은 메모리에 위치
			// transferTo는 파일을 이동
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		//Map = 설계도없이 JS의 객체에 대응하는 자바객체
		String 주소= imageUrl+저장할이미지이름;
		Map<String, Object> map=Map.of("uploaded",1,"url",주소,"filename",저장할이미지이름);
		return ResponseEntity.ok(map);
	}
	
	@GetMapping("/board/image")
	public ResponseEntity<byte[]> viewprofile(String imagename){
		return imageUtil.getImageResponseEntity(imagename);
	}
	
}
