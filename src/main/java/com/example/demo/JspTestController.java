package com.example.demo;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.board.mapper.BoardMapper;

@Controller
public class JspTestController {

	@Resource(name = "com.example.demo.board.mapper.BoardMapper")
	BoardMapper boardMapper;
	
	@RequestMapping("/test")
	private String jspTest() throws Exception {
		
		System.out.println(boardMapper.boardCount());
		return "test"; // jsp 파일명 매핑
	}
}
