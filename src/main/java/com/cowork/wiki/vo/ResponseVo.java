package com.cowork.wiki.vo;

import java.io.Serializable;
import java.util.List;

public class ResponseVo implements Serializable{

	private static final long serialVersionUID = 1L;
	private boolean success;
	private String errorMsg;
	
	private ArticleVo article;
	
	private List<ArticleVo> articles;
	
	private int total;
	
	private int pageNo;
	
	private int pageSize;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public ArticleVo getArticle() {
		return article;
	}

	public void setArticle(ArticleVo article) {
		this.article = article;
	}

	public List<ArticleVo> getArticles() {
		return articles;
	}

	public void setArticles(List<ArticleVo> articles) {
		this.articles = articles;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
