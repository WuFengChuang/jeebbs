webpackJsonp([75],{1012:function(t,e,a){function s(t){a(1349)}var i=a(99)(a(1124),a(1435),s,"data-v-e07a6d86",null);t.exports=i.exports},1124:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var s=a(364);e.default={data:function(){return{loading:!0,liveList:[],ids:"",sort:[{label:"待审核",value:0},{label:"未开始",value:1},{label:"进行中",value:2},{label:"已结束",value:3},{label:"已拒绝",value:4},{label:"已关闭",value:5}],groupList:[],params:{pageNo:1,pageSize:20,qStatus:2,qTitle:"",qUsername:"",qOrderBy:""},totalCount:0,currentPage:1,changePageSize:localStorage.getItem("PageSize"),disabled:!0}},methods:{reject:function(t){var e=this;this.ids=t,this.$confirm("您确定要拒绝吗?","警告",{type:"warning"}).then(function(t){s.n({ids:e.ids,reason:"管理员后台拒绝"}).then(function(t){e.loading=!1,"100"==t.code?(e.$message.success("拒绝成功"),e.params.qStatus=0,e.getLiveList()):e.$message.error(t.message)}).catch(function(t){e.loading=!1,e.$message.error("退回异常")})}).catch(function(t){})},check:function(t){var e=this;this.ids=t,this.$confirm("您确定要审核通过吗?","警告",{type:"warning"}).then(function(t){s.o({ids:e.ids}).then(function(t){e.loading=!1,"100"==t.code?(e.$message.success("审核成功"),e.params.qStatus=0,e.getLiveList()):e.$message.error(t.message)}).catch(function(t){e.loading=!1,e.$message.error("审核异常")})}).catch(function(t){})},stopLive:function(t){var e=this;this.ids=t,this.$confirm("您确定要关闭直播吗?","警告",{type:"warning"}).then(function(t){s.p({ids:e.ids}).then(function(t){e.params.qStatus=2,e.loading=!1,"100"==t.code?(e.$message.success("关闭成功"),e.getLiveList()):e.$message.error(t.message)}).catch(function(t){e.loading=!1,e.$message.error("关闭异常")})}).catch(function(t){})},startLive:function(t){var e=this;this.ids=t,this.$confirm("您确定要恢复直播吗?","警告",{type:"warning"}).then(function(t){s.q({ids:e.ids}).then(function(t){e.params.qStatus=5,e.loading=!1,"100"==t.code?(e.$message.success("恢复成功"),e.getLiveList()):e.$message.error(t.message)}).catch(function(t){e.loading=!1,e.$message.error("恢复异常")})}).catch(function(t){})},getLiveList:function(){var t=this,e=this.params;s.r(e).then(function(e){console.log(e),"100"==e.code?(t.liveList=e.body,t.totalCount=e.totalCount,t.loading=!1):(t.loading=!1,t.$message.error(e.message))}).catch(function(e){t.loading=!1,t.$message.error("数据拉取异常")})},queryCtg:function(t){this.loading=!0,this.getLiveList()},liveConfig:function(){this.$router.push({path:"/liveconfig",query:{noceStr:Math.round(10*Math.random())}})},hostsLink:function(){this.$router.push({path:"/hostlist",query:{noceStr:Math.round(10*Math.random())}})},liveLink:function(){this.$router.push({path:"/livelist",query:{noceStr:Math.round(10*Math.random())}})},getPage:function(t){console.log(t),this.loading=!0,this.params.pageNo=t,this.getLiveList()},getSize:function(t){this.loading=!0,this.params.pageNo=t,this.getLiveList()},changeSize:function(t){var e=t.target.value;e<1?(localStorage.setItem("PageSize",20),e=20):localStorage.setItem("PageSize",e),this.loading=!0,this.params.pageSize=parseInt(e),this.params.pageNo=1,this.currentPage=1,this.getLiveList()},checkIds:function(t){for(var e=[],a=0;a<t.length;a++)e.push(t[a].id);0==e.length?(this.ids="",this.disabled=!0):(this.ids=e.join(","),this.disabled=!1)}},created:function(){console.log(this.$route.query),1==this.$route.query.index&&(this.params.qStatus=0);var t=localStorage.getItem("PageSize");null!=t?this.params.pageSize=parseInt(t):this.changePageSize=20,this.getLiveList()},watch:{$route:function(t,e){this.loading=!0,this.getLiveList()}}}},1264:function(t,e,a){e=t.exports=a(976)(!1),e.push([t.i,".forum-name[data-v-e07a6d86]{cursor:pointer}",""])},1349:function(t,e,a){var s=a(1264);"string"==typeof s&&(s=[[t.i,s,""]]),s.locals&&(t.exports=s.locals);a(977)("1a35b420",s,!0)},1435:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"forum-module"},[a("div",{staticClass:"forum-header"},[a("span",{staticClass:"forum-name",on:{click:t.liveLink}},[t._v("直播管理")]),t._v(" "),a("span",{staticClass:"forum-name",on:{click:t.hostsLink}},[t._v("主播管理")])]),t._v(" "),a("div",{staticClass:"table-top-bar clearfix"},[a("el-select",{on:{change:t.queryCtg},model:{value:t.params.qStatus,callback:function(e){t.$set(t.params,"qStatus",e)},expression:"params.qStatus"}},t._l(t.sort,function(t,e){return a("el-option",{key:t.value,attrs:{label:t.label,value:t.value}})})),t._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:t.liveConfig}},[t._v("直播配置")])],1),t._v(" "),a("div",{directives:[{name:"loading",rawName:"v-loading.body",value:t.loading,expression:"loading",modifiers:{body:!0}}],staticClass:"table-responsive"},[a("form",{staticClass:"form-horizontal no-margin"},[a("el-table",{staticStyle:{width:"100%"},attrs:{data:t.liveList},on:{"selection-change":t.checkIds}},[a("el-table-column",{attrs:{type:"selection",align:"center"}}),t._v(" "),a("el-table-column",{attrs:{label:"id",prop:"id",align:"center",width:50}}),t._v(" "),a("el-table-column",{attrs:{label:"标题",prop:"title",align:"center"}}),t._v(" "),a("el-table-column",{attrs:{label:"主播",prop:"realname",align:"center"}}),t._v(" "),a("el-table-column",{attrs:{label:"门票价格",prop:"beginPrice",align:"center"}}),t._v(" "),a("el-table-column",{attrs:{label:"开始时间",prop:"beginTime",align:"center"}}),t._v(" "),3==t.params.qStatus?a("el-table-column",{attrs:{label:"结束时间",prop:"endTime",align:"center"}}):t._e(),t._v(" "),2==t.params.qStatus?a("el-table-column",{attrs:{label:"在线人数",prop:"inliveUserNum",align:"center"}}):t._e(),t._v(" "),3==t.params.qStatus||2==t.params.qStatus?a("el-table-column",{attrs:{label:"直播数",prop:"giftNum",align:"center"}}):t._e(),t._v(" "),1!=t.params.qStatus?a("el-table-column",{attrs:{label:"操作",align:"center",width:200},scopedSlots:t._u([{key:"default",fn:function(e){return[0==t.params.qStatus?a("a",{staticClass:"t-edit iconfont bbs-duigouzhuanhuan",attrs:{href:"javascript:void(0)",title:"通过"},on:{click:function(a){t.check(e.row.id)}}}):t._e(),t._v(" "),0==t.params.qStatus?a("a",{staticClass:"t-del iconfont bbs-guanbi",attrs:{href:"javascript:void(0)",title:"拒绝"},on:{click:function(a){t.reject(e.row.id)}}}):t._e(),t._v(" "),2==t.params.qStatus?a("a",{staticClass:"t-del iconfont bbs-guanbi",attrs:{href:"javascript:void(0)",title:"关闭"},on:{click:function(a){t.stopLive(e.row.id)}}}):t._e(),t._v(" "),5==t.params.qStatus?a("a",{staticClass:"t-edit iconfont bbs-duigouzhuanhuan",attrs:{href:"javascript:void(0)",title:"开启"},on:{click:function(a){t.startLive(e.row.id)}}}):t._e()]}}])}):t._e()],1)],1)]),t._v(" "),a("div",{staticClass:"table-bottom-bar"},[a("div",{staticClass:"pull-left"},[0==t.params.qStatus?a("el-button",{staticClass:"btn-pass",attrs:{disabled:t.disabled},on:{click:function(e){t.check(t.ids)}}},[t._v("\n                  批量审核")]):t._e(),t._v(" "),0==t.params.qStatus?a("el-button",{staticClass:"btn-refuse",attrs:{disabled:t.disabled},on:{click:function(e){t.reject(t.ids)}}},[t._v("\n                批量拒绝")]):t._e(),t._v(" "),2==t.params.qStatus?a("el-button",{staticClass:"btn-refuse",attrs:{disabled:t.disabled},on:{click:function(e){t.stopLive(t.ids)}}},[t._v("\n                批量关闭")]):t._e(),t._v(" "),5==t.params.qStatus?a("el-button",{staticClass:"btn-pass",attrs:{disabled:t.disabled},on:{click:function(e){t.check(t.ids)}}},[t._v("\n                  批量恢复")]):t._e(),t._v(" "),a("span",{staticClass:"ml-48"},[t._v("每页显示\n                "),a("el-input",{staticClass:"input-sm",attrs:{type:"number",min:"1",max:"50"},on:{blur:t.changeSize},model:{value:t.changePageSize,callback:function(e){t.changePageSize=e},expression:"changePageSize"}}),t._v("条,输入后按回车")],1)],1),t._v(" "),a("div",{staticClass:"pull-right"},[a("el-pagination",{attrs:{layout:"total,prev, pager, next",total:t.totalCount,"page-size":t.params.pageSize,"current-page":t.currentPage},on:{"update:currentPage":function(e){t.currentPage=e},"current-change":t.getPage,"size-change":t.getSize}})],1)])])},staticRenderFns:[]}}});