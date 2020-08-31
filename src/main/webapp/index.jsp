<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8"></meta>
<title>index</title>

<style>
	body {
		width: 50em;
		margin: 0 auto;
		font-family: Tahoma, Verdana, Arial, sans-serif;
	}
</style>
<!-- 引入jquery' -->
<script src="${pageContext.request.contextPath}/jquery/3.3.1/jquery.min.js"></script>

<script type="text/javascript">
  var contextRootPath = "${pageContext.request.contextPath}";
</script>

</head>
<body>
<a href="${pageContext.request.contextPath}/article/new">new artical</a><br/>

<div id="dataGridDiv">
</div>
</body>

<!-- 引入article.js -->
<script src="${pageContext.request.contextPath}/index.js"></script>
</html>