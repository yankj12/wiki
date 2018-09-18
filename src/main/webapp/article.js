/**
 * 
 */

// 关于simplemde插件的定制化配置，可以在https://github.com/sparksuite/simplemde-markdown-editor查看详细介绍
var simplemde = new SimpleMDE({
								// autoDownloadFontAwesome: If set to true, force downloads Font Awesome (used for icons). If set to false, prevents downloading. 
								// Defaults to undefined, which will intelligently check whether Font Awesome has already been included, then download accordingly.
								autoDownloadFontAwesome: false,
								// spellChecker: If set to false, disable the spell checker. Defaults to true
								spellChecker: false,    //置为false，不会从cdn.jsdelivr.net下载资源
								element: document.getElementById("article_content_simplemde")
								});


function checkContent(){
	var content = simplemde.value();
	//set value
	//simplemde.value("This text will appear in the editor");
	alert(content);
	return true;
}

//获取url中的参数
function getUrlParam(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
	var r = window.location.search.substr(1).match(reg); //匹配目标参数
	if (r != null) return unescape(r[2]); return null; //返回参数值
}

$(document).ready(function(){
	//body onload后处理的事情
	//从隐藏域中获取到文章的id
	var articleId = $("#articleId").val();
	if(articleId == null || articleId == ''){
		//如果文章id为空，表示是新增，进入页面应该是编辑模式
		//1、标题后面的Edit按钮置为隐藏
		//2、Save左边的按Cancel按钮置为隐藏
		
	}else{
		//如果文章id不为空，表示是修改，进入页面应该是阅读模式
		//根据id异步加载数据
		//1、标题隐藏文本框，改为文本模式，显示Edit
		//2、Save按钮变为Edit
		//3、Cancel按钮隐藏
		//4、文本编辑器变为阅读模式
		
		$.ajax({
	        type:"GET", 
	        url: "/wiki/rest/article/" + articleId,
	        //url:"leave/saveLeaveApplication?editType=新增",
	        dataType:"json", 
	        //data:postData,
	        contentType: "application/json", 
	        success:function(result){
	        	if (result.success){
	        		var article = result.article;
	        		
	        		//设置标题
	        		$("#article_title_edit").val(article.title);
	        		
	        		//设置作者
	        		$("#article_author_edit").val(article.author);
	        		
	        		//设置内容
	        		simplemde.value(article.content);
					
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
});

function saveArticle(){
	var article = new Object();
	
	// 先获取下articleId
	article.articleId = $("#articleId").val();
	
	//设置标题
	article.title = $("#article_title_edit").val();
	
	//设置作者
	article.author = $("#article_author_edit").val();
	
	//设置内容
	article.content = simplemde.value();
	
	var ajaxType = '';
	var url = ''
	if(article.articleId == null || article.articleId == ''){
		// 新增
		ajaxType = "PUT";
		url = "/wiki/rest/article";
	}else{
		// 修改
		ajaxType = "POST";
		url = "/wiki/rest/article/" + article.articleId;
	}
	
	$.ajax({
        type:ajaxType, 
        url: url,
        //url:"leave/saveLeaveApplication?editType=新增",
        dataType:"json", 
        data:JSON.stringify(article),
        contentType: "application/json;charset=utf-8", 
        success:function(result){
        	if (result.success){
        		var article = result.article;
        		
        		//设置articleId
        		$("#articleId").val(article.articleId);
				
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
