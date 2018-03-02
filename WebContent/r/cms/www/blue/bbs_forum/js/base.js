$(function () {
	$('.user-logo').hover(function () {
		$(this).find('.tooltip').show()
	},function () {
		$(this).find('.tooltip').hide()
	})
	
	$('.tooltip-con').hover(function () {
		$(this).addClass('active');
		$(this).find('.tooltip').show()
	},function () {
		$(this).removeClass('active');
		$(this).find('.tooltip').hide()
	})
	
	$('.gift-box').hover(function () {
		 $(this).find('.gift-inner').show();
	
	},function () {
		 $(this).find('.gift-inner').hide();
	})
	
	$('body').on('mouseenter mouseleave','.magic-tool-box',function (e) {
			console.log();
			if(e.type==='mouseenter'){
				$(this).find('.tooltip').show();
			}else{
				$(this).find('.tooltip').hide();
			}
	})
	
	
//		$('.magic-tool-box').hover(function () {      
//		$(this).find('.tooltip').show();
//	},function(){
//$(this).find('.tooltip').hide();
// })
	
	/*密码显示英藏*/
	$('.eye').on('click',function () {
		var flag=$(this).hasClass('see-close');
		 if(flag){
		 	$(this).removeClass('see-close').addClass('see-on');
		 	var txt=$(this).siblings('.pwd').val();
		$(this).siblings('.pwd').hide();
		$(this).siblings('.show-pwd').val(txt).show();
		 }else{
		 		$(this).removeClass('see-on').addClass('see-close');
				$(this).siblings('.pwd').show();
				$(this).siblings('.show-pwd').hide();
		 }
	})

	
	/*hover显影*/
	  $('.bbs-content-card li').hover(function () {
	  	$(this).find('.hover-theme').css('visibility','visible');
	  },function () {
	  	$(this).find('.hover-theme').css('visibility','hidden');
	  }) 
	
})

function showModel(select) {
	var docH=$('body').height();
	var w= parseInt($(select).width())/2;
	var h=  parseInt($(select).height())/2;	
	 $('.cover').remove();
	 $('body').append("<div class='cover'></div>");
	 $('.cover').css('height',docH+'px');
    $(select).show().css('margin-left',-w+'px');
     $(select).show().css('margin-top',-h+'px');
	 
}

function closeModel() {
	 $('.cover').remove();
	 $('.model-panel').hide();
}
$('body').on('click','.cover',function(){
   closeModel()
})

function displayPostReplayDive(postId){
	$("#innerPost"+postId).toggle();
}
function cancelContent(postId){
	$("#postReplyContent"+postId).val("");
}
function cancelTopicContent(topicId){
	$("#topicReplyContent"+topicId).val("");
}
function cancelPostContent(postId){
	$("#postChildReplyContent"+postId).val("");
}

//回复首页的帖子
function replyPostContent(base,topicId,postId){  
	$.post(base+"/post/o_saveAjax.jspx",{
		"content": $("#postReplyContent"+postId).val(),
		"topicId":topicId,
		"parentId":postId
	},function(data){
		if(data.status==1){
			getTopicPostInner(base,topicId,1);
		}else if(data.status==-2){
			showModel('#noPerm');
		}else if(data.status==-1){
			getloginTabOut();
			showModel('#login');
		}else if(data.status==-3){
			showModel('#intervalShort');
		}else if(data.status==-4){
			showModel('#uploadError');
		}
	});
}
//回复帖子详细页的帖子
function replyPostChildContent(base,topicId,postId){
	$.post(base+"/post/o_saveAjax.jspx",{
		"content": $("#postChildReplyContent"+postId).val(),
		"topicId":topicId,
		"parentId":postId
	},function(data){
		if(data.status==1){
			getPostChildInner(base,postId,1);
		}else if(data.status==-2){
			showModel('#noPerm');
		}else if(data.status==-1){
			getloginTabOut();
			showModel('#login');
		}else if(data.status==-3){
			showModel('#intervalShort');
		}else if(data.status==-4){
			showModel('#uploadError');
		}           
	});
}
//回复主题
function replyTopicContent(base,topicId){  
	$.post(base+"/post/o_saveAjax.jspx",{
		"content": $("#topicReplyContent"+topicId).val(),
		"topicId":topicId
	},function(data){   
		if(data.status==1){
			getTopicPostInner(base,topicId,1);
		}else if(data.status==-2){
			showModel('#noPerm');
		}else if(data.status==-1){
			getloginTabOut();
			showModel('#login');
		}else if(data.status==-3){
			showModel('#intervalShort');
		}else if(data.status==-4){
			showModel('#uploadError');
		}
	});
}
function getTopicPostInner(base,topicId,pageNo){ 
	if(pageNo==null){
		pageNo=1;
	}
	$("#topicposts"+topicId).load(base+"/topic/getPostPage.jspx?orderBy=1&topicId="+topicId+"&pageNo="+pageNo);
}

//获取帖子回复列表
function getPostChildPage(base,postId){
	var h=$("#postChildPage"+postId).html();
	if(h!=""){
		clearPostChildPage(postId);
	}else{
		$("#postChildPage"+postId).load(base+"/post/listChild.jspx?orderBy=1&parentId="+postId);
	}
}
//清空帖子回复列表
function clearPostChildPage(postId){
	$("#postChildPage"+postId).html("");
}
 //刷新帖子回复列表
function getPostChildInner(base,parentId,pageNo){
	if(pageNo==null){
		pageNo=1;
	}
	$("#postChildPage"+parentId).load(base+"/post/listChild.jspx?orderBy=1&parentId="+parentId+"&pageNo="+pageNo);
}
/**
 * 帖子点赞 0点赞 3取消点赞 
 */
function postUp(base,postId, operate) {
	$.post(base+"/post/up.jspx", {
		"postId" : postId,
		"operate" : operate
	}, function(data) {
		if(data.result){
			var origValue=$("#pupcount"+postId).text();
			if(operate==0){
				$("#pupcount" + postId).text(parseInt(origValue) + 1);
				$("#cpupcount" + postId).text(parseInt(origValue) + 1);
				$("#uppost"+postId).hide();
				$("#cuppost"+postId).show();
			}else{
				$("#cpupcount" + postId).text(parseInt(origValue) - 1);
				$("#pupcount" + postId).text(parseInt(origValue) - 1);
				$("#uppost"+postId).show();
				$("#cuppost"+postId).hide();
			}
		}else{
			getloginTabOut();
			showModel('#login');
		}
	}, "json");
}
//申请好友
function apply(base,id, val,pid){
	$.post(base+"/member/applyJson.jhtml",{"u": val},function(data){
		alert(data.message);	
		$("#u"+id+"_"+pid).css("iconfont  icon-tongxunlu green");
	});
}
//删除好友
function applyDel(base,id, uid,pid){
	var r=confirm("确定删除好友吗")
  if (r==true)
    {
    $.post(base+"/member/delJson.jhtml",{"uid": uid},function(data){
		if(data.status==-1){
			$("#u"+id+"_"+pid).removeClass(" icon-tongxunlu").addClass("icon-tongxunlu1");
			 alert("删除成功");
		}
	});
    }
  else
    {
    document.write("You pressed Cancel!")
    }
	
}
//举报帖子
function report(base,url){
	$.post(base+"/member/report.jhtml",{"url": url},function(data){
		alert("举报成功!");
	}, "json");
}
//禁用用户
function forBidden(base,userId,forumId){
	$.post(base+"/user/forbidden.jspx",{
		"userId": userId,
		"forumId":forumId
	},function(data){
		if(data.status==1){
			$("a[id^='forbiddenLink_"+userId+"']").each(function(){
				$(this).prop("title","禁用");
				$(this).addClass("jy-btn");
                                $(this).removeClass("qxjy-btn");
			});
		}else if(data.status==2){
			$("a[id^='forbiddenLink_"+userId+"']").each(function(){
				$(this).prop("title","解除禁用");
				$(this).addClass("qxjy-btn");
                                $(this).removeClass("jy-btn");
			});
		}else if(data.status==0){
			alert("禁用失败!");
		}
	},"json");
}
//关注用户 operate 0关注  1取消关注
function attent(base,userId,operate){
	$.post(base+"/member/attent.jhtml",{
		"userId": userId
	},function(data){ 
		if(data.status==0){
			$("a[id^='a"+userId+"']").each(function(i){			
			   $(this).removeClass('icon-guanzhu').addClass('icon-yiguanzhu')
			   $(this).attr('title','取消关注');
			    $(this).find('.tooltip-down').html('取消关注');
			});
		}else{
			$("a[id^='a"+userId+"']").each(function(i){
				   $(this).removeClass('icon-yiguanzhu').addClass('icon-guanzhu')
			   $(this).attr('title','添加关注');
			    $(this).find('.tooltip-down').html('添加关注');
			});
		}
	},"json");
}
/**
 * 主题互动 0点赞  2关注  3取消点赞  5取消关注 
 */
function topicOp(base,topicId, operate) {
	$.post(base+"/topic/operate.jspx", {
		"topicId" : topicId,
		"operate" : operate
	}, function(data) {
		if(data.result){
			if(operate==2){
				$("#attenttopic"+topicId).hide();
				$("#cattenttopic"+topicId).show();
			}else if(operate==5){
				$("#attenttopic"+topicId).show();
				$("#cattenttopic"+topicId).hide();
			}else if(operate==0){
				$("#uptopic"+topicId).hide();
				$("#cuptopic"+topicId).show();
				var origValue=$("#upcount"+topicId).html()
				$("#upcount"+topicId).html(parseInt(origValue) + 1);
				$("#cupcount"+topicId).html(parseInt(origValue) + 1);
			}else if(operate==3){
				$("#uptopic"+topicId).show();
				$("#cuptopic"+topicId).hide();
				var origValue=$("#upcount"+topicId).html()
				$("#upcount"+topicId).html(parseInt(origValue) - 1);
				$("#cupcount"+topicId).html(parseInt(origValue) - 1);
			}
		}else{
			getloginTabOut();
			showModel('#login');
		}
	}, "json");
}
//检查内容
function checkContent(){
  if($("#_editor_textarea_more").val()==""){
	 alert("内容不能为空");
	 return false;
  }
  return true;
}
//主题置顶
 function upTopLevel(base,topicId,forumId){
	$.post(base+"/topic/o_upTopAjax.jspx",{
		"topicId": topicId,
		"forumId":forumId
	},function(data){
		if(data.status==1){
			$("#topLevel"+topicId).prop("title","取消置顶");
			$("#topLevel"+topicId+" span").html("取消置顶");
		}else if(data.status==2){
			$("#topLevel"+topicId).prop("title","置顶");
			$("#topLevel"+topicId+" span").html("置顶");
		}
	},"json");
 }
 //阅读全文
 function loadContent(base,postId){
	$.post(base+"/post/ajaxGetPost.jspx",{
		"postId": postId
	},function(data){
		$("#topicContentAll"+postId).html(data.content);
		$("#packup"+postId).show();
		$("#topicContent"+postId).hide();
		$("#readMore"+postId).hide();
		$("#themePics"+postId).hide();
	},"json");
}
//收起全文
function packupuContent(postId){
		$("#topicContentAll"+postId).html("");
		$("#packup"+postId).hide();
		$("#topicContent"+postId).show();
		$("#readMore"+postId).show();
		$("#themePics"+postId).show();
}
//获取主题回复
function getTopicPost(base,topicId){
	var h=$("#topicposts"+topicId).html();
	if(h!=""){
		clearTopicPost(topicId);
	}else{
		$("#topicposts"+topicId).load(base+"/topic/getPostPage.jspx?orderBy=1&topicId="+topicId);
	}
}
//清除主题回复
function clearTopicPost(topicId){
	$("#topicposts"+topicId).html("");
}