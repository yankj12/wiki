<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8"></meta>
<title>wiki</title>
<!-- simplemde插件将autoDownloadFontAwesome置为false，样式从本地加载 begin -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/font-awesome/4.7/css/font-awesome.min.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/font-awesome/4.7/fonts/fontawesome-webfont.woff2"/>
<!-- simplemde插件将autoDownloadFontAwesome置为false，样式从本地加载 end -->

<!-- 引入simplemde插件 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/simplemde/dist/simplemde.min.css"/>
<script src="${pageContext.request.contextPath}/simplemde/dist/simplemde.min.js"></script>

<!-- 引入jquery' -->
<script src="${pageContext.request.contextPath}/jquery/3.3.1/jquery.min.js"></script>


<style>
	body {
		width: 50em;
		margin: 0 auto;
		font-family: Tahoma, Verdana, Arial, sans-serif;
	}
</style>
</head>
<body>
	<form id="fm" action="/article/input" onSubmit="return checkContent()">
		<table width="100%">
			<tr>
				<td width="85%">
					<input type="text" id="article_title_edit" name="title" value=""  style="width:100%"/>
				</td>
				<td width="15%" align="middle">
					<input type="button" id="article_title_edit_btn" value="Edit" style="width:90%"/>
				</td>
			</tr>
		</table>
		<table width="100%">
			<tr>
				<td>作者</td>
				<td>
					<input type="text" id="article_author_edit" name="author" th:value=""/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<textarea id="article_content_simplemde" name="content" th:text=""></textarea>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="right">
					<input type="button" id="article_save_btn" value="Save" onClick="saveArticle()"/>
				</td>
			</tr>
			
		</table>
	</form>
</body>
<!-- 引入article.js -->
<script src="${pageContext.request.contextPath}/article.js"></script>

</html>