webpackJsonp([64],{1069:function(t,a,i){"use strict";function e(t){return i.i(F.a)({url:"/api/admin/forum/listGroupByCategory",method:"post",data:t})}function r(t){return i.i(F.a)({url:"/api/admin/category/list",method:"post",data:t})}function n(t){return i.i(F.a)({url:"/api/admin/forum/list ",method:"post",data:t})}function o(t){return i.i(F.a)({url:"/api/admin/forum/batchupdate",method:"post",signValidate:!0,data:t})}function s(t){return i.i(F.a)({url:"/api/admin/category/delete",method:"post",signValidate:!0,data:t})}function u(t){return i.i(F.a)({url:"/api/admin/forum/delete",method:"post",signValidate:!0,data:t})}function d(t){return i.i(F.a)({url:"/api/admin/category/o_priority_update",method:"post",signValidate:!0,data:t})}function l(t){return i.i(F.a)({url:"/api//admin/forum/get",method:"post",data:t})}function p(t){return i.i(F.a)({url:"/api/admin/forum/update",method:"post",signValidate:!0,data:t})}function c(t){return i.i(F.a)({url:"/api/admin/topicType/tree",method:"post",signValidate:!1,data:t})}function m(t){return i.i(F.a)({url:"/api/admin/topicType/list",method:"post",signValidate:!1,data:t})}function f(t){return i.i(F.a)({url:"/api/admin/topicType/get",method:"post",signValidate:!1,data:t})}function v(t){return i.i(F.a)({url:"/api/admin/topicType/delete",method:"post",signValidate:!0,data:t})}function h(t){return i.i(F.a)({url:"/api/admin/topicType/update",method:"post",signValidate:!0,data:t})}function g(t){return i.i(F.a)({url:"/api/admin/topicType/save",method:"post",signValidate:!0,data:t})}function _(t){return i.i(F.a)({url:"/api/admin/sensitivity/list",method:"post",signValidate:!1,data:t})}function y(t){return i.i(F.a)({url:"/api/admin/sensitivity/delete",method:"post",signValidate:!0,data:t})}function b(t){return i.i(F.a)({url:"/api/admin/sensitivity/save",method:"post",signValidate:!0,data:t})}function V(t){return i.i(F.a)({url:"/api/admin/sensitivity/batch_update",method:"post",signValidate:!0,data:t})}function C(t){return i.i(F.a)({url:"/api/admin/sensitivity/batch_save",method:"post",signValidate:!0,data:t})}function x(t){return i.i(F.a)({url:"/api/admin/report/list",method:"post",data:t})}function k(t){return i.i(F.a)({url:"/api/admin/report/process",signValidate:!0,method:"post",data:t})}function w(t){return i.i(F.a)({url:"/api/admin/report/delete",signValidate:!0,method:"post",data:t})}function $(t){return i.i(F.a)({url:"/api/admin/report/get",method:"post",data:t})}a.t=e,a.q=r,a.a=n,a.u=o,a.v=s,a.w=u,a.x=d,a.r=l,a.s=p,a.k=c,a.o=m,a.l=f,a.p=v,a.m=h,a.n=g,a.g=_,a.j=y,a.h=b,a.i=V,a.f=C,a.d=x,a.e=k,a.c=w,a.b=$;var F=i(224),T=i(151);i.n(T)},1095:function(t,a,i){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var e=i(1069);a.default={data:function(){return{params:{type:1,words:""},rules:{words:[{required:!0,message:"请填写敏感词内容",trigger:"blur"}]}}},methods:{back:function(){this.$router.push({path:"/sensitivitylist",query:{noceStr:Math.round(10*Math.random())}})},add:function(t){var a=this;this.$refs[t].validate(function(t){if(!t)return!1;e.f(a.params).then(function(t){"100"==t.code&&(a.$message.success("添加成功"),setTimeout(function(){a.$router.push({path:"/sensitivitylist"})},1e3))})})},resetForm:function(t){this.$refs[t].resetFields()}},created:function(){}}},1257:function(t,a,i){a=t.exports=i(976)(!1),a.push([t.i,".el-radio-group .el-radio{display:block;margin-left:0!important;margin-bottom:12px}",""])},1342:function(t,a,i){var e=i(1257);"string"==typeof e&&(e=[[t.i,e,""]]),e.locals&&(t.exports=e.locals);i(977)("59c49990",e,!0)},1428:function(t,a){t.exports={render:function(){var t=this,a=t.$createElement,i=t._self._c||a;return i("div",{staticClass:"forum-module"},[i("div",{staticClass:"forum-header"},[i("span",{staticClass:"forum-name"},[t._v(t._s(t.$route.name))]),t._v(" "),i("div",{staticClass:"pull-right"},[i("el-button",{on:{click:t.back}},[t._v("返回列表")])],1)]),t._v(" "),i("div",{staticClass:"table-body table-responsive"},[i("el-form",{ref:"ruleForm",attrs:{rules:t.rules,model:t.params}},[i("el-form-item",{staticClass:"form-group",attrs:{label:"输入",prop:"words"}},[i("el-col",{attrs:{span:9}},[i("el-input",{attrs:{type:"textarea",rows:14},model:{value:t.params.words,callback:function(a){t.$set(t.params,"words",a)},expression:"params.words"}})],1),t._v(" "),i("el-col",{attrs:{span:1}},[t._v(" ")]),t._v(" "),i("el-col",{attrs:{span:9}},[i("div",{staticClass:"font-hint",staticStyle:{"margin-left":"-15px"}},[i("p",[t._v(" 每行一组敏感词，敏感词和替换词之间使用“=”进行分割；")]),t._v(" "),i("p",[t._v("如果只是想将某个敏感词直接替换成 **，则只输入敏感词即可；")]),t._v(" "),i("p",[t._v("如果想禁止发布所有包含敏感词的文字，则只需要输入敏感词并在")]),t._v(" "),i("p",[t._v("\n                            列表页进行配置；")]),t._v(" "),i("p",[t._v(" 例如：")]),t._v(" "),i("p",[t._v("toobad")]),t._v(" "),i("p",[t._v("nobad")]),t._v(" "),i("p",[t._v("badword=good")])])])],1),t._v(" "),i("el-form-item",{staticClass:"form-group",attrs:{label:"添加方式"}},[i("el-col",{attrs:{span:10}},[i("el-radio-group",{model:{value:t.params.type,callback:function(a){t.$set(t.params,"type",a)},expression:"params.type"}},[i("el-radio",{attrs:{label:1}},[t._v("不替换已经存在的敏感词")]),t._v(" "),i("el-radio",{attrs:{label:2}},[t._v("替换全部敏感词")]),t._v(" "),i("el-radio",{attrs:{label:3}},[t._v("清空当前敏感词并导入新敏感词")])],1)],1)],1)],1),t._v(" "),i("div",{staticClass:"margin-md"},[i("input",{staticClass:"bbs-submit",attrs:{type:"button",value:"提交"},on:{click:function(a){t.add("ruleForm")}}}),t._v(" "),i("input",{staticClass:"bbs-reset",attrs:{type:"reset",value:"重置"},on:{click:function(a){t.resetForm("ruleForm")}}})])],1)])},staticRenderFns:[]}},983:function(t,a,i){function e(t){i(1342)}var r=i(99)(i(1095),i(1428),e,null,null);t.exports=r.exports}});