package com.cowork.wiki.web;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cowork.wiki.vo.ArticleVo;
import com.cowork.wiki.vo.ResponseVo;
import com.fasterxml.jackson.core.JsonProcessingException;

@EnableDiscoveryClient
@RestController
public class ArticleController {
	
	@Autowired
    private DiscoveryClient discoveryClient;
	
	//显示输入页面 http://localhost:8012/content/viewInput
	@RequestMapping("/wiki")
	public void inputItem(HttpServletResponse response) throws Exception {
				
		response.sendRedirect("/article.html");
	}
	
	//通过输入页面的submit按钮，可提交输入的内容到这里显示出来
	@RequestMapping("/input")
	public String inputItem(String title, String author, String content) throws JsonProcessingException {
		
		ArticleVo articleVo = new ArticleVo();
		articleVo.setAuthor(author);
		articleVo.setTitle(title);
		articleVo.setContent(content);
		
		List<ServiceInstance> instances = discoveryClient.getInstances("wiki-mongo-data-service");
		ServiceInstance instance = instances.get(0);
		
		URI uri = instance.getUri();
		String saveAPIUrl = uri.toString() + "/api/saveArticle";
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseVo responseVo = restTemplate.postForObject(saveAPIUrl, articleVo, ResponseVo.class);
		
		//System.out.println(responseVo.getArticle().getId());
		String result = null;
		
		if(responseVo != null && responseVo.isSuccess()) {
			result = "保存成功！";
		}else {
			result = "保存失败，请重试！";
		}
		
		return result;
	}
}
