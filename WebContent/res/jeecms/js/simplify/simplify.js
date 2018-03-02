$(function	()	{
	//scrollable sidebar
	$('.scrollable-sidebar').slimScroll({
		height: '100%',
		size: '0px'
	});
	
	
	$('.live-item').slimScroll({
		height: '356px',
		size: '7px',
		color:'#eaeaea',
		railColor: '#3f9f9f9',
		disableFadeOut: false, //是否 鼠标经过可滚动区域时显示组件，离开时隐藏组件
        railVisible: true
	});
	//Collapsible Sidebar Menu
	$('.sidebar-menu .openable > a').click(function()	{
		
		if(!$('aside').hasClass('sidebar-mini') || Modernizr.mq('(max-width: 991px)'))	{
			if( $(this).parent().children('.submenu').is(':hidden') ) {
				$(this).parent().siblings().removeClass('open').children('.submenu').slideUp(200);
				$(this).parent().addClass('open').children('.submenu').slideDown(200);
			}
			else	{
				$(this).parent().removeClass('open').children('.submenu').slideUp(200);
			}
		}
		
		return false;
	});

	//Open active menu
	if(!$('.sidebar-menu').hasClass('sidebar-mini') || Modernizr.mq('(max-width: 767px)'))	{
		$('.openable.open').children('.submenu').slideDown(200);
	}
	
	//Toggle User container on sidebar menu
	$('#btn-collapse').click(function()	{
		$('.sidebar-header').toggleClass('active');
	});
	
	//theme setting
	$("#theme-setting-icon").click(function()	{ 
		if($('#theme-setting').hasClass('open'))	{
			$('#theme-setting').removeClass('open');
			$('#theme-setting-icon').removeClass('open');
		}
		else	{
			$('#theme-setting').addClass('open');
			$('#theme-setting-icon').addClass('open');
		}

		return false;
	});
	
	$('#sidebarToggleLG').click(function()	{
		if($('.wrapper').hasClass('display-right'))	{
			$('.wrapper').removeClass('display-right');
			$('.sidebar-right').removeClass('active');
		}
		else	{
			//$('.nav-header').toggleClass('hide');
			$('.top-nav').toggleClass('sidebar-mini');
			$('aside').toggleClass('sidebar-mini');
			$('footer').toggleClass('sidebar-mini');
			$('.main-container').toggleClass('sidebar-mini');
			
			$('.main-menu').find('.openable').removeClass('open');
			$('.main-menu').find('.submenu').removeAttr('style');
		}		
	});
	
	$('#sidebarToggleSM').click(function()	{
		$('aside').toggleClass('active');
		$('.wrapper').toggleClass('display-left');
	});
	
	
	$('.dropdown-menu input').click(function(e) {
        e.stopPropagation(); //This will prevent the event from bubbling up and close the dropdown when you type/click on text boxes.
    });
	
	//to do list
	$('.task-finish').click(function()	{ 
		if($(this).is(':checked'))	{
			$(this).parent().parent().addClass('selected');					
		}
		else	{
			$(this).parent().parent().removeClass('selected');
		}
	});

	//Delete to do list
	$('.task-del').click(function()	{			
		var activeList = $(this).parent().parent();

		activeList.addClass('removed');
				
		setTimeout(function() {
			activeList.remove();
		}, 1000);
			
		return false;
	});
	
	var $activeWidget = '';
	var $activeWidgetHeader;
	var $headerHeight;
	var $screenHeight;
	var $widgetHeight;
	var $borderHeight = 3;

	//Smart Widget

	// Refresh Widget
	$('.widget-refresh-option').click(function()	{

		$activeWidget = $(this).parent().parent().parent();
		
		var $activeSpinIcon = $activeWidget.find('.refresh-icon-animated').fadeIn();

		setTimeout(function() {
			$activeSpinIcon.fadeOut();
		},2000);

		return false;
	});

	// Collasible Widget
	$('.widget-collapse-option').click(function()	{
		$activeWidget = $(this).parent().parent().parent();

		$activeWidget.find('.smart-widget-inner').slideToggle();
		$activeWidget.toggleClass('smart-widget-collapsed');

		var $activeSpinIcon = $activeWidget.find('.refresh-icon-animated').fadeIn();

		setTimeout(function() {
			$activeSpinIcon.fadeOut();
		},500);

		$activeWidget = '';

		return false;
	});
        /*高级设置*/
     	$('.setting-collapse-option').click(function()	{
		$activeWidget = $(this).parent().parent().parent();
		$activeWidget.find('.smart-widget-inner').toggle();
		$activeWidget.toggleClass('smart-widget-collapsed');
		
		
		/*var $activeSpinIcon = $activeWidget.find('.refresh-icon-animated').fadeIn();
		setTimeout(function() {
			$activeSpinIcon.fadeOut();
		},200);*/
      	    if($activeWidget.is('.smart-widget-collapsed')){
			$(this).find('.setting-collapse-states').text('+');
		}else{
			$(this).find('.setting-collapse-states').text('-');
		}
		$activeWidget = '';
		return false;
	});
     
     
	//Changing Widget Color
	$('.widget-toggle-hidden-option').click(function()	{
		$activeWidget = $(this).parent().parent().parent();

		$activeWidget.find('.smart-widget-hidden-section').slideToggle();	

		var $activeSpinIcon = $activeWidget.find('.refresh-icon-animated').fadeIn();

		setTimeout(function() {
			$activeSpinIcon.fadeOut();
		},500);


		$activeWidget = '';			

		return false;
	});


	// Remove Widget
	$('.widget-remove-option').click(function()	{

		$activeWidget = $(this).parent().parent().parent();

		$('#deleteWidgetConfirm').popup('show');

		return false;

	});

	$('.remove-widget-btn').click(function()	{
		$('#deleteWidgetConfirm').popup('hide');
		$activeWidget.fadeOut();

		$activeWidget = '';

		return false;
	});

	//Scroll to Top
	$(".scroll-to-top").click(function()	{
		$("html, body").animate({ scrollTop: 0 }, 600);
		 return false;
	});

	// Popover
    $("[data-toggle=popover]").popover();
	
	// Tooltip
    $("[data-toggle=tooltip]").tooltip();
	$("[rel=tooltip]").tooltip();
});


$(window).load(function() {
	$('body').removeClass('overflow-hidden');

	//Enable animation
	$('.wrapper').removeClass('preload');

});

// Toggle Scroll to Top button
$(window).scroll(function(){
		
	 var position = $(window).scrollTop();
	
	 if(position >= 200)	{
		$('.scroll-to-top').addClass('active')
	 }
	 else	{
		$('.scroll-to-top').removeClass('active')
	 }
});


$(function () {
	$('.main-menu li a').hover(
		function () {
			$('.main-menu li').removeClass('active');
			$(this).parent('li').addClass('active');
		},function () {
			
		}
	);
	
	$(".bbs-hover").hover(
		
		function () {
			$(".bbs-hover").removeClass('hover-shadow');
			$(".bbs-hover").find('.bbs-ellipsis').css('color','#f0f0f0');
			$(this).addClass('hover-shadow');
			$(this).find('.bbs-ellipsis').css('color','#188ae2');
		}
		
	);
	$(".edit-price").on('click',function (e) {
		 e.preventDefault();
	     $("#editPrice").removeAttr('disabled');
	      $("#editPrice").focus();
	})
	  
})

function addChildModule(that,num) {
	var tpl='<tr class="child-module"><td></td><td>'+num+'</td><td><div class="child-box"><input type="" name="" id="" placeholder="新版块名称" class="table-input"/></div></td><td><input type="" name="" id="" value="" class="table-input-md"/></td><td><a href=""><i class="iconfont bbs-xiayi"></i></a><a href=""><i class="iconfont bbs-xiayi1"></i></a>	</td><td><button class="t-edit" onclick="test();"><i class="iconfont bbs-edit"></i></button><button class="t-del" onclick="deleteChild(this)"><i class="iconfont bbs-delete"></i></button></td></tr>';
	
  var $parent=$(that).parent().parent().parent();
  $parent.before(tpl);
  
   $(that).parents('.first-table').find('.child-module')
  $(that).parents('.first-table').find('.t-collapse').text('[-]');
  $(that).parents('.first-table').find('.t-collapse').addClass('active');
  
  
}


function addZone(that,id) {
	var tpl='<tbody class="first-table">'+
        '<tr class="parent-module">'+
        '<td class="td-5 t-collapse">[-]</td>'+
        '<td class="td-5">1</td>'+
        ' <td>'+
        '<input type="" name="" id="" value="售前咨询" class="table-input"/>'+
        ' </td>'+
        ' <td class="td-20"><input type="" name="" id="" value="" class="table-input-md"/></td>'+
        ' <td class="td-15">'+
        ' <a href=""><i class="iconfont bbs-xiayi"></i></a>'+
        ' <a href=""><i class="iconfont bbs-xiayi1"></i></a>'+
        ' </td>'+
        ' <td class="td-15"><a href="javascript:void(0);" class="t-del iconfont bbs-delete" onclick="deleteParent(this)" title="删除"></a></td>'+
        ' </tr>'+
        ' <tr>'+
        ' <td></td>'+
        ' <td></td>'+
        ' <td colspan="4">'+
        ' <div class="creat-add">'+
        ' <a href="javascript:void(0);" onclick="addChildModule(this,255)">添加新版块</a>'+
        '</div>'+
        ' </td>'+
        ' </tr>'+
        ' </tbody>';
        
        	
  var $parent=$(that).parent().parent().parent().parent();
  $parent.before(tpl);   
}
function deleteParent(that) {
	
	swal({
  title: "你确定删除这个版块吗?",
  type: "warning",
  showCancelButton: true,
  confirmButtonColor: "#DD6B55",
  confirmButtonText: "删除!",
  cancelButtonText: "返回",
  closeOnConfirm: true,
  closeOnCancel: true
},
function(isConfirm){
  if (isConfirm) {
  
    $(that).parents('tbody').remove();
  } else {
	  
  }
});
	
	
}
function deleteChild(that) {
	
	swal({
  title: "您确定要禁止发布所有含敏感词的文字吗？",
  showCancelButton: true,
  confirmButtonColor: "#DD6B55",
  cancelButtonText: "取消",
  confirmButtonText: "确定",
  closeOnConfirm: true,
  closeOnCancel: true,
  /*allowOutsideClick:true*/
},
function(isConfirm){
  if (isConfirm) {
   $(that).parents('tr').remove();
  } else {
	  
  }
});
	
	
	
}

$('.bbs-table').on('click','.t-collapse',function () {
	$(this).toggleClass('active');
	var state=$(this).is('.active');
	 if(state){
	 	$(this).text('[-]');
	    $(this).parents('.first-table').find('.child-module').show();
	 }else{
	 	$(this).text('[+]')
	 	 $(this).parents('.first-table').find('.child-module').hide();
	 }
	 
})

	function setScreen() {
	   var sysHeight=$(window).outerHeight();
	   var minHeight=sysHeight-190;
	    $('.forum-module').css('minHeight',minHeight+'px');
	
      }
	
	setScreen();

    function showDiv(id) {
    	var elem='#'+id;
         $(elem).siblings('.checkModule').addClass('hidden');
    	$(elem).removeClass('hidden').addClass('show');
    }  



