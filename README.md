# wiki
wiki 是一个用来整理团队关于产品或项目的设计、分析、沟通过程中内容的项目，旨在收集平时沟通中关于产品和项目的内容，以减少后续编写文档或者查找相关资料的工作量。

***
## 计划包含的功能
| 功能 | 描述 | 状态 | 时间安排 | 链接 |
|---|---|---|---|---|
| 录入内容 | 用户在页面上的文本域录入内容，点击保存按钮进行保存 | 待开发 | 2018/04/18-2018/04/22 |  |
| 文本域支持markdown | 文本域支持使用markdown语法的内容，采用markdown插件 | 待开发 | 暂未安排 |  |
| 增加搜索页面 | 搜索页面使用 all in one 的搜索框 | 待开发 | 暂未安排| |
| 增加标签的功能 | 增加给内容标记标签的功能（手动标记） | 待开发 | 暂未安排 | |

## 数据结构
wiki项目计划调用数据保存微服务的方式进行保存。与服务交互的数据结构如下：
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

## 感谢
[yankj12](https://github.com/yankj12)

[cc](https://github.com/cc-lady)

[Orange](https://github.com/43942692)

[wurenshuang](https://github.com/wurenshuang1992)

