package com.cowork.wiki.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WikiController {

	// web的根路径是/wiki，所以RequestMapping不要以/wiki开头，容易冲突
	//显示输入页面 http://localhost:8012/content/viewInput
	@RequestMapping("/article/new")
	public String inputItem(){
				
		return "article";
	}

}