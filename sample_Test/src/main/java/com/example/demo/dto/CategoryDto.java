package com.example.demo.dto;

import java.util.*;

import lombok.*;

@Data
public class CategoryDto {
	private Long cno;
	private String cname;
	private List<CategoryDto> children;
}
