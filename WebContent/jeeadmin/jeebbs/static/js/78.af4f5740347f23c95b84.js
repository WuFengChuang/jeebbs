webpackJsonp([78],{1001:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"forum-module"},[a("div",{staticClass:"forum-header"},[a("span",{staticClass:"forum-name tab-name active",on:{click:t.advertisinglist}},[t._v("广告列表")]),t._v(" "),a("span",{staticClass:"forum-name tab-name ",on:{click:t.advertisingspacelist}},[t._v("广告版位列表")])]),t._v(" "),a("div",{staticClass:"table-top-bar clearfix"},[a("div",{staticClass:"query-inline-box flex-between"},[a("el-button",{attrs:{type:"warning"},on:{click:t.addAdvertisingList}},[t._v("添加广告")]),t._v(" "),a("div",{staticClass:"query-inline-box "},[a("div",{staticClass:"query-inline-group"},[a("label",{staticStyle:{"line-height":"32px"}},[t._v("展现方式：")]),t._v(" "),a("el-select",{staticClass:"w128",attrs:{placeholder:"选择付费方式"},on:{change:t.queryCtg},model:{value:t.params.queryChargeMode,callback:function(e){t.$set(t.params,"queryChargeMode",e)},expression:"params.queryChargeMode"}},[a("el-option",{attrs:{value:"  ",label:"选择付费方式"}}),t._v(" "),a("el-option",{attrs:{value:"0",label:"按周期收费"}}),t._v(" "),a("el-option",{attrs:{value:"1",label:"按点击量收费"}}),t._v(" "),a("el-option",{attrs:{value:"2",label:"按展现量收费"}})],1)],1),t._v(" "),a("div",{staticClass:"query-inline-group"},[a("el-input",{staticClass:"w128",attrs:{placeholder:"请输入关键词"},model:{value:t.params.queryTitle,callback:function(e){t.$set(t.params,"queryTitle",e)},expression:"params.queryTitle"}})],1),t._v(" "),a("div",{staticClass:"query-inline-group"},[a("el-button",{on:{click:t.queryTitle}},[t._v("查询")])],1)])],1)]),t._v(" "),a("div",{directives:[{name:"loading",rawName:"v-loading.body",value:t.loading,expression:"loading",modifiers:{body:!0}}],staticClass:"table-responsive"},[a("form",{staticClass:"form-horizontal no-margin"},[a("el-table",{staticStyle:{width:"100%"},attrs:{data:t.advertisingList},on:{"selection-change":t.checkIds}},[a("el-table-column",{attrs:{type:"selection",align:"center",width:"68"}}),t._v(" "),a("el-table-column",{attrs:{label:"id",prop:"id",align:"center",width:50}}),t._v(" "),a("el-table-column",{attrs:{label:"名称",prop:"name",align:"center"}}),t._v(" "),a("el-table-column",{attrs:{label:"版位",prop:"adspaceName",align:"center"}}),t._v(" "),a("el-table-column",{attrs:{label:"类型",prop:"category",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return["image"==e.row.category?a("span",[t._v("图片")]):"code"==e.row.category?a("span",[t._v("代码")]):"text"==e.row.category?a("span",[t._v("文字")]):"flash"==e.row.category?a("span",[t._v("flash")]):t._e()]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"收费方式",prop:"priority",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return[0==e.row.chargeMode?a("span",[t._v("按周期收费")]):t._e(),t._v(" "),1==e.row.chargeMode?a("span",[t._v("按点击量收费")]):t._e(),t._v(" "),2==e.row.chargeMode?a("span",[t._v("按展现量收费")]):t._e()]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"开始时间",prop:"startTime",align:"center"}}),t._v(" "),a("el-table-column",{attrs:{label:"结束时间",prop:"endTime",align:"center"}}),t._v(" "),a("el-table-column",{attrs:{label:"点击/展现=点击率",prop:"priority",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("span",[t._v(t._s(e.row.clickCount)+"/"+t._s(e.row.displayCount)+"="+t._s(e.row.percent)+"%")])]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"启用",prop:"enabled",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return[e.row.enabled?a("span",[t._v("是")]):a("span",{staticClass:"red"},[t._v("否")])]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"操作",align:"center",width:200},scopedSlots:t._u([{key:"default",fn:function(e){return[a("a",{staticClass:"t-edit iconfont bbs-edit",attrs:{href:"javascript:void(0)",title:"编辑"},on:{click:function(a){t.editFriendLink(e.row.id)}}}),t._v(" "),a("a",{staticClass:"t-del iconfont bbs-delete",attrs:{href:"javascript:void(0)",title:"删除"},on:{click:function(a){t.deleteAdvertising(e.row.id)}}})]}}])})],1)],1)]),t._v(" "),a("div",{staticClass:"table-bottom-bar"},[a("div",{staticClass:"pull-left"},[a("el-button",{attrs:{disabled:t.disabled},on:{click:function(e){t.deleteAdvertising(t.ids)}}},[t._v("批量删除")]),t._v(" "),a("span",{staticClass:"ml-48"},[t._v("每页显示\n                 "),a("el-input",{staticClass:"input-sm",attrs:{type:"number",min:"1",max:"50"},on:{blur:t.changeSize},model:{value:t.changePageSize,callback:function(e){t.changePageSize=e},expression:"changePageSize"}}),t._v("条,输入后按回车")],1)],1),t._v(" "),a("div",{staticClass:"pull-right"},[a("el-pagination",{attrs:{layout:"total,prev, pager, next",total:t.totalCount,"page-size":t.params.pageSize,"current-page":t.currentPage},on:{"update:currentPage":function(e){t.currentPage=e},"current-change":t.getPage,"size-change":t.getSize}})],1)])])},staticRenderFns:[]}},598:function(t,e,a){function i(t){a(914)}var s=a(66)(a(692),a(1001),i,null,null);t.exports=s.exports},692:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var i,s=a(229),n=a.n(s),r=a(228);e.default={data:function(){return{checkAll:!1,isIndeterminate:!0,loading:!0,advertisingSpaceList:[],advertisingList:[],ids:"",sort:this.$store.state.sort,lastLoginDay:this.$store.state.lastLoginDay,groupList:[],params:{pageNo:1,pageSize:20,queryChargeMode:"",queryTitle:""},totalCount:0,currentPage:1,changePageSize:localStorage.getItem("PageSize"),disabled:!0}},methods:(i={getAdvertisingList:function(){}},n()(i,"getAdvertisingList",function(){var t=this,e=this.params;r.U(e).then(function(e){"100"==e.code?(t.advertisingList=e.body,t.totalCount=e.totalCount,t.loading=!1):(t.loading=!1,t.$message.error(e.message))}).catch(function(e){t.loading=!1,t.$message.error("网络异常")})}),n()(i,"queryCtg",function(t){this.loading=!0,this.getAdvertisingList()}),n()(i,"queryTitle",function(t){this.loading=!0,this.getAdvertisingList()}),n()(i,"getAdvertisingSpaceList",function(){this.$router.push({path:"/advertisingspacelist"})}),n()(i,"addAdvertisingList",function(){this.$router.push({path:"/advertisingadd",query:{type:"add",id:0}})}),n()(i,"advertisinglist",function(){this.$router.push({path:"/advertisinglist"})}),n()(i,"advertisingspacelist",function(){this.$router.push({path:"/advertisingspacelist"})}),n()(i,"editFriendLink",function(t){this.$router.push({path:"/advertisingedit",query:{type:"edit",id:t}})}),n()(i,"deleteAdvertising",function(t){var e=this;this.ids=t,this.$confirm("您确定要删除广告吗?","警告",{type:"warning"}).then(function(t){r.V({ids:e.ids}).then(function(t){e.loading=!1,"100"==t.code?(e.$message.success("删除成功"),e.getAdvertisingList()):e.$message.error(t.message)}).catch(function(t){e.loading=!1,e.$message.error("网络异常")})}).catch(function(t){})}),n()(i,"getPage",function(t){this.loading=!0,this.params.pageNo=t,this.getAdvertisingList()}),n()(i,"getSize",function(t){this.loading=!0,this.params.pageNo=t,this.getAdvertisingList()}),n()(i,"changeSize",function(t){var e=t.target.value;e<1?(localStorage.setItem("PageSize",20),e=20):localStorage.setItem("PageSize",e),this.loading=!0,this.params.pageSize=parseInt(e),this.params.pageNo=1,this.currentPage=1,this.getAdvertisingList()}),n()(i,"checkIds",function(t){for(var e=[],a=0;a<t.length;a++)e.push(t[a].id);0==e.length?(this.ids="",this.disabled=!0):(this.ids=e.join(","),this.disabled=!1)}),i),created:function(){var t=localStorage.getItem("PageSize");null!=t?this.params.pageSize=parseInt(t):this.changePageSize=20,this.getAdvertisingList()},watch:{$route:function(t,e){this.loading=!0,this.getAdvertisingList()}}}},828:function(t,e,a){e=t.exports=a(570)(!1),e.push([t.i,"",""])},914:function(t,e,a){var i=a(828);"string"==typeof i&&(i=[[t.i,i,""]]),i.locals&&(t.exports=i.locals);a(571)("3e37b922",i,!0)}});