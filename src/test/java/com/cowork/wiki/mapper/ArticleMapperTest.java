package com.cowork.wiki.mapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cowork.wiki.WikiApplication;
import com.cowork.wiki.model.Article;

@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes=WikiApplication.class)// 指定spring-boot的启动类  
public class ArticleMapperTest {

	@Autowired
	private ArticleMapper articleMapper;
	
	//@Test
	public void insertArticle() {
		Article article = new Article();
		article.setAuthor("测试");
		article.setTitle("测试标题2");
		article.setContent("测试内容2");
		article.setInsertTime(new Date());
		article.setUpdateTime(new Date());
		articleMapper.insertArticle(article);
	}

	@Test
	public void findArticlesByCondition() {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("title", "测试%");
		condition.put("offset", 0);
		condition.put("pageSize", 10);
		
		List<Article> articles = articleMapper.findArticlesByCondition(condition);
		for(Article article:articles) {
			System.out.println(article.getTitle());
		}
		
	}
}
