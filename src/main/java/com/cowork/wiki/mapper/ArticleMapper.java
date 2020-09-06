package com.cowork.wiki.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.cowork.wiki.model.Article;

@Mapper
public interface ArticleMapper {
	
	public String insertArticle(Article article);

	public List<Article> findArticlesByCondition(Map<String, Object> condition);
	
	public Long countArticlesByCondition(Map<String, Object> condition);
	
	public void updateArticle(Article article);
	
	public Article findArticleById(Integer id);
	
	public void updateArticleFieldsById(Integer id, Map<String, Object> map);
}
