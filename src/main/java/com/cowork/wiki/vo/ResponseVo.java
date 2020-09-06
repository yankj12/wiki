package com.cowork.wiki.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class ResponseVo implements Serializable{

	private static final long serialVersionUID = 1L;
	private boolean success;
	private String errorMsg;
	
	private ArticleVo article;
	
	private List<Integer> articleIds;
	
	private int total;
	
	private int pageNo;
	
	private int pageSize;
}
