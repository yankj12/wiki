package com.cowork.wiki.controller;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.cowork.wiki.dao.ArticleMongoDaoUtil;
import com.cowork.wiki.model.Article;
import com.cowork.wiki.vo.ArticleVo;
import com.cowork.wiki.vo.ResponseVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yan.common.util.SchemaCopyUtil;

@Controller
public class WikiRestController {
	
	@Autowired
	private ArticleMongoDaoUtil articleMongoDaoUtil;
	
	@RequestMapping(value = "/wiki/api/article/{id}",method = RequestMethod.GET,consumes="application/json")
	public ResponseVo queryArticle(@PathVariable String id) throws JsonProcessingException {
		ResponseVo responseVo = new ResponseVo();
		responseVo.setSuccess(false);
		responseVo.setErrorMsg(null);
		
		if(id != null && !"".equals(id.trim())) {
			//查询数据
			Article article = articleMongoDaoUtil.findArticleById(id);
			
			ArticleVo articleVo = (ArticleVo) SchemaCopyUtil.simpleCopy(article, ArticleVo.class);
			responseVo.setSuccess(true);
			responseVo.setArticle(articleVo);
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
			
			
			Article article = (Article)SchemaCopyUtil.simpleCopy(articleVo, Article.class);
			String id = articleMongoDaoUtil.insertArticle(article);
			
			articleVo.setId(id);
			
			responseVo.setSuccess(true);
			responseVo.setArticle(articleVo);
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
			
			Article article = (Article)SchemaCopyUtil.simpleCopy(articleVo, Article.class);
			articleMongoDaoUtil.updateArticle(article);
			
			responseVo.setSuccess(true);
			responseVo.setArticle(articleVo);
		}
		
		return responseVo;
	}
	
	@RequestMapping(value = "/wiki/api/article/{id}",method = RequestMethod.DELETE, consumes="application/json")
	public ResponseVo deleteArticle(@PathVariable String id) throws JsonProcessingException {
		ResponseVo responseVo = new ResponseVo();
		responseVo.setSuccess(false);
		responseVo.setErrorMsg(null);
		
		if(id != null && !"".equals(id.trim())) {
			
			// 逻辑删除，将有效标志位置为无效
			Map<String, Object> map = new HashMap<>();
			map.put("validStatus", "0");
			articleMongoDaoUtil.updateArticleMultiFieldsById(id, map);
			
			responseVo.setSuccess(true);
		}
		
		return responseVo;
	}
	
}