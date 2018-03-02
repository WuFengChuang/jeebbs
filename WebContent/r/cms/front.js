Cms = {};
/**
 * 浏览次数
 */
Cms.viewCount = function(base, contentId, viewId, commentId, downloadId, upId,
		downId) {
	viewId = viewId || "views";
	commentId = commentId || "comments";
	downloadId = downloadId || "downloads";
	upId = upId || "ups";
	downId = downId || "downs";
	$.getJSON(base + "/content_view.jspx", {
		contentId : contentId
	}, function(data) {
		if (data.length > 0) {
			$("#" + viewId).text(data[0]);
			$("#" + commentId).text(data[1]);
			$("#" + downloadId).text(data[2]);
			$("#" + upId).text(data[3]);
			$("#" + downId).text(data[4]);
		}
	});
}
/**
 * 成功返回true，失败返回false。
 */
Cms.up = function(base, contentId, origValue, upId) {
	upId = upId || "ups";
	var updown = $.cookie("_cms_updown_" + contentId);
	if (updown) {
		return false;
	}
	$.cookie("_cms_updown_" + contentId, "1");
	$.get(base + "/content_up.jspx", {
		"contentId" : contentId
	}, function(data) {
		$("#" + upId).text(origValue + 1);
	});
	return true;
}
/**
 * 成功返回true，失败返回false。
 */
Cms.down = function(base, contentId, origValue, downId) {
	downId = downId || "downs";
	var updown = $.cookie("_cms_updown_" + contentId);
	if (updown) {
		return false;
	}
	$.cookie("_cms_updown_" + contentId, "1");
	$.get(base + "/content_down.jspx", {
		contentId : contentId
	}, function(data) {
		$("#" + downId).text(origValue + 1);
	});
	return true;
}
/**
 * 获取附件地址
 */
Cms.attachment = function(base, contentId, n, prefix) {
	$.get(base + "/attachment_url.jspx", {
		"cid" : contentId,
		"n" : n
	}, function(data) {
		var url;
		for (var i = 0;i < n; i++) {
			url = base + "/attachment.jspx?cid=" + contentId + "&i=" + i
					+ data[i];
			$("#" + prefix + i).attr("href", url);
		}
	}, "json");
}
/**
 * 提交评论
 */
Cms.comment = function(callback, form) {
	form = form || "commentForm";
	$("#" + form).validate( {
		submitHandler : function(form) {
			$(form).ajaxSubmit( {
				"success" : callback,
				"dataType" : "json"
			});
		}
	});
}
/**
 * 获取评论列表
 * 
 * @param siteId
 * @param contentId
 * @param greatTo
 * @param recommend
 * @param orderBy
 * @param count
 */
Cms.commentList = function(base, c, options) {
	c = c || "commentListDiv";
	$("#" + c).load(base + "/comment_list.jspx", options);
}
/**
 * 客户端包含登录
 */
Cms.loginCsi = function(base, c, options) {
	c = c || "loginCsiDiv";
	$("#" + c).load(base + "/login_csi.jspx", options);
}
/**
 * 向上滚动js类
 */
Cms.UpRoller = function(rid, speed, isSleep, sleepTime, rollRows, rollSpan,
		unitHight) {
	this.speed = speed;
	this.rid = rid;
	this.isSleep = isSleep;
	this.sleepTime = sleepTime;
	this.rollRows = rollRows;
	this.rollSpan = rollSpan;
	this.unitHight = unitHight;
	this.proll = $('#roll-' + rid);
	this.prollOrig = $('#roll-orig-' + rid);
	this.prollCopy = $('#roll-copy-' + rid);
	// this.prollLine = $('#p-roll-line-'+rid);
	this.sleepCount = 0;
	this.prollCopy[0].innerHTML = this.prollOrig[0].innerHTML;
	var o = this;
	this.pevent = setInterval(function() {
		o.roll.call(o)
	}, this.speed);
}
Cms.UpRoller.prototype.roll = function() {
	if (this.proll[0].scrollTop > this.prollCopy[0].offsetHeight) {
		this.proll[0].scrollTop = this.rollSpan + 1;
	} else {
		if (this.proll[0].scrollTop % (this.unitHight * this.rollRows) == 0
				&& this.sleepCount <= this.sleepTime && this.isSleep) {
			this.sleepCount++;
			if (this.sleepCount >= this.sleepTime) {
				this.sleepCount = 0;
				this.proll[0].scrollTop += this.rollSpan;
			}
		} else {
			var modCount = (this.proll[0].scrollTop + this.rollSpan)
					% (this.unitHight * this.rollRows);
			if (modCount < this.rollSpan) {
				this.proll[0].scrollTop += this.rollSpan - modCount;
			} else {
				this.proll[0].scrollTop += this.rollSpan;
			}
		}
	}
}
Cms.LeftRoller = function(rid, speed, rollSpan) {
	this.rid = rid;
	this.speed = speed;
	this.rollSpan = rollSpan;
	this.proll = $('#roll-' + rid);
	this.prollOrig = $('#roll-orig-' + rid);
	this.prollCopy = $('#roll-copy-' + rid);
	this.prollCopy[0].innerHTML = this.prollOrig[0].innerHTML;
	var o = this;
	this.pevent = setInterval(function() {
		o.roll.call(o)
	}, this.speed);
}
Cms.LeftRoller.prototype.roll = function() {
	if (this.proll[0].scrollLeft > this.prollCopy[0].offsetWidth) {
		this.proll[0].scrollLeft = this.rollSpan + 1;
	} else {
		this.proll[0].scrollLeft += this.rollSpan;
	}
}
Cms.loginSSO=function(base){
	var username=$.cookie('username');
	var sessionId=$.cookie('JSESSIONID');
	var ssoLogout=$.cookie('sso_logout');
	if(username!=null){
		if(sessionId!=null||(ssoLogout!=null&&ssoLogout=="true")){
			$.post(base+"/sso/login.jspx", {
				 username:username,
				 sessionId:sessionId,
				 ssoLogout:ssoLogout
			}, function(data) {
					if(data.result=="login"||data.result=="logout"){
						location.reload();
					}
			}, "json");
		}
	}
}
Cms.loginAdmin=function(base){
	var sessionKey=localStorage.getItem("sessionKey");
	if(sessionKey==null||sessionKey==""){
		$.post(base+"/adminLogin.jspx", {
		}, function(data) {
			if(data.sessionKey!=""){
				localStorage.setItem("sessionKey", data.sessionKey); 
				localStorage.setItem("userName", data.userName); 
			}
		}, "json");
	}
}
Cms.logoutAdmin=function(base){
	var sessionKey=localStorage.getItem("sessionKey");
	var userName=localStorage.getItem("userName");
	if(sessionKey!=null&&sessionKey!=""&&userName!=""){
		$.post(base+"/adminLogout.jspx", {
			userName:userName,
			sessionKey:sessionKey
		}, function(data) {
		}, "json");
		localStorage.removeItem("sessionKey");
		localStorage.removeItem("userName");
	}
}
/**
 * 收藏信息 1收藏 3取消收藏
 */
Cms.collect = function(base, topicId, operate,origValue,
		collectCountId,showSpanId,hideSpanId) {
	$.post(base + "/topic/operate.jspx", {
		"topicId" : topicId,
		"operate" : operate
	}, function(data) {
		if(data.result){
			if(operate==1){
				$("#" + collectCountId).text(parseInt(origValue) + 1);
				$("#"+showSpanId).show();
				$("#"+hideSpanId).hide();
			}else{
				$("#" + collectCountId).text(parseInt(origValue) - 1);
				$("#"+showSpanId).hide();
				$("#"+hideSpanId).show();
			}
		}else{
			alert("请先登录");
		}
	}, "json");
}
Cms.collectCsi = function(base,collectCsiDiv, tpl, topicId) {
	collectCsiDiv = collectCsiDiv || "collectCsiDiv";
	$("#"+collectCsiDiv).load(base+"/csi_custom.jspx?tpl="+tpl+"&topicId="+topicId);
}
/**
 * 检测是否已经收藏信息
 */
Cms.collectexist = function(base, topicId,showSpanId,hideSpanId) {
	$.post(base + "/topic/collect_exist.jspx", {
		"topicId" : topicId
	}, function(data) {
		if(data.result){
			$("#"+showSpanId).show();
			$("#"+hideSpanId).hide();
		}else{
			$("#"+showSpanId).hide();
			$("#"+hideSpanId).show();
		}
	}, "json");
}
/**
 * 主题互动 0点赞  2关注  3取消点赞  5取消关注 
 */
Cms.topicOp = function(base, topicId, operate,
		opCountId,showSpanId,hideSpanId) {
	$.post(base + "/topic/operate.jspx", {
		"topicId" : topicId,
		"operate" : operate
	}, function(data) {
		if(data.result){
			var origValue=$("#"+opCountId).html();
			if(operate==0||operate==2){
				$("#" + opCountId).text(parseInt(origValue) + 1);
				$("#"+showSpanId).show();
				$("#"+hideSpanId).hide();
			}else{
				$("#" + opCountId).text(parseInt(origValue) - 1);
				$("#"+showSpanId).hide();
				$("#"+hideSpanId).show();
			}
		}else{
			alert("请先登录");
		}
	}, "json");
}
/**
 * 帖子点赞 0点赞 3取消点赞 
 */
Cms.postUp = function(base, postId, operate,
		opCountId,showSpanId,hideSpanId) {
	$.post(base + "/post/up.jspx", {
		"postId" : postId,
		"operate" : operate
	}, function(data) {
		if(data.result){
			var origValue=$("#"+opCountId).html();
			if(operate==0||operate==2){
				$("#" + opCountId).text(parseInt(origValue) + 1);
				$("#"+showSpanId).show();
				$("#"+hideSpanId).hide();
			}else{
				$("#" + opCountId).text(parseInt(origValue) - 1);
				$("#"+showSpanId).hide();
				$("#"+hideSpanId).show();
			}
		}else{
			alert("请先登录");
		}
	}, "json");
}
Cms.getCookie=function getCookie(c_name){
	if (document.cookie.length>0)
	  {
	  	c_start=document.cookie.lastIndexOf(c_name + "=")
		  if (c_start!=-1)
		    { 
			    c_start=c_start + c_name.length+1;
			    c_end=document.cookie.indexOf(";",c_start);
			    if (c_end==-1){
			    	c_end=document.cookie.length;
			    } 
			    return unescape(document.cookie.substring(c_start,c_end))
		    } 
		  }
	return ""
}