package com.cowork.wiki.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping(value="/content")
public class ContentController {
	
	@RequestMapping("/viewInput")
	public ModelAndView inputItem() throws JsonProcessingException {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("inputItem");
		return mav;
	}
	
	@RequestMapping("/input")
	public String inputItem(String contentText) throws JsonProcessingException {

		return "保存成功！content = " + contentText;
	}
}
