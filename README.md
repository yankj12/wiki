# wiki
wiki 是一个用来整理团队关于产品或项目的设计、分析、沟通过程中内容的项目，旨在收集平时沟通中关于产品和项目的内容，以减少后续编写文档或者查找相关资料的工作量。

***
## 目录

- [计划包含的功能](#计划包含的功能)
- [数据结构](#数据结构)
- [APIs](./docs/api.md)
- [页面](#页面展示)
	- [页面设计](#页面设计)
	- [前后台交互](#前后台交互)
- [感谢](#感谢)

***
## 计划包含的功能
| 功能 | 描述 | 状态 | 时间安排 | 链接 |
|---|---|---|---|---|
| 录入内容 | 用户在页面上的文本域录入内容，点击保存按钮进行保存 | 待开发 | 2018/04/18-2018/04/22 |  |
| 文本域支持markdown | 文本域支持使用markdown语法的内容，采用markdown插件 | 待开发 | 暂未安排 |  |
| 增加搜索页面 | 搜索页面使用 all in one 的搜索框 | 待开发 | 暂未安排| |
| 增加标签的功能 | 增加给内容标记标签的功能（手动标记） | 待开发 | 暂未安排 | |
|修改日志|后续版本采用修改日志记录对文章的修改|待开发|暂未安排||

## 数据结构
wiki项目计划调用数据保存微服务的方式进行保存。与服务交互的数据结构如下：

```
Article
{
	id : '',
	title : '',
	content : '',
	author : 'jim',
	insertTime : '2018-04-18 20:00:00',
	updateTime : '2018-04-18 20:00:00'
}

```

## 页面展示
### **页面设计**
- 标题栏
	- 类似GitHub的issue的标题栏
- 文本编辑框
	- 计划采用[sparksuite/simplemde-markdown-editor](https://github.com/sparksuite/simplemde-markdown-editor)，一个支持markdown语法的文本编辑器

- 作者
	- 现在文章作者采用文本框填写
	- 后续版本取消作者属性。采用修改日志记录对文章的修改。
- 修改日志
	- 后续版本采用修改日志记录对文章的修改。

-  标签
	- 标签在内容编辑框右侧，或者上方展示

- 前台页面跳转流程设计
	此汇总表格只说明页面跳转流程，并不负责详细描述每个URL对应页面中的内容。

|URL|说明|
|---|---|
| /wiki |进入文章查询页面，以列表形式展示文章|
| /wiki/articles | 文章展示页面，展示所有的文章|
| /wiki/article/{id} |进入到id为`{id}`的文章的展示页，页面上点击`Edit`等按钮可以进行文章的编辑操作|
| /wiki/article/new |进入文章的新增页面，新增页面点击`Save`之后，跳转到 `/wiki/article/{id}` 页面|


### **前后台交互**
标题栏与文本编辑框与后台交互都采用异步交互的方式，效果参考GitHub的issue
- 创建新的文章
	- 类似GitHub，从查询页面，通过New按钮，跳转到创建文章的页面。此时，标题、内容都是可编辑的，点击内容下方的提交按钮，同时将标题和内容保存，并且标题和内容变为阅读视图。
- 修改及保存标题
	- 点击标题右侧的Edit按钮，标题变为文本框，可以修改，标题右侧的Edit变为Save，点击Save，将修改后的标题保存，并且标题变为阅读视图。
	- 修改标题时内容还是阅读视图。
- 修改及保存文章内容
	- 修改内容，内容下方按钮变为Cancle和Update，修改或者取消修改后，内容变为阅读视图。
	- 修改内容时，标题是阅读视图，是不变的
- 标签
	标签相关的操作都采用异步交互方式
	- 创建标签
	- 给文章打标签

- 前后台交互API汇总
	整理API内容如下表：

|序号| API URL | 描述 | 交互数据示例 |
|---|---|---|---|
|1| /wiki/api/article/{id} |进入文章编辑页面，加载文章数据的api，RESTful风格GET |示例暂无|
|2| /wiki/api/article/{id} |新增或者修改文章，保存文章内容，RESTful风格PUT，POST|示例暂无|
|3|  |修改文章标题后，保存标题，RESTful风格 |示例暂无|
|4|  |修改文章内容后，保存内容，RESTful风格 |示例暂无|
|备注||考虑3和4是否可以使用更新文章整体的api|

## 感谢
[yankj12](https://github.com/yankj12)

[cc](https://github.com/cc-lady)

[Orange](https://github.com/43942692)

[wurenshuang](https://github.com/wurenshuang1992)

## 常见问题

```
java.util.concurrent.ExecutionException: org.apache.catalina.LifecycleException: Failed to start component [StandardEngine[Catalina].StandardHost[localhost].StandardContext[/wiki]]
Caused by: java.lang.ClassNotFoundException: org.springframework.core.io.Resource

```
缺少spring核心包

```
<!-- https://mvnrepository.com/artifact/org.springframework/spring-core -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
    <version>4.3.0.RELEASE</version>
</dependency>
```

