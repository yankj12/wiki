package com.cowork.wiki.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cowork.wiki.dao.ArticleMongoDaoUtil;
import com.cowork.wiki.model.Article;
import com.cowork.wiki.vo.ArticleVo;
import com.yan.common.util.SchemaCopyUtil;

@Controller
public class WikiController {

	
	@Autowired
	private ArticleMongoDaoUtil articleMongoDaoUtil;
	
	// web的根路径是/wiki，所以RequestMapping不要以/wiki开头，容易冲突
	//显示输入页面 http://localhost:8012/content/viewInput
	@RequestMapping("/article/new")
	public String newArticle(){
				
		return "article";
	}

	@RequestMapping(value = "/article/saveArticle", method = RequestMethod.POST)
	public String saveArticle(ArticleVo articleVo){
		
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
				
				return "redirect:/article/" + newArticleId;
			} catch (Exception e) {
				e.printStackTrace();
				return "forward:/errot.jsp";
			}
		}else {
			return "forward:/errot.jsp";
		}
		
	}
	
	@RequestMapping("/article/{articleId}")
	public ModelAndView editArticle(@PathVariable Integer articleId){
		ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("article");
        modelAndView.addObject("articleId", articleId);
        return modelAndView;
	}
	
}