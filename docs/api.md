# APIs
[返回首页](../README.md)

***
## 目录

- [修改记录](#修改记录)
- [wiki 所需要的API如下](#wiki-所需要的API如下)
- [概述](#概述)

***
## 修改记录
| 版本 | 修改类型 | 描述 |
|---|---|---|
| 1.0 | 创建文档 | 创建 API 文档 |

***
## 概述
本文档主要用来描述所需要的API，初版api没有采用 RESTful 风格，后续会进行改进

***
## wiki 所需要的API如下

| API URL | 描述 | 交互数据示例 |
|---|---|---|
| /api/saveArticle | 保存文章 | [示例](#example-api-savearticle) |
| /api/queryArticle?id= | 查询文章 | [示例](#example-api-queryarticle) |
| /api/searchArticle?key= | 搜索文章 | [示例](#example-api-searcharticle) |

***
### example-api-saveArticle
```
request
{
	title : '',
	content : '',
	author : 'jim',
	insertTime : '2018-04-18 20:00:00',
	updateTime : '2018-04-18 20:00:00'
}

response
{
	success : true,
	errorMsg : '',
	id : '',
	title : '',
	content : '',
	author : 'jim',
	insertTime : '2018-04-18 20:00:00',
	updateTime : '2018-04-18 20:00:00'
}
```

### example-api-queryArticle
```
request
/api/queryArticle?id=

response
{
	success : true,
	errorMsg : '',
	id : '',
	title : '',
	content : '',
	author : 'jim',
	insertTime : '2018-04-18 20:00:00',
	updateTime : '2018-04-18 20:00:00'
}
```

### example-api-searchArticle
```
request
/api/searchArticle?key=

response
{
	success : true,
	errorMsg : '',
	total : 150,
	pageNo : 1,
	pageSize : 20,
	ids : []
}
```
