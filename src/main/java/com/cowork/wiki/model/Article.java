package com.cowork.wiki.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Article implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String title;
	private String content;
	private String author;
	private String validStatus;
	private Date insertTime;
	private Date updateTime;	
}
