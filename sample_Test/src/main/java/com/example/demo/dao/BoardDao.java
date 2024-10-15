package com.example.demo.dao;

import org.apache.ibatis.annotations.*;

import com.example.demo.entity.*;

@Mapper
public interface BoardDao {
	public int save(board board); 
		
}
