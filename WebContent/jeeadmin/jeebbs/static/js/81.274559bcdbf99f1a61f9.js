webpackJsonp([81],{1004:function(t,e,i){function a(t){i(1284)}var s=i(99)(i(1116),i(1370),a,null,null);t.exports=s.exports},1116:function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=i(364);e.default={data:function(){return{checkAll:!1,isIndeterminate:!0,loading:!0,advertisingSpaceList:[],ids:"",sort:this.$store.state.sort,lastLoginDay:this.$store.state.lastLoginDay,groupList:[],params:{pageNo:1,pageSize:20,queryChargeMode:"",queryTitle:""},totalCount:0,currentPage:1,changePageSize:localStorage.getItem("PageSize"),disabled:!0}},methods:{getAdvertisingSpaceList:function(){var t=this,e=this.params;a.O(e).then(function(e){console.log(e),"100"==e.code?(t.advertisingSpaceList=e.body,t.totalCount=e.totalCount,t.loading=!1):(t.loading=!1,t.$message.error(e.message))}).catch(function(e){t.loading=!1,t.$message.error("数据拉取异常")})},back:function(){this.$router.push({path:"/advertisinglist"})},addAdvertisingSpaceList:function(){this.$router.push({path:"/advertisingspaceadd",query:{type:"add",id:0}})},editFriendLink:function(t){this.$router.push({path:"/advertisingspaceedit",query:{type:"edit",id:t}})},deleteAdvertisingSpace:function(t){var e=this;this.ids=t,this.$confirm("您确定要删除广告版位吗?","警告",{type:"warning"}).then(function(t){a.P({ids:e.ids}).then(function(t){e.loading=!1,"100"==t.code?(e.$message.success("删除成功"),e.getAdvertisingSpaceList()):e.$message.error(t.message)}).catch(function(t){console.log(t),e.loading=!1,e.$message.error("数据拉取异常")})}).catch(function(t){})},checkIds:function(t){for(var e=[],i=0;i<t.length;i++)e.push(t[i].id);0==e.length?(this.ids="",this.disabled=!0):(this.ids=e.join(","),this.disabled=!1)}},created:function(){this.getAdvertisingSpaceList()},watch:{$route:function(t,e){this.loading=!0,this.getAdvertisingSpaceList()}}}},1199:function(t,e,i){e=t.exports=i(976)(!1),e.push([t.i,"",""])},1284:function(t,e,i){var a=i(1199);"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);i(977)("24b3add6",a,!0)},1370:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"forum-module"},[t._m(0),t._v(" "),i("div",{staticClass:"table-top-bar clearfix"},[i("a",{staticClass:"add-Class",attrs:{href:"javascript:void(0)"},on:{click:t.addAdvertisingSpaceList}},[t._v("添加")]),t._v(" "),i("div",{staticClass:"pull-right"},[i("el-button",{on:{click:t.back}},[t._v("广告管理")])],1)]),t._v(" "),i("div",{directives:[{name:"loading",rawName:"v-loading.body",value:t.loading,expression:"loading",modifiers:{body:!0}}],staticClass:"table-responsive"},[i("form",{staticClass:"form-horizontal no-margin"},[i("el-table",{staticStyle:{width:"100%"},attrs:{data:t.advertisingSpaceList},on:{"selection-change":t.checkIds}},[i("el-table-column",{attrs:{type:"selection",align:"center"}}),t._v(" "),i("el-table-column",{attrs:{label:"id",prop:"id",align:"center",width:"50"}}),t._v(" "),i("el-table-column",{attrs:{label:"名称",prop:"name",align:"center"}}),t._v(" "),i("el-table-column",{attrs:{label:"启用",prop:"enabled",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return[e.row.enabled?i("span",[t._v("是")]):i("span",{staticClass:"red"},[t._v("否")])]}}])}),t._v(" "),i("el-table-column",{attrs:{label:"操作",align:"center",width:"200"},scopedSlots:t._u([{key:"default",fn:function(e){return[i("a",{staticClass:"t-edit iconfont bbs-edit",attrs:{href:"javascript:void(0)",title:"编辑"},on:{click:function(i){t.editFriendLink(e.row.id)}}}),t._v(" "),i("a",{staticClass:"t-del iconfont bbs-delete",attrs:{href:"javascript:void(0)",title:"删除"},on:{click:function(i){t.deleteAdvertisingSpace(e.row.id)}}})]}}])})],1)],1)]),t._v(" "),i("div",{staticClass:"table-bottom-bar"},[i("div",{staticClass:"pull-left"},[i("el-button",{attrs:{disabled:t.disabled},on:{click:function(e){t.deleteAdvertisingSpace(t.ids)}}},[t._v("批量删除")])],1),t._v(" "),i("div",{staticClass:"pull-right"})])])},staticRenderFns:[function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"forum-header"},[i("span",{staticClass:"forum-name"},[t._v("广告版位列表")])])}]}}});