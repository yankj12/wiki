package com.cowork.wiki.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping(value="/article")
public class ArticleController {
	
	//显示输入页面 http://localhost:8012/content/viewInput
	@RequestMapping("/edit")
	public ModelAndView inputItem() throws JsonProcessingException {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("inputItem");
		return mav;
	}
	
	//通过输入页面的submit按钮，可提交输入的内容到这里显示出来
	@RequestMapping("/input")
	public String inputItem(String title, String author, String content) throws JsonProcessingException {
		System.out.println(title);
		System.out.println(author);
		System.out.println(content);
		
		return "保存成功！";
	}
}
