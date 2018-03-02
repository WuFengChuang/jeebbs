
$(function(){if(typeof scope=='undefined'){scope={};}
var navMoreConfig={oNavBox:$('#j_navShowBox'),oNavBtn:$('#j_navShowBtn')}
function navMore(options){var oNavBox=options.oNavBox,oNavBtn=options.oNavBtn;oNavBtn.on('click tap',function(){var flag=oNavBtn[0].className;if(flag=='down'){oNavBox.removeClass('hide').addClass('show'),oNavBtn.html('收起').addClass('up').removeClass('down');oNavBtn.data('close','up');}else{oNavBox.removeClass('show').addClass('hide'),oNavBtn.html('更多').addClass('down').removeClass('up');oNavBtn.data('close','down');}});}
var picsHeightManager={isPicDivsBlocked:false,init:function(){this.setPicsHeight();var self=this;window.addEventListener('onorientationchange'in window?'orientationchange':'resize',function(){var sf=self;setTimeout(function(){sf.setPicsHeight();},300);},false);},setPicsHeight:function(){try{var oSwipe=$('#j_imgSwipe'),oImg=oSwipe.children().eq(0).find('a').find('img');var _swiptContainerWidth=oSwipe.width();var _picsWidth=oImg.width();var _picsHeight=oImg.height();var _swiptContainerHeight=_swiptContainerWidth*_picsHeight/_picsWidth;_swiptContainerHeight=Math.floor(_swiptContainerHeight);oSwipe.css("height",_swiptContainerHeight+"px");oSwipe.find(".swipe-wrap").css("height",_swiptContainerHeight+"px");var _swipe_pics=oSwipe.find(".swipe_pic");_swipe_pics.css("height",_swiptContainerHeight+"px");if(!this.isPicDivsBlocked){this.isPicDivsBlocked=true;_swipe_pics.css("display","block");}}catch(e){}}};function imgSwipe(id){var obj=null,oSwipe=$('#'+id),oImg=oSwipe.children().eq(0).find('a').find('img'),len=oImg.length,oSpan=oSwipe.children().eq(1).find('span'),len=oImg.length,maxCount=len-2,count=1,logUrl='http://data.3g.sina.com.cn/api/logger/ls_focuspic_log.php',logType,ua=navigator.userAgent,current=0;obj=new Swipe(document.getElementById(id),{startSlide:0,auto:4000,speed:300,callback:function(index,element){oSpan.html(index+1);var currImg,wrap;if(index>current&&index>=1&&index<len-1)currImg=oImg.eq(index+1);else if(index<current&&index>=2)currImg=oImg.eq(index);else if(index==len-1&&current==0)currImg=oImg.eq(len-1);else{current=index;return;}
current=index;if(currImg.length>0)wrap=currImg.parent().parent();if(currImg.length>0&&wrap.attr('data-loaded')=='0'){var src=currImg.attr('data-src'),alt=currImg.attr('alt'),_oImg=new Image(),loaded=false,isTimeout=false,timer=null;currImg.attr('alt','');wrap.addClass('loading');_oImg.src=src;_oImg.onload=function(){wrap.removeClass('loading');currImg.attr('src',src);currImg.attr('alt',alt);wrap.attr('data-loaded','1');};_oImg.onerror=function(){var _Img=new Image();timer=setTimeout(function(){if(!loaded){isTimeout=true;wrap.removeClass('loading').addClass('timeout');currImg.attr('alt','');logType='1';$.ajax({type:'POST',async:false,url:logUrl+'?type='+logType+'&index='+index+'&src='+src+'&timeout=10&ua='+encodeURIComponent(ua),dataType:'jsonp',success:function(data){},error:function(xhr,type){}});}},10000);var t=new Date().getTime();_Img.src=src+'?t='+t;_Img.onload=function(){loaded=true;clearTimeout(timer);wrap.removeClass('loading');currImg.attr('src',src);currImg.attr('alt',alt);wrap.attr('data-loaded','1');};_Img.onerror=function(){if(!isTimeout){loaded=true;clearTimeout(timer);wrap.removeClass('loading').addClass('error');currImg.attr('alt','');logType='0';$.ajax({type:'POST',async:false,url:logUrl+'?type='+logType+'&index='+index+'&src='+src+'&timeout=10&ua='+encodeURIComponent(ua),dataType:'jsonp',success:function(data){},error:function(xhr,type){}});}};};}}});}
var fastModeConfig={activeRss:$('#j_activeRss')}
function fastMode(options){var oSpan=options.activeRss;if(oSpan.length>0){oSpan[0].addEventListener('touchstart',function(ev){function fnEnd(ev){clickSwitch();document.removeEventListener('touchend',fnEnd,false);}
document.addEventListener('touchend',fnEnd,false);},false);function clickSwitch(){if(oSpan.hasClass('ar_wrapDis')){oSpan.removeClass('ar_wrapDis');oSpan.find('b').html('无图');oSpan.addClass('ar_wrap');$('dt').addClass('hide');$('dt').next().find('h3').removeClass('pic_t_44');$('dl').find('h4').addClass('hide');setCookie('nopicState','true',10);}
else{oSpan.removeClass('ar_wrap');oSpan.find('b').html('有图');oSpan.addClass('ar_wrapDis');$('dt').removeClass('hide');if(columnConfig.iscolumn){$('dt').next().find('h3').addClass('pic_t_44');}
$('dl').find('h4').removeClass('hide');removeCookie('nopicState');}}}}
function tap()
{if($(this).find('.more_btn_next').length>0){location.href=$(this).find('.more_btn_next').find('a').attr('href');return;}
var that=$(this).find('.more_btn_down')[0];$(that).parent().parent().find('.carditems').find('a').removeClass('hide');$(that).parent().parent().find('.carditems').find('a').eq(2).find('dl').css('borderBottom','1px solid #ececec;');var _cardTitle="";try{var _cardTitleSpan=$(this).parent().find(".j_cardTitle");_cardTitle=_cardTitleSpan.text();_cardTitle=$.trim(_cardTitle);}catch(e){}
$(that).html('进入'+_cardTitle);$(that).addClass('more_btn_next');$(that).removeClass('more_btn_down');$(this).off('click',tap);$(this).on('click',function(){location.href=$(that).data('href');})
$('.card_more_list').on('click',function(){});}
$('.card_more_list').on('click',tap);var navScrollConfig={oComment_nav1:$('#j_comment_nav1'),oComment_nav2:$('#j_comment_nav2'),oNavShowBox:$('#j_navShowBox')}
function navScroll(options){var oComment_nav1=options.oComment_nav1,oComment_nav2=options.oComment_nav2,oNavShowBox=options.oNavShowBox,Pos_t=getPos(oComment_nav1[0]).top+oComment_nav1.height(),nav_timer=null,dTimer=null,bodyTop;function navScrollTop(){bodyTop=document.body.scrollTop;changeclass(bodyTop);};addOnscroll(navScrollTop);function changeclass(top){if(top>=document.documentElement.clientHeight){oComment_nav1.addClass('fixednav');oComment_nav2.removeClass('hide');navMoreConfig.oNavBox.removeClass('show').addClass('hide');if(navMoreConfig.oNavBtn.length>0){navMoreConfig.oNavBtn.html('更多').addClass('down').removeClass('up');}}
else{oComment_nav1.removeClass('fixednav');oComment_nav2.addClass('hide');if(navMoreConfig.oNavBtn.data('close')!='down'){navMoreConfig.oNavBox.removeClass('hide').addClass('show');if(navMoreConfig.oNavBtn.length>0){navMoreConfig.oNavBtn.html('收起').addClass('up').removeClass('down');}}}}
function getPos(obj)
{var l=0;var t=0;while(obj)
{l+=obj.offsetLeft;t+=obj.offsetTop;obj=obj.offsetParent;};return{left:l,top:t}};}
var scrollToTopConfig={oTop:$('#back_to_top'),oComment_nav1:$('#j_comment_nav1'),oComment_nav2:$('#j_comment_nav2'),oBackTop:$('.j_backTop ')}
function scrollToTop(options){var toggleTimer=null,oTop=options.oTop;oComment_nav1=options.oComment_nav1,oComment_nav2=options.oComment_nav2,oBackTop=options.oBackTop;if(oTop.length>0){var sTop=0,rTop=0,pageHeight=document.documentElement.clientHeight;addOnscroll(function(){sTop=document.body.scrollTop||document.documentElement.scrollTop;var winHeight=$('body').height();var pHeight=winHeight-sTop;if(sTop<winHeight/2){oTop.hide();}else{oTop.show();}
rTop=sTop;});oTop.click(function(){oComment_nav1.removeClass('fixednav');oComment_nav2.addClass('hide');document.body.scrollTop=document.documentElement.scrollTop=0;});oBackTop.bind('click',function(){oComment_nav1.removeClass('fixednav');oComment_nav2.addClass('hide');});}}
function setCookie(name,value,iDay)
{var oDate=new Date();oDate.setDate(oDate.getDate()+iDay);document.cookie=name+'='+value+';expires='+oDate;}
function removeCookie(name)
{setCookie(name,'',-1);}
function addOnscroll(fn){if(typeof window.onscroll=='function'){var tempFn=window.onscroll;window.onscroll=function(){tempFn();fn();}}else{window.onscroll=function(){fn();}}}
function imgDelay(){var area=$('.j_imgdelay'),areaLen=area.length,loadCount=0;if(areaLen==0){return false;}
var viewHeight=document.documentElement.clientHeight;viewHeight=viewHeight*2;function loadingImg(){if(loadCount>=areaLen){return false;}
for(var i=0;i<areaLen;i++){var currObj=area.eq(i);if(currObj.data('imgdelay')===1){continue;}
var scrollY=document.body.scrollTop||document.documentElement.scrollTop,maxHeight=viewHeight+scrollY;if(currObj.offset().top<maxHeight){var oImg=currObj.find('img'),imgLen=oImg.length;for(var j=0;j<imgLen;j++){var currImg=oImg.eq(j);currImg.attr('src',currImg.data('src'));}
currObj.data('imgdelay','1');loadCount=loadCount+1;}}}
var timer;addOnscroll(function(){if(timer){clearTimeout(timer);timer=null;}
timer=setTimeout(loadingImg,200);});}
var channelConfig={},ua=window.navigator.userAgent.toLowerCase(),pf=window.navigator.platform.toLowerCase(),oLoadBtn=$('#j_loading_btn'),oWrap=$('#j_items_list'),oLoading=$('#j_loading_bar');channelConfig.loadMoreApplyURL=scope.load_api,channelConfig.page=2,channelConfig.load_num=scope.load_num,channelConfig.ch=scope.channel_id,channelConfig.col=scope.channel_id,channelConfig.show_num=scope.page_size;var columnConfig={};columnConfig.loadMoreApplyURL=scope.load_api,columnConfig.page=2,columnConfig.show_num=scope.show_num,columnConfig.load_num=scope.load_num,columnConfig.iscolumn=scope.iscolumn||'';var pullLoaderConfig={};pullLoaderConfig.loading=0;pullLoaderConfig.isload=0;pullLoaderConfig.oPullLoader=$('#j_pullLoader');function pullLoader(){window.addEventListener('scroll',addcomment,false);}
function addcomment(ev){if(pullLoaderConfig.loading==0){var sTop=document.body.scrollTop||document.documentElement.scrollTop,dHeight=$(document).height(),cHeight=document.documentElement.clientHeight;if(sTop+cHeight>=dHeight-cHeight){pullLoaderConfig.loading=1;pullLoaderConfig.oPullLoader.removeClass('hide');if(!pullLoaderConfig.isload){setTimeout(function(){loadMoreApply();},100)
pullLoaderConfig.isload=1;}
else{loadMoreApply();}
if(window.suda){var sudaName=pullLoaderConfig.oPullLoader.data('sudaclick');if(sudaName){var clickInfo={'type':'feedloadMore','name':sudaName,'title':'feed流上拉加载统计','index':0}}
if(window.suds_count){window.suds_count(clickInfo);}};}}}
function loadMoreApply(){if(columnConfig.iscolumn){var url=columnConfig.loadMoreApplyURL,page=columnConfig.page,show_num=columnConfig.show_num;if($('.star_nav').length>0){var type=$('.star_nav').data('type');var col=$('#j_'+type+'_list').data('cid');var level=$('#j_'+type+'_list').data('level');}
else{var col=$('#j_items_list').find('.j_itemscard').last().data('cid');var level=$('#j_items_list').find('.j_itemscard').last().data('level');}
var data={col:col,level:level,show_num:columnConfig.show_num,page:columnConfig.page,act:'more',jsoncallback:'callbackFunction'}}
else{var url=channelConfig.loadMoreApplyURL,page=channelConfig.page,ch=channelConfig.ch,col=channelConfig.col,show_num=channelConfig.show_num;var data={ch:ch,col:col,show_num:show_num,page:page,act:'more',jsoncallback:'callbackFunction'}}
if(!!url==false){return false;}
if(url.indexOf('?')==-1){url=url+'?';}
ajax(url,data,success,error,complate);function error(){}
function success(){}
function complate(){}}
function ajax(url,data,success,error,complate){$.ajax({url:url,data:data,async:false,type:'GET',dataType:'jsonp',success:function(data){success&&success();complate&&complate();},error:function(xhr,type){error&&error();complate&&complate();}});}
window.callbackFunction=function(rs){if(columnConfig.iscolumn){if(rs.result.status.code==0||rs.result.status.code=='0'){if($('.star_nav').length>0){var type=$('.star_nav').data('type');var oWrap=$('#j_'+type+'_list');}
else{var oWrap=$('#j_items_list').find('.carditems').last();}
var data=rs.result.data.list;var total=rs.result.data.total;var len=data.length,arr=[];var repeat;var dataCid=[];var aA=oWrap.find('a');aA.each(function(){dataCid.push($(this).data('cid'));});for(i=0;i<len;i++){repeat=false;for(j=0;j<dataCid.length;j++){if(data[i]._id==dataCid[j]){repeat=true;}}
if(repeat){continue;}
if(data[i].type=='normal'){arr.push('<a data-cid="'+data[i]._id+'" href="'+data[i].URL+'">');arr.push('<dl data-timestamp="'+data[i].cTime+'" class="carditems_list" data-imgdelay="1"> ');if(data[i].allPics.total&&data[i].allPics.pics[0]&&!scope.nopic_state){dt_push_column(data);}
if($('#j_activeRss').hasClass('ar_wrap'))
{dd_push_column(data);}
else{dd_push_column2(data);}
arr.push('</dl></a>');function dt_push_column(){if($('#j_activeRss').hasClass('ar_wrap'))
{arr.push('<dt class="carditems_list_dt hide">');}
else{arr.push('<dt class="carditems_list_dt">');}
arr.push('<img src="'+data[i].allPics.pics[0].imgurl+'" alt="">');if(data[i].mediaTypes=="video"){arr.push('<span class="video_tips">&nbsp</span>');}
arr.push('</dt>');}
function dd_push_column(){arr.push('<dd class="carditems_list_dd">');if(data[i].summary&&scope.needsummary){arr.push('<h3 class="carditems_list_h3">'+data[i].stitle+'</h3> ');arr.push('<h4 class="carditems_list_h4">'+data[i].summary+'</h4> ');}
else{arr.push('<h3 class="carditems_list_h3">'+data[i].title+'</h3> ');}
arr.push('<p class="carditems_list_op">');arr.push('<span class="op_ico time_num fl">'+data[i].cTime+'</span>');if(data[i].URL.indexOf('weibo.com')!=-1||data[i].URL.indexOf('weibo.cn')!=-1){arr.push('<span class="op_sole_tips fr">微博</span>');}
else if(data[i].comment==''||data[i].comment=='0'||data[i].comment==0){}
else{arr.push('<span class="op_ico num_ico fr">'+data[i].comment+'</span>');}
arr.push('</p>');arr.push('</dd>');}
function dd_push_column2(){arr.push('<dd class="carditems_list_dd">');if(data[i].summary&&scope.needsummary){arr.push('<h3 class="carditems_list_h3">'+data[i].stitle+'</h3> ');arr.push('<h4 class="carditems_list_h4">'+data[i].summary+'</h4> ');}
else{if(data[i].allPics.total&&!scope.nopic_state){arr.push('<h3 class="carditems_list_h3 pic_t_44">'+data[i].title+'</h3> ');}
else{arr.push('<h3 class="carditems_list_h3">'+data[i].title+'</h3> ');}}
arr.push('<p class="carditems_list_op">');arr.push('<span class="op_ico time_num fl">'+data[i].cTime+'</span>');if(data[i].URL.indexOf('weibo.com')!=-1||data[i].URL.indexOf('weibo.cn')!=-1){arr.push('<span class="op_sole_tips fr">微博</span>');}
else if(data[i].comment==''||data[i].comment=='0'||data[i].comment==0){}
else{arr.push('<span class="op_ico num_ico fr">'+data[i].comment+'</span>');}
arr.push('</p>');arr.push('</dd>');}}
else if(data[i].type=='pics'){arr.push('<a data-cid="'+data[i]._id+'" href="'+data[i].URL+'">');arr.push('<dl data-timestamp="'+data[i].cTime+'" class="carditems_list j_imgdelay" data-imgdelay="1">');arr.push('<dd class="carditems_list_dd">');arr.push('<h3 class="carditems_list_h3">'+data[i].stitle+'</h3>');if(!scope.nopic_state){arr.push('<ul class="carditems_list_pics">');arr.push('<li><span>');if(data[i].allPics.pics&&data[i].allPics.pics[0]){arr.push('<img src="'+data[i].allPics.pics[0].imgurl+'" data-src="'+data[i].allPics.pics[0].imgurl+'" alt="'+data[i].allPics.pics[0].note+'">');arr.push('</span></li>');arr.push('<li><span>');arr.push('<img src="'+data[i].allPics.pics[1].imgurl+'" data-src="'+data[i].allPics.pics[1].imgurl+'" alt="'+data[i].allPics.pics[1].note+'">');arr.push('</span></li>');arr.push('<li><span>');arr.push('<img src="'+data[i].allPics.pics[2].imgurl+'" data-src="'+data[i].allPics.pics[2].imgurl+'" alt="'+data[i].allPics.pics[2].note+'">');}
arr.push('</span></li>');arr.push('</ul>');}
arr.push('<p class="carditems_list_op">');arr.push('<span class="op_ico time_num fl">'+data[i].cTime+'</span>');if(data[i].URL.indexOf('weibo.com')!=-1||data[i].URL.indexOf('weibo.cn')!=-1){arr.push('<span class="op_sole_tips fr">微博</span>');}
else if(data[i].comment==''||data[i].comment=='0'||data[i].comment==0){}
else{arr.push('<span class="op_ico num_ico fr">'+data[i].comment+'</span>');}
arr.push('<span class="op_ico pics_ico fr">'+data[i].allPics.total+'</span>');arr.push('</p>');arr.push('</dd>');arr.push('</dl>');arr.push('</a>');}}
oWrap.append(arr.join(''));if(total<columnConfig.page*columnConfig.show_num||columnConfig.page>columnConfig.load_num){window.removeEventListener('scroll',addcomment,false);}
else{oWrap.data('show','0');}
columnConfig.page++;}else{alert(rs.message);}}
else{if(rs.status==1||rs.status=='1'){var count=rs.count;var data=rs.data;var len=data.length,arr=[];var aWrap=$('#j_items_list');if($('#j_oWrap').length==0){aWrap.append('<div class="carditems" id="j_oWrap">');}
var oWrap=$('#j_oWrap');var repeat;var dataCid=[];var aA=$('#j_items_list a');aA.each(function(){dataCid.push($(this).data('cid'));});for(i=0;i<len;i++){repeat=false;for(j=0;j<dataCid.length;j++){if(data[i].comment_id==dataCid[j]){repeat=true;}}
if(repeat){continue;}
if(data[i].type=='normal'){arr.push('<a data-cid="'+data[i].comment_id+'" href="'+data[i].link+'">');arr.push('<dl data-timestamp="'+data[i].timestamp+'" class="carditems_list" data-imgdelay="1"> ');if(data[i].img){dt_push(arr,data[i]);}
if($('#j_activeRss').hasClass('ar_wrap'))
{dd_push2(arr,data[i]);}
else{dd_push(arr,data[i]);}
arr.push('</dl></a>');}
else if(data[i].type=='pics'){pushPic(arr,data[i]);}}
oWrap.append(arr.join(''));channelConfig.page++;if(channelConfig.page>=channelConfig.load_num||channelConfig.page>=count){window.removeEventListener('scroll',addcomment,false);}
else{}}else{alert(rs.message);}}
pullLoaderConfig.oPullLoader.addClass('hide');pullLoaderConfig.loading=0;}
var setColumnConfig={aLi:$('#j_nav_main li'),oStar_nav:$('#j_star_nav'),oLoadBtn:$('#j_loading_btn')}
function setColumn(options){var oStar_nav=options.oStar_nav,aLi=options.aLi;aLi.each(function(){if($(this).hasClass('on')){oStar_nav.data('type',$(this).attr('id'));}})
$('#j_nav_main li').on('click',tapSwitch)
function tapSwitch(){if($(this).hasClass('on')){return;}
var aBox=$(this).parent().parent().parent().find('.carditems');aBox.each(function(){$(this).addClass('hide');})
aBox.eq($(this).index()).removeClass('hide');$(this).parent().children().removeClass('on');$(this).addClass('on');oStar_nav.data('type',$(this).attr('id'));if($('#j_'+$(this).attr('id')+'_list').data('show')){window.addEventListener('scroll',addcomment,false);}
else{window.removeEventListener('scroll',addcomment,false);}}}
var downConfig={};downConfig.data=[];downConfig.len=5;downConfig.count=0;downConfig.loaded=false;downConfig.url=scope.tuijian_api||'';function check(){if(downConfig.loaded){var oTips=$('#tips');oTips.find('span').html('暂时没有新的新闻');showtipes();if(window.suda){var sudaName='feed_dragDown_5';if(sudaName){var clickInfo={'type':'feeddragDown','name':sudaName,'title':'feed流下拉加载统计','index':0}}
if(window.suds_count){window.suds_count(clickInfo);}};return false;}
else{downConfig.count++;if(downConfig.count>1)
{if(window.suda){var sudaName='feed_dragDown_'+downConfig.count;if(sudaName){var clickInfo={'type':'feeddragDown','name':sudaName,'title':'feed流下拉加载统计','index':0}}
if(window.suds_count){window.suds_count(clickInfo);}};var oWrap=$('#j_dragDown_list');var oTips=$('#tips');var data=downConfig.data;var arr=[];if(downConfig.data.length<(downConfig.len*downConfig.count)){var len=downConfig.data.length;}else{var len=downConfig.len*downConfig.count;}
for(i=downConfig.len*(downConfig.count-1);i<len;i++){if(data[i].type=='normal'){arr.push('<a data-cid="'+data[i].comment_id+'" href="'+data[i].link+'">');arr.push('<dl class="carditems_list" data-imgdelay="1">');if(data[i].img){dt_push(arr,data[i]);}
if($('#j_activeRss').hasClass('ar_wrap'))
{dd_push2(arr,data[i]);}
else{dd_push(arr,data[i]);}
arr.push('</dl></a>');}
else if(data[i].type=='pics'){pushPic(arr,data[i]);}}
oWrap.prepend(arr.join(''));showtipes();if(downConfig.count==4){downConfig.loaded=true;}
return false;}
return true;}}
function downRefresh(){if($('#dragdown_wrapper').length>0){var iscolumn=scope.iscolumn||false;var url=downConfig.url;if(!!url==false){return false;}
if(url.indexOf('?')==-1){url=url+'?';}
if(!iscolumn){dragDown({type:"jsonp",beforeSend:check,downWrapId:"dragdown_wrapper",downUrl:url+'&ch='+channelConfig.ch+'&show_num='+channelConfig.show_num+'&act=more&jsoncallback=callbackDragDown'});}}}
window.callbackDragDown=function(rs){if(rs.status==1||rs.status=='1'){if($('#j_dragDown_list').length==0){$('<div id="j_dragDown_list" class="carditems" style="position:relative;" data-sudaclick="newslist">').insertAfter($('#j_imgSwipe'));}
var oWrap=$('#j_dragDown_list');var count=rs.count;var data=rs.data;var len=data.length,arr=[];downConfig.data=data;for(i=0;i<downConfig.len;i++){if(data[i].type=='normal'){arr.push('<a data-cid="'+data[i].comment_id+'" href="'+data[i].link+'">');arr.push('<dl class="carditems_list" data-imgdelay="1">');if(data[i].img){dt_push(arr,data[i]);}
if($('#j_activeRss').hasClass('ar_wrap'))
{dd_push2(arr,data[i]);}
else{dd_push(arr,data[i]);}
arr.push('</dl></a>');}
else if(data[i].type=='pics'){pushPic(arr,data[i]);}}
oWrap.prepend(arr.join(''));if($('#tips').length==0){var bottom=navScrollConfig.oComment_nav1[0].getBoundingClientRect().bottom;$('body').append('<div class="tips hide" id="tips" style="position: absolute;z-index: 10;width: 100%;opacity: 0;border: none;background-color: rgba(50,160,230,0.9);line-height: 28px;text-align: center;top:'+bottom+'px;"><span style="color: #fff;font-size: 14px;">刚刚为您推荐了5条新闻</span></div>');}
showtipes();}else{alert(rs.message);}
if(window.suda){var sudaName='feed_dragDown_1';if(sudaName){var clickInfo={'type':'feeddragDown','name':sudaName,'title':'feed流下拉加载统计','index':0}}
if(window.suds_count){window.suds_count(clickInfo);}};}
function showtipes(){$('#j_dragDown_list').prepend($('.advertise'));var bottom=navScrollConfig.oComment_nav1[0].getBoundingClientRect().bottom;var oTips=$('#tips');oTips.css('top',bottom);oTips.removeClass('hide');oTips.animate({opacity:'1'},500);setTimeout(function(){oTips.animate({opacity:'0'},500);},1500);setTimeout(function(){oTips.addClass('hide');},2000);}
function pushPic(arr,data){arr.push('<a data-cid="'+data.comment_id+'" href="'+data.link+'">');arr.push('<dl class="carditems_list j_imgdelay" data-imgdelay="1">');arr.push('<dd class="carditems_list_dd">');arr.push('<h3 class="carditems_list_h3">'+data.wap_title+'</h3>');arr.push('<ul class="carditems_list_pics">');arr.push('<li><span>');arr.push('<img src="'+data.pics.imgs[0]+'" data-src="'+data.pics.imgs[0]+'" alt="'+data.wap_title+'">');arr.push('</span></li>');arr.push('<li><span>');arr.push('<img src="'+data.pics.imgs[1]+'" data-src="'+data.pics.imgs[1]+'" alt="'+data.wap_title+'">');arr.push('</span></li>');arr.push('<li><span>');arr.push('<img src="'+data.pics.imgs[2]+'" data-src="'+data.pics.imgs[2]+'" alt="'+data.wap_title+'">');arr.push('</span></li>');arr.push('</ul>');arr.push('<p class="carditems_list_op">');arr.push('<span class="op_ico time_num fl">'+data.date+'</span>');if(data.link.indexOf('weibo.com')!=-1||data.link.indexOf('weibo.cn')!=-1){arr.push('<span class="op_sole_tips fr">微博</span>');}
else if(data.comment==''||data.comment=='0'||data.comment==0){}
else{arr.push('<span class="op_ico num_ico fr">'+data.comment+'</span>');}
arr.push('<span class="op_ico pics_ico fr">'+data.pics.total+'</span>');arr.push('</p>');arr.push('</dd>');arr.push('</dl>');arr.push('</a>');}
function dt_push(arr,data){if($('#j_activeRss').hasClass('ar_wrap'))
{arr.push('<dt class="carditems_list_dt hide">');}
else{arr.push('<dt class="carditems_list_dt">');}
arr.push('<img src="'+data.img+'" alt="">');if(data.mediaTypes=='video'){arr.push('<span class="video_tips">&nbsp</span>');}
arr.push('</dt>');}
function dd_push(arr,data){arr.push('<dd class="carditems_list_dd">');if(data.intro&&scope.needsummary){arr.push('<h3 class="carditems_list_h3">'+data.wap_title+'</h3> ');arr.push('<h4 class="carditems_list_h4 ">'+data.intro+'</h4>');}
else{arr.push('<h3 class="carditems_list_h3 pic_t_44">'+data.wap_title+'</h3> ');}
arr.push('<p class="carditems_list_op">');arr.push('<span class="op_ico time_num fl">'+data.date+'</span>');if(data.link.indexOf('weibo.com')!=-1||data.link.indexOf('weibo.cn')!=-1){arr.push('<span class="op_sole_tips fr">微博</span>');}
else if(data.comment==''||data.comment=='0'||data.comment==0){}
else{arr.push('<span class="op_ico num_ico fr">'+data.comment+'</span>');}
if(data.isDujia==1||data.isDujia=='1'){arr.push('<span class="op_sole_tips fr">独家</span>');}
else if(data.isSubject==1||data.isSubject=='1'){arr.push('<span class="op_sub_tips fr">专题</span>');}
arr.push('</p>');arr.push('</dd>');}
function dd_push2(arr,data){arr.push('<dd class="carditems_list_dd">');if(data.intro&&scope.needsummary){arr.push('<h3 class="carditems_list_h3">'+data.wap_title+'</h3> ');arr.push('<h4 class="carditems_list_h4 hide">'+data.intro+'</h4>');}
else{arr.push('<h3 class="carditems_list_h3 pic_t_44">'+data.wap_title+'</h3> ');}
arr.push('<p class="carditems_list_op">');arr.push('<span class="op_ico time_num fl">'+data.date+'</span>');if(data.link.indexOf('weibo.com')!=-1||data.link.indexOf('weibo.cn')!=-1){arr.push('<span class="op_sole_tips fr">微博</span>');}
else if(data.comment==''||data.comment=='0'||data.comment==0){}
else{arr.push('<span class="op_ico num_ico fr">'+data.comment+'</span>');}
if(data.isDujia==1||data.isDujia=='1'){arr.push('<span class="op_sole_tips fr">独家</span>');}
else if(data.isSubject==1||data.isSubject=='1'){arr.push('<span class="op_sub_tips fr">专题</span>');}
arr.push('</p>');arr.push('</dd>');}
function SetCookie(name,value){var Days=15;var exp=new Date();exp.setTime(exp.getTime()+Days*24*60*60*1000);try{document.cookie=name+"="+escape(value)+";expires="+exp.toGMTString();}catch(e){}}
function prevent(e){e.preventDefault();return;}
function guideInit(){if($('.guideWrap').length>0){window.addEventListener("touchmove",prevent,false);if($('#dragdown_wrapper').length>0){$("#dragdown_wrapper .dragdown_loading").get(0).isLoad=true;}
$('.j_guide_btn').on('click tap',function(){$('.guideWrap').remove();window.removeEventListener("touchmove",prevent,false);if($('#dragdown_wrapper').length>0){$("#dragdown_wrapper .dragdown_loading").get(0).isLoad=false;}
SetCookie('tech_guide_close','yes');});}}
function forbidScroll(){if($('#j_imgSwipe').length>0&&$('#dragdown_wrapper').length>0){$('#j_imgSwipe')[0].addEventListener('touchmove',function(ev){var obj=ev.srcElement||ev.target;if(($(obj).parent().parent().css('WebkitTransform')!='translate(0px, 0px) translateZ(0px)'&&$(obj).parent().parent().css('WebkitTransform')!='translateX(0px)')){$("#dragdown_wrapper .dragdown_loading").get(0).isLoad=true;}},false);$(document)[0].addEventListener('touchmove',function(ev){var obj=ev.srcElement||ev.target;if(obj.tagName=="IMG"||obj.className.indexOf('swipe')!=-1){return;}
$("#dragdown_wrapper .dragdown_loading").get(0).isLoad=false;},false);$('#j_imgSwipe')[0].addEventListener('touchend',function(){$("#dragdown_wrapper .dragdown_loading").get(0).isLoad=false;},false);}}
function setCid(){if($('.star_nav_main li').length>0){$('.star_nav_main li').each(function(){if($(this).hasClass('on')){$('.star_nav').data('type',$(this).attr('id'));}})}}
setCid();function mScroll(){if($('#wrapper').length>0){var obj_box=$('#scroller');var obj=$('.j_box');var obj_box_width=obj.length*obj[0].offsetWidth;obj_box.css('width',obj_box_width);var myScroll=new IScroll('#wrapper',{eventPassthrough:true,scrollX:true,scrollY:false,preventDefault:false});}}
function initPraise(){function createScript(url){var head=document.getElementsByTagName('head')[0],script=document.createElement('script');script.src=url;script.charset='utf-8';head.appendChild(script);}
var _praiseInterAddr="http://interface.sina.cn/wap_api/wap_counter.d.html/?t=set&r=good&k=";var _key=$(".j_upraise_icon").data("praisekey");$(".j_upraise_icon").on("click tap",function(){var _commentNums=parseInt($(this).html());if(_commentNums<=0||isNaN(_commentNums)){_commentNums=0;}
if($(this).hasClass("upraiseoff")){_commentNums+=1;$(this).removeClass("upraiseon upraiseoff ucandlepraiseoff ucandlepraiseon");$(this).addClass("upraiseon");createScript(_praiseInterAddr+_key);}else if($(this).hasClass("ucandlepraiseoff")){_commentNums+=1;$(this).removeClass("upraiseon upraiseoff ucandlepraiseoff ucandlepraiseon");$(this).addClass("ucandlepraiseon");createScript(_praiseInterAddr+_key);}
$(this).html(_commentNums);});}
setTimeout(function(){navMore(navMoreConfig);if($('#j_imgSwipe').length>0&&$('.j_sax').length==0){imgSwipe('j_imgSwipe');picsHeightManager.init();}
initPraise();fastMode(fastModeConfig);navScroll(navScrollConfig)
imgDelay();scrollToTop(scrollToTopConfig);setColumn(setColumnConfig);downRefresh();pullLoader(pullLoaderConfig);guideInit();forbidScroll();mScroll();},300);});