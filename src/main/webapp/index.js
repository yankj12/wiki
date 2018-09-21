
function init(){
	execQuery(1, 10);
}

function execQuery(pageNo, pageSize){
	pageNo = parseInt(pageNo);
	pageSize = parseInt(pageSize);
	
	var url = contextRootPath + "/rest/article?page=" + pageNo + "&rows=" + pageSize;
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
        		var total = result.total;
        		
        		var totalPage = 0;
        		if(total % pageSize == 0){
        			totalPage = parseInt(total/pageSize);
        		}else{
        			totalPage = parseInt(total/pageSize) + 1;
        		}
        		
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
        			
        			// tfoot部分
        			
        			innerHtml += "<tfoot><tr><td align=\"center\" colspan=\"4\">";
        			// <a href="javascript:javascript:void(0);" onclick="clickevent(this);">
        			// 首页
        			if(pageNo <= 1){
        				innerHtml += "首页&nbsp;";
        			}else{
        				innerHtml += "<a href='javascript:javascript:execQuery(1," + pageSize + ");'>首页</a>&nbsp;";
        			}
        			
        			// 上一页
        			var prePageNo = parseInt(pageNo) - 1;
        			if(prePageNo < 1){
        				prePageNo = 1;
        				innerHtml += "|&nbsp;上一页&nbsp;";
        			}else{
        				innerHtml += "|&nbsp;<a href='javascript:javascript:execQuery(" + prePageNo + "," + pageSize + ");'>上一页</a>&nbsp;";
        			}
        			
        			// 当前页
        			innerHtml += "|&nbsp;第" + pageNo + "页,&nbsp;总计&nbsp;" + totalPage + "页&nbsp;&nbsp;每页" + pageSize + "条,&nbsp;总计" + total + "条&nbsp;";
        			// 下一页
        			var nextPageNo = parseInt(pageNo) + 1;
        			if(nextPageNo > totalPage){
        				nextPageNo = totalPage;
        				innerHtml += "|&nbsp;下一页&nbsp;";
        			}else{
        				innerHtml += "|&nbsp;<a href='javascript:javascript:execQuery(" + nextPageNo + "," + pageSize + ");'>下一页</a>&nbsp;";
        			}
        			
        			// 末页
        			if(pageNo >= totalPage){
        				innerHtml += "末页&nbsp;";
        			}else{
        				innerHtml += "|&nbsp;<a href='javascript:javascript:execQuery(" + totalPage + "," + pageSize + ");'>末页</a>";
        			}
        			
        			innerHtml += "</td></tr></tfoot>";
        			
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
