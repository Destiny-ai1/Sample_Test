package com.example.demo.service;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.example.demo.dao.*;
import com.example.demo.dto.*;
import com.example.demo.dto.CommentDto.*;
import com.example.demo.entity.*;

@Service
public class CommentService {
	@Autowired
	private CommentDao commentDao;

	public List<Comment> write(Long bno, String content, String loginId) {
		commentDao.saveComment(bno, content, loginId);
		return commentDao.findByBno(bno);
	}
	public List<Comment> deleteComment(Long bno, Long cno, String loginId) {
		commentDao.deleteByCnoAndWriter(cno, loginId);
		return commentDao.findByBno(bno);
	}
}
