package com.cowork.wiki.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cowork.wiki.mapper.ArticleMapper;
import com.cowork.wiki.model.Article;
import com.cowork.wiki.util.SchameCopyUtil;
import com.cowork.wiki.vo.ArticleVo;
import com.cowork.wiki.vo.ResponseVo;

@RestController
public class ArticleRestController {
	
	@Autowired
	private ArticleMapper articleMapper;

	/**
	 * 根据userId查询这个用户的数据权限
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/api/saveArticle", method=RequestMethod.POST)
    public ResponseVo saveArticle(@RequestBody ArticleVo articalVo) {
		ResponseVo responseVo = new ResponseVo();
		responseVo.setSuccess(false);
		responseVo.setErrorMsg(null);
		
		if(articalVo != null) {
			Article article = (Article)SchameCopyUtil.simpleCopy(articalVo, Article.class);
			if(articalVo.getId() != null) {
				// 避免将insertTime字段进行更新
				article.setInsertTime(null);
				article.setUpdateTime(new Date());
				articleMapper.updateArticle(article);
			}else {
				article.setInsertTime(new Date());
				article.setUpdateTime(new Date());
				Integer id = articleMapper.insertArticle(article);
				articalVo.setId(id);
			}
			responseVo.setSuccess(true);
		}
		responseVo.setArticle(articalVo);
        return responseVo;
    }
	
	@RequestMapping(value="/api/queryArticle", method=RequestMethod.GET)
    public ResponseVo queryArticle(@RequestParam Integer id) {
		ResponseVo responseVo = new ResponseVo();
		responseVo.setSuccess(false);
		responseVo.setErrorMsg(null);
		
		if(id != null) {
			Article article = articleMapper.findArticleById(id);
			if(article != null){
				ArticleVo articalVo = (ArticleVo)SchameCopyUtil.simpleCopy(article, ArticleVo.class);
				responseVo.setArticle(articalVo);
				
				responseVo.setSuccess(true);
			}else{
				responseVo.setErrorMsg("article not found.");;
				responseVo.setSuccess(false);
			}
		}
		
        return responseVo;
    }
	
	@RequestMapping(value="/api/deleteArticle", method=RequestMethod.GET)
    public ResponseVo deleteArticle(@RequestParam Integer id) {
		ResponseVo responseVo = new ResponseVo();
		responseVo.setSuccess(false);
		responseVo.setErrorMsg(null);
		
		if(id != null) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			map.put("validStatus", "0");
			map.put("updateTime", new Date());
			
			articleMapper.updateArticleFieldsById(map);
			responseVo.setSuccess(true);
		}
		
        return responseVo;
    }
	
	@RequestMapping(value="/api/searchArticle", method=RequestMethod.GET)
    public ResponseVo searchArticle(@RequestParam String key, @RequestParam int pageNo, @RequestParam int pageSize) {
		ResponseVo responseVo = new ResponseVo();
		responseVo.setSuccess(false);
		responseVo.setErrorMsg(null);
		
		if(key != null && !"".equals(key.trim())) {
			Map<String, Object> condition = new HashMap<>();
			condition.put("title", key);
			condition.put("content", key);
			condition.put("author", key);
			
			if (pageNo <= 0) {
				pageNo = 1;
			}
			if (pageSize <= 0) {
				pageSize = 10;
			}
			
			condition.put("page", pageNo);
			condition.put("rows", pageSize);
			
			Long total = articleMapper.countArticlesByCondition(condition);
			List<Article> articles = articleMapper.findArticlesByCondition(condition);
			
			if(articles != null){
				List<ArticleVo> articleVos = new ArrayList<>();
				List<Integer> articleIds = new ArrayList<>();
				for(Article article:articles) {
					ArticleVo articalVo = (ArticleVo)SchameCopyUtil.simpleCopy(article, ArticleVo.class);
					articleVos.add(articalVo);
					articleIds.add(article.getId());
				}
				//responseVo.setArticles(articleVos);
				responseVo.setArticleIds(articleIds);
				responseVo.setTotal(total.intValue());
				
				responseVo.setSuccess(true);
			}else{
				responseVo.setErrorMsg("article not found.");;
				responseVo.setSuccess(false);
			}
		}
		
        return responseVo;
    }
}