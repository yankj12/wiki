package com.cowork.wiki.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WikiController {

	
	//显示输入页面 http://localhost:8012/content/viewInput
	@RequestMapping("/wiki/article/new")
	public void inputItem(HttpServletRequest request, HttpServletResponse response) throws Exception {
				
		response.sendRedirect("article");
	}

}