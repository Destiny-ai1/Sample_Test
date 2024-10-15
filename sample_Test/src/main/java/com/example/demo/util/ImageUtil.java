package com.example.demo.util;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;

@Component
public class ImageUtil {
	@Value("${sample.board.image.folder}")
	private String imageSaveFolder;
	
	public List<String> getImageNames(String html){
		// Jsoup에서 html을 파싱
		Document document= Jsoup.parse(html);
		// 파싱된 html에서 img 태그들만 추출
		Elements elements= document.select("img");
		
		List<String> result= new ArrayList<>();
		for(Element element:elements) {
			String src= element.attr("src");
			int 등호위치= src.indexOf("=");
			String imagename = src.substring(등호위치+1);
			result.add(imagename);
		}
		return result;
	}
	
	
	public ResponseEntity<byte[]> getImageResponseEntity(String imagename){
		try {
			Path filePath= Paths.get(imageSaveFolder+imagename);
			byte[] imageBytes= Files.readAllBytes(filePath);
			String mimeType = Files.probeContentType(filePath);
			MediaType mediaType= MediaType.parseMediaType(mimeType);
			return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(imageBytes);
		}catch(IOException e){
			e.printStackTrace();
		}
		return ResponseEntity.status(409).body(null);
	}
}
