package com.cowork.wiki.web;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	@RequestMapping("/wiki/article/new")
	public void inputItem(HttpServletResponse response) throws Exception {
				
		response.sendRedirect("/article.html");
	}

	@RequestMapping(value = "/wiki/api/article/{id}",method = RequestMethod.GET,consumes="application/json")
	public ResponseVo queryArticle(@PathVariable String id) throws JsonProcessingException {
		ResponseVo responseVo = new ResponseVo();
		responseVo.setSuccess(false);
		responseVo.setErrorMsg(null);
		
		if(id != null && !"".equals(id.trim())) {
			//查询数据
			List<ServiceInstance> instances = discoveryClient.getInstances("wiki-mongo-data-service");
			ServiceInstance instance = instances.get(0);
			
			URI uri = instance.getUri();
			String apiUrl = uri.toString() + "/api/queryArticle?id={id}";
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			
			RestTemplate restTemplate = new RestTemplate();
			ResponseVo responseVo2 = restTemplate.getForObject(apiUrl, ResponseVo.class, map);
			if(responseVo2 != null && responseVo2.isSuccess() && responseVo2.getArticle() != null) {
				ArticleVo article = responseVo2.getArticle();
				
				responseVo.setSuccess(true);
				responseVo.setArticle(article);
			}
		}
		
		return responseVo;
	}
	
	@RequestMapping(value = "/wiki/api/article",method = RequestMethod.PUT, consumes="application/json")
	public ResponseVo addArticle(@RequestBody ArticleVo articleVo) throws JsonProcessingException {
		ResponseVo responseVo = new ResponseVo();
		responseVo.setSuccess(false);
		responseVo.setErrorMsg(null);
		
		if(articleVo != null) {
			articleVo.setId(null);
			
			articleVo.setValidStatus("1");
			articleVo.setInsertTime(new Date());
			articleVo.setUpdateTime(new Date());
			
			//新增数据
			List<ServiceInstance> instances = discoveryClient.getInstances("wiki-mongo-data-service");
			ServiceInstance instance = instances.get(0);
			
			URI uri = instance.getUri();
			String apiUrl = uri.toString() + "/api/saveArticle";
			
			RestTemplate restTemplate = new RestTemplate();
			responseVo = restTemplate.postForObject(apiUrl, articleVo, ResponseVo.class);
			
			if(responseVo != null && responseVo.isSuccess() && responseVo.getArticle() != null) {
				ArticleVo article = responseVo.getArticle();
				
				responseVo.setSuccess(true);
				responseVo.setArticle(article);
			}
		}
		
		return responseVo;
	}
	
	@RequestMapping(value = "/wiki/api/article/{id}",method = RequestMethod.POST, consumes="application/json")
	public ResponseVo updateArticle(@PathVariable String id, @RequestBody ArticleVo articleVo) throws JsonProcessingException {
		ResponseVo responseVo = new ResponseVo();
		responseVo.setSuccess(false);
		responseVo.setErrorMsg(null);
		
		if(articleVo != null && id != null && !"".equals(id.trim())) {
			articleVo.setId(id);
			
			//查询数据
			List<ServiceInstance> instances = discoveryClient.getInstances("wiki-mongo-data-service");
			ServiceInstance instance = instances.get(0);
			
			URI uri = instance.getUri();
			String saveAPIUrl = uri.toString() + "/api/saveArticle";
			
			RestTemplate restTemplate = new RestTemplate();
			responseVo = restTemplate.postForObject(saveAPIUrl, articleVo, ResponseVo.class);
			
			if(responseVo != null && responseVo.isSuccess() && responseVo.getArticle() != null) {
				ArticleVo article = responseVo.getArticle();
				
				responseVo.setSuccess(true);
				responseVo.setArticle(article);
			}
		}
		
		return responseVo;
	}
	
	@RequestMapping(value = "/wiki/api/article/{id}",method = RequestMethod.DELETE, consumes="application/json")
	public ResponseVo deleteArticle(@PathVariable String id) throws JsonProcessingException {
		ResponseVo responseVo = new ResponseVo();
		responseVo.setSuccess(false);
		responseVo.setErrorMsg(null);
		
		if(id != null && !"".equals(id.trim())) {
			
			//查询数据
			List<ServiceInstance> instances = discoveryClient.getInstances("wiki-mongo-data-service");
			ServiceInstance instance = instances.get(0);
			
			URI uri = instance.getUri();
			String apiUrl = uri.toString() + "/api/deleteArticle";
			
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			RestTemplate restTemplate = new RestTemplate();
			ResponseVo responseVo2 = restTemplate.getForObject(apiUrl, ResponseVo.class, map);
			
			if(responseVo2 != null && responseVo2.isSuccess()) {
				
				responseVo.setSuccess(true);
			}
		}
		
		return responseVo;
	}
}
