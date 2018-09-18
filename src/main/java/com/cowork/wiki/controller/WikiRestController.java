package com.cowork.wiki.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cowork.wiki.dao.ArticleMongoDaoUtil;
import com.cowork.wiki.model.Article;
import com.cowork.wiki.vo.ArticleVo;
import com.cowork.wiki.vo.ResponseVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yan.common.util.SchemaCopyUtil;

@RestController
public class WikiRestController {
	
	@Autowired
	private ArticleMongoDaoUtil articleMongoDaoUtil;
	
	// ,consumes="application/json"
	@RequestMapping(value = "/rest/article/{articleId}",method = RequestMethod.GET)
	public ResponseVo queryArticle(@PathVariable Integer articleId) throws JsonProcessingException {
		ResponseVo responseVo = new ResponseVo();
		responseVo.setSuccess(false);
		responseVo.setErrorMsg(null);
		
		if(articleId != null) {
			try {
				//查询数据
				Article article = articleMongoDaoUtil.findArticleByArticleId(articleId);
				
				ArticleVo articleVo = (ArticleVo) SchemaCopyUtil.simpleCopy(article, ArticleVo.class);
				responseVo.setSuccess(true);
				responseVo.setArticle(articleVo);
			} catch (Exception e) {
				e.printStackTrace();
				responseVo.setErrorMsg(e.getMessage());
			}
		}
		
		return responseVo;
	}
	
	@RequestMapping(value = "/rest/article",method = RequestMethod.PUT)
	public ResponseVo addArticle(@RequestBody ArticleVo articleVo) throws JsonProcessingException {
		ResponseVo responseVo = new ResponseVo();
		responseVo.setSuccess(false);
		responseVo.setErrorMsg(null);
		
		if(articleVo != null) {
			articleVo.setId(null);
			
			articleVo.setValidStatus("1");
			articleVo.setInsertTime(new Date());
			articleVo.setUpdateTime(new Date());
			
			
			try {
				
				// 查询出最大的articleId
				Integer maxArticleId = articleMongoDaoUtil.findMaxArticleId();
				if(maxArticleId == null || maxArticleId < 0) {
					maxArticleId = 0;
				}
				Integer newArticleId = maxArticleId + 1;
				
				Article article = (Article)SchemaCopyUtil.simpleCopy(articleVo, Article.class);
				// 设置articleId
				article.setArticleId(newArticleId);
				// 要传articleId到页面，所以要给vo设置值
				articleVo.setArticleId(newArticleId);
				String id = articleMongoDaoUtil.insertArticle(article);
				
				articleVo.setId(id);
				
				responseVo.setSuccess(true);
				responseVo.setArticle(articleVo);
			} catch (Exception e) {
				e.printStackTrace();
				responseVo.setErrorMsg(e.getMessage());
			}
		}
		
		return responseVo;
	}
	
	@RequestMapping(value = "/rest/article/{articleId}",method = RequestMethod.POST)
	public ResponseVo updateArticle(@PathVariable Integer articleId, @RequestBody ArticleVo articleVo) throws JsonProcessingException {
		ResponseVo responseVo = new ResponseVo();
		responseVo.setSuccess(false);
		responseVo.setErrorMsg(null);
		
		if(articleVo != null && articleId != null ) {
			
			try {
				String title = articleVo.getTitle();
				String content = articleVo.getContent();
				String author = articleVo.getAuthor();
				Date updateTime = new Date();
				
				// 逻辑删除，将有效标志位置为无效
				Map<String, Object> map = new HashMap<>();
				map.put("title", title);
				map.put("content", content);
				map.put("author", author);
				map.put("updateTime", updateTime);
				
				articleMongoDaoUtil.updateArticleMultiFieldsByArticleId(articleId, map);
				
				responseVo.setSuccess(true);
				responseVo.setArticle(articleVo);
			} catch (Exception e) {
				e.printStackTrace();
				responseVo.setErrorMsg(e.getMessage());
			}
		}
		
		return responseVo;
	}
	
	@RequestMapping(value = "/rest/article/{articleId}",method = RequestMethod.DELETE)
	public ResponseVo deleteArticle(@PathVariable Integer articleId) throws JsonProcessingException {
		ResponseVo responseVo = new ResponseVo();
		responseVo.setSuccess(false);
		responseVo.setErrorMsg(null);
		
		if(articleId != null) {
			
			try {
				// 逻辑删除，将有效标志位置为无效
				Map<String, Object> map = new HashMap<>();
				map.put("validStatus", "0");
				articleMongoDaoUtil.updateArticleMultiFieldsByArticleId(articleId, map);
				
				responseVo.setSuccess(true);
			} catch (Exception e) {
				e.printStackTrace();
				responseVo.setErrorMsg(e.getMessage());
			}
		}
		
		return responseVo;
	}
	
}