$(function(){
	$(".bbs-content-card .top .card").click(function(){
		$(this).addClass("active").siblings().removeClass("active");
	});
	
	
	$(".tabelTextarea").bind("input propertychange",function(){
		var $length = $(".tabelTextarea").val().length;
		$(".zishu span").html($length);
	});
	
	$('.btn-add').click(function(){
		var num = $(this).siblings('.shopnum').val();
		num ++;
		$(this).siblings('.shopnum').val(num);
	})
	$('.reduce').click(function(){
		var num = $(this).siblings('.shopnum').val();
		if(num>=1){
			num --;
		}
		$(this).siblings('.shopnum').val(num);
	});
	var friendPinglunOff = true
	$(".friend .icon-pinglun1").click(function(){
		if(friendPinglunOff){
			$(this).css("color","#1b8be2");
			$(this).parents("li").find(".gzdr-info").fadeOut();
			friendPinglunOff = false
		}else{
			$(this).css("color","#c0cbd3");
			$(this).parents("li").find(".gzdr-info").fadeIn();
			friendPinglunOff = true
		}
		
		$(this).parents("li").find(".liuyanbox").slideToggle("slow");
		
	});
	$(".friend .lookAll").click(function(){
		if(friendPinglunOff){
                        $(this).parents(".friend").find(".icon-pinglun1").css("color","#1b8be2");
			$(this).parents("li").find(".gzdr-info").fadeOut();
			friendPinglunOff = false
		}else{
			$(this).parents("li").find(".gzdr-info").fadeIn();
			friendPinglunOff = true
		}
		
		$(this).parents("li").find(".liuyanbox").slideToggle("slow");
		
	});
	$(".hiddenFriend").click(function(){
		var status = $(this).attr("status");
		if(status==1){
			$(".rightFriendList").animate({'width':'1px','paddingLeft':0},200);
			$(this).animate({right:'0px'},200);
			$(this).attr("status",0)
		}else{
			$(".rightFriendList").animate({'width':'280px','paddingLeft':30},200);
			$(this).animate({right:'280px'},200);
			$(this).attr("status",1)
		}
	});
	
	$("#tixian").keyup(function(){
		var str = $(this).val();
		var str1 = str.replace(/[^\d.]/g,"");
		$(this).val(str1);
		
	});
})
