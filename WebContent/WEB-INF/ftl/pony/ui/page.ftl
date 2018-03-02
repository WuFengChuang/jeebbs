<#--
表格标签：用于显示列表数据。
	value：列表数据，可以是Pagination也可以是List。
-->
<#assign n=5/>
<#assign m=3/>
<#assign b=2/>
<#macro page value listAction="v_list.do" tableForm='tableForm' >
<#if value?is_sequence><#local pageList=value/><#else><#local pageList=value.list/></#if>
<div class="table-bottom-bar">
	<div class="pull-left">
		<div class="checkbox ">
			<label>
				 <input type="checkbox"  onclick="Pn.checkbox('ids',this.checked)">全选
			</label>
		</div>
		<input type="button" class="btn btn-default" value="批量删除"/>
		<span>每页显示 <input type="text"  class="input-sm" value="${value.pageSize!}"  onfocus="this.select();" onblur="$.cookie('_cookie_page_size',this.value,{expires:3650});" onkeypress="if(event.keyCode==13){$(this).blur();return false;}"/>条,输入后按回车</span>
	</div>
	<div class="pull-right">
		<nav>
		<ul class="pagination">
			<#if !value.firstPage>
			<li class="page-next">
			<a href="#" onclick="_gotoPage('${value.prePage}');"><span class="iconfont bbs-shangyiye-copy"></span></a>
			</li>
		    </#if>
		    <#if value.totalPage gt n>
				 <#if value.pageNo lte m>
					<#list 1..n as page>
						<li <#if page==value.pageNo>class="active"<#else>class=""</#if>><a href="#"  onclick="_gotoPage('${page}');">${page}</a></li>
					</#list>
				     <li class="more"><a href="#">...</a></li>
				     <li <#if value.totalPage==value.pageNo>class="active"<#else>class=""</#if>><a href="#" onclick="_gotoPage('${value.totalPage!}');">${value.totalPage!}</a></li>
			     <#elseif value.pageNo>value.totalPage-m>
				     <li <#if 1==value.pageNo>class="active"<#else>class=""</#if>><a href="#" onclick="_gotoPage('1');">1</a></li>
				     <li class="more"><a href="#">...</a></li>
				     <#list value.totalPage-n+1..value.totalPage as page>  
				     <li  <#if page==value.pageNo>class="active"<#else>class=""</#if>><a href="#" onclick="_gotoPage('${page!}');">${page}</a></li>
				     </#list>
			     <#else>
				     <li <#if 1==value.pageNo>class="active"</#if>><a href="#" onclick="_gotoPage('1');">1</a></li>
				     <li class="more"><a href="#">...</a></li>
				     <#list value.pageNo-b..value.pageNo+b as page>
				     <li <#if page==value.pageNo>class="active"<#else>class=""</#if>><a href="#" onclick="_gotoPage('${page!}');">${page}</a></li>
				     </#list>
				     <li class="more"><a href="#">...</a></li>
				     <li <#if value.totalPage==value.pageNo>class="active"<#else>class=""</#if>><a href="#" onclick="_gotoPage('${value.totalPage!}');">${value.totalPage!}</a></li>
			     </#if>
	       <#else>
		     <#list 1..value.totalPage as page>
		     <li <#if page==value.pageNo>class="active"<#else>class=""</#if>><a href="#" onclick="_gotoPage('${page!}');">${page}</a></li>
		     </#list>
		   </#if>
		   <#if !value.lastPage>
		   <li class="page-prev">
		   <a href="#" class="" onclick="_gotoPage('${value.nextPage}');"><span class="iconfont bbs-shangyiye-copy xz"></span></a>
		   </li>	
		   </#if>		 		
 		 </ul>
		</nav>
	</div>
</div>
<script type="text/javascript">
function getTableForm() {
	return document.getElementById('${tableForm!}');
}
function _gotoPage(pageNo) {
	try{
		var tableForm = getTableForm();
		$("input[name=pageNo]").val(pageNo);
		tableForm.action="${listAction}";
		tableForm.onsubmit=null;
		tableForm.submit();
	} catch(e) {
		console.log(e);
		alert('_gotoPage(pageNo)方法出错');
	}
}
</script>
</#macro>