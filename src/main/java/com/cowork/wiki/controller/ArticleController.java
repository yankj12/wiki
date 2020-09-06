package com.cowork.wiki.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController {
	
	//显示输入页面 http://localhost:8012/content/viewInput
	@RequestMapping("/pages/article/new")
	public void inputItem(HttpServletResponse response) throws Exception {	
		response.sendRedirect("/article.html");
	}
	
}
