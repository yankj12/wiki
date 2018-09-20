
function init(){
	var url = contextRootPath + "/rest/article?page=1&rows=10";
	$.ajax({
        type:"GET", 
        url: url,
        //url:"leave/saveLeaveApplication?editType=新增",
        dataType:"json", 
        //data:JSON.stringify(article),
        contentType: "application/json;charset=utf-8", 
        success:function(result){
        	if (result.success){
        		var articles = result.articles;
        		
        		var innerHtml = "";
        		
        		if(articles != null && articles.length != null && articles.length > 0){
        			// 设置表格为单线  border-collapse:collapse;
        			innerHtml += "<table border=\"1\" style=\"border-collapse:collapse;width:100%;height:auto;\">";
        			innerHtml += "<thead><tr><td>ID</td><td>标题</td><td>作者</td><td>操作</td></tr></thead>";
        			
        			innerHtml += "<tbody>";
        			
        			var i = 0;
        			for(i=0;i<articles.length;i++){
        				var article = articles[i];
        				innerHtml += "<tr><td>" + article.articleId + "</td><td>" + article.title + "</td><td>" + article.author + "</td><td><a href=\"" + contextRootPath + "/article/" + article.articleId + "\">编辑</a></td></tr><br/>";
        				// 删除操作应该使用js触发
        			}
        			innerHtml += "</tbody>";
        			innerHtml += "<tfoot><tr><td align=\"center\" colspan=\"4\"><a>上一页</>&nbsp;|&nbsp;当前页&nbsp;1页&nbsp;,总计&nbsp;10页&nbsp;|&nbsp;<a>下一页</a></td></tr></tfoot>";
        			
        			innerHtml += "</table>";
        		}
        		
        		
        		$("#dataGridDiv").html(innerHtml);
				
        	}else{
        		alert('提示' + result.errorMsg);
        	}
        },
       	failure:function (result) {  
       		//(提示框标题，提示信息)
    		alert('提示' + '加载失败');
       	}
	});
}


/**
 * 页面初始化后做些东西
 * 
 * 这段代码最好写在js文件最后，因为写在前面可能有些引用的js还未加载
 * 
 */
$(document).ready(function(){
	console.log('init');
	init();
});
