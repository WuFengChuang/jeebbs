webpackJsonp([25],{1061:function(t,a,e){function i(t){e(1331)}var n=e(99)(e(1173),e(1417),i,null,null);t.exports=n.exports},1063:function(t,a,e){"use strict";function i(t){return e.i(x.a)({url:"/api/admin/group/list",method:"post",signValidate:!1,data:t})}function n(t){return e.i(x.a)({url:"/api/admin/group/delete",method:"post",signValidate:!0,data:t})}function s(t){return e.i(x.a)({url:"/api/admin/group/get",method:"post",signValidate:!1,data:t})}function r(t){return e.i(x.a)({url:"/api/admin/group/update",method:"post",signValidate:!0,data:t})}function d(t){return e.i(x.a)({url:"/api/admin/group/save",method:"post",signValidate:!0,data:t})}function o(t){return e.i(x.a)({url:"/api/admin/user/list",method:"post",signValidate:!1,data:t})}function u(t){return e.i(x.a)({url:"/api/admin/user/official_list",method:"post",signValidate:!1,data:t})}function l(t){return e.i(x.a)({url:"/api/admin/user/get",method:"post",signValidate:!1,data:t})}function p(t){return e.i(x.a)({url:"/api/admin/user/update",method:"post",signValidate:!0,data:t})}function c(t){return e.i(x.a)({url:"/api/admin/user/save",method:"post",signValidate:!0,data:t})}function m(t){return e.i(x.a)({url:"/api/admin/user/delete",method:"post",signValidate:!0,data:t})}function f(t){return e.i(x.a)({url:"/api/admin/admin/list",method:"post",signValidate:!1,data:t})}function h(t){return e.i(x.a)({url:"/api/admin/admin/update",method:"post",signValidate:!0,data:t})}function g(t){return e.i(x.a)({url:"/api/admin/admin/save",method:"post",signValidate:!0,data:t})}function v(t){return e.i(x.a)({url:"/api/admin/role/list",method:"post",signValidate:!1,data:t})}function _(t){return e.i(x.a)({url:"/api/admin/role/delete",method:"post",signValidate:!0,data:t})}function y(t){return e.i(x.a)({url:"/api/admin/role/get",method:"post",signValidate:!1,data:t})}function V(t){return e.i(x.a)({url:"/api/admin/role/update",method:"post",signValidate:!0,data:t})}function b(t){return e.i(x.a)({url:"/api/admin/account/list",method:"post",signValidate:!0,data:t})}function C(t){return e.i(x.a)({url:"/api/admin/account/delete",method:"post",signValidate:!0,data:t})}function $(t){return e.i(x.a)({url:"/api/admin/message/sys_list",method:"post",signValidate:!0,data:t})}function I(t){return e.i(x.a)({url:"/api/admin/message/delete",method:"post",signValidate:!0,data:t})}function k(t){return e.i(x.a)({url:"/api/admin/message/sendSys",method:"post",signValidate:!0,data:t})}a.a=i,a.w=n,a.t=s,a.u=r,a.v=d,a.s=o,a.n=u,a.k=l,a.l=p,a.m=c,a.o=m,a.r=f,a.p=h,a.q=g,a.i=v,a.j=_,a.g=y,a.h=V,a.e=b,a.f=C,a.c=$,a.d=I,a.b=k;var x=e(224),G=e(151);e.n(G)},1173:function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var i=e(1063);a.default={data:function(){return{typeId:1,groupList:[]}},methods:{getList:function(t){var a=this;i.a({groupType:t}).then(function(t){"100"==t.code?a.groupList=t.body:a.$message({message:t.message,type:"error"})}).catch(function(t){a.$message({message:"数据拉取异常",type:"error"})})},deleteGroup:function(t){var a=this,e={ids:t},n=this;this.$confirm("您确定要删除当前会员组吗?","提示",{type:"warning"}).then(function(t){i.w(e).then(function(t){console.log(t),"100"==t.code&&(n.getList(n.typeId),a.$message({message:"删除成功",type:"success"}))})}).catch(function(t){})},addGroup:function(t){this.$router.push({path:"/usergroupadd",query:{id:0,typeId:t,type:"add"}})},editGroup:function(t,a){this.$router.push({path:"/usergroupedit",query:{id:t,typeId:a,type:"edit"}})}},created:function(){var t=this.$route.query.typeId;this.typeId=t,this.getList(t)}}},1246:function(t,a,e){a=t.exports=e(976)(!1),a.push([t.i,"",""])},1331:function(t,a,e){var i=e(1246);"string"==typeof i&&(i=[[t.i,i,""]]),i.locals&&(t.exports=i.locals);e(977)("367e0a53",i,!0)},1417:function(t,a){t.exports={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",{staticClass:"forum-module"},[t._m(0),t._v(" "),e("div",{staticClass:"table-top-bar clearfix"},[e("a",{staticClass:"add-Class",attrs:{href:"javascript:void(0)"},on:{click:function(a){t.addGroup(t.$route.query.typeId)}}},[t._v("添加")])]),t._v(" "),e("div",{staticClass:"table-responsive"},[e("form",{staticClass:"form-horizontal no-margin"},[e("table",{staticClass:"bbs-table table-striped table-hover"},[e("tr",{staticClass:"table-header"},[e("th",[t._v("头衔")]),t._v(" "),e("th",[t._v("头衔图标")]),t._v(" "),1==t.typeId?e("th",[t._v("需要累计积分")]):t._e(),t._v(" "),e("th",[t._v("操作")])]),t._v(" "),t._l(t.groupList,function(a){return e("tr",{key:a.id},[e("td",{staticClass:"blue"},[t._v(t._s(a.name))]),t._v(" "),e("td",[e("img",{attrs:{src:t.$store.state.baseUrl+a.imgPath,alt:""}})]),t._v(" "),1==t.typeId?e("td",[t._v(t._s(a.point))]):t._e(),t._v(" "),e("td",[e("a",{staticClass:"t-edit iconfont bbs-edit",attrs:{href:"javascript:void(0)",title:"编辑"},on:{click:function(e){t.editGroup(a.id,t.typeId)}}}),t._v(" "),e("a",{staticClass:"t-del iconfont bbs-delete",attrs:{href:"javascript:void(0)",title:"删除"},on:{click:function(e){t.deleteGroup(a.id)}}})])])})],2)])])])},staticRenderFns:[function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",{staticClass:"forum-header"},[e("span",{staticClass:"forum-name"},[t._v("用户组列表")])])}]}}});