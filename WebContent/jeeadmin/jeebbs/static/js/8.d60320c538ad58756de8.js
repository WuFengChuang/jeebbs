webpackJsonp([8],{1001:function(t,e,r){function a(t){r(1334)}var i=r(99)(r(1113),r(1420),a,"data-v-89ba5480",null);t.exports=i.exports},1065:function(t,e){e.f={}.propertyIsEnumerable},1066:function(t,e,r){var a=r(38),i=r(59),s=r(225),n=r(1067),o=r(116).f;t.exports=function(t){var e=i.Symbol||(i.Symbol=s?{}:a.Symbol||{});"_"==t.charAt(0)||t in e||o(e,t,{value:n.f(t)})}},1067:function(t,e,r){e.f=r(39)},1068:function(t,e){e.f=Object.getOwnPropertySymbols},1070:function(t,e,r){t.exports={default:r(1073),__esModule:!0}},1071:function(t,e,r){t.exports={default:r(1074),__esModule:!0}},1072:function(t,e,r){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}e.__esModule=!0;var i=r(1071),s=a(i),n=r(1070),o=a(n),l="function"==typeof o.default&&"symbol"==typeof s.default?function(t){return typeof t}:function(t){return t&&"function"==typeof o.default&&t.constructor===o.default&&t!==o.default.prototype?"symbol":typeof t};e.default="function"==typeof o.default&&"symbol"===l(s.default)?function(t){return void 0===t?"undefined":l(t)}:function(t){return t&&"function"==typeof o.default&&t.constructor===o.default&&t!==o.default.prototype?"symbol":void 0===t?"undefined":l(t)}},1073:function(t,e,r){r(1079),r(372),r(1080),r(1081),t.exports=r(59).Symbol},1074:function(t,e,r){r(373),r(374),t.exports=r(1067).f("iterator")},1075:function(t,e,r){var a=r(365),i=r(1068),s=r(1065);t.exports=function(t){var e=a(t),r=i.f;if(r)for(var n,o=r(t),l=s.f,f=0;o.length>f;)l.call(t,n=o[f++])&&e.push(n);return e}},1076:function(t,e,r){var a=r(119);t.exports=Array.isArray||function(t){return"Array"==a(t)}},1077:function(t,e,r){var a=r(227)("meta"),i=r(102),s=r(117),n=r(116).f,o=0,l=Object.isExtensible||function(){return!0},f=!r(152)(function(){return l(Object.preventExtensions({}))}),u=function(t){n(t,a,{value:{i:"O"+ ++o,w:{}}})},c=function(t,e){if(!i(t))return"symbol"==typeof t?t:("string"==typeof t?"S":"P")+t;if(!s(t,a)){if(!l(t))return"F";if(!e)return"E";u(t)}return t[a].i},g=function(t,e){if(!s(t,a)){if(!l(t))return!0;if(!e)return!1;u(t)}return t[a].w},d=function(t){return f&&m.NEED&&l(t)&&!s(t,a)&&u(t),t},m=t.exports={KEY:a,NEED:!1,fastKey:c,getWeak:g,onFreeze:d}},1078:function(t,e,r){var a=r(1065),i=r(226),s=r(118),n=r(366),o=r(117),l=r(367),f=Object.getOwnPropertyDescriptor;e.f=r(100)?f:function(t,e){if(t=s(t),e=n(e,!0),l)try{return f(t,e)}catch(t){}if(o(t,e))return i(!a.f.call(t,e),t[e])}},1079:function(t,e,r){"use strict";var a=r(38),i=r(117),s=r(100),n=r(101),o=r(371),l=r(1077).KEY,f=r(152),u=r(228),c=r(153),g=r(227),d=r(39),m=r(1067),p=r(1066),v=r(1075),_=r(1076),h=r(68),b=r(118),y=r(366),I=r(226),x=r(368),k=r(369),w=r(1078),$=r(116),S=r(365),C=w.f,q=$.f,O=k.f,P=a.Symbol,E=a.JSON,j=E&&E.stringify,F=d("_hidden"),A=d("toPrimitive"),N={}.propertyIsEnumerable,R=u("symbol-registry"),M=u("symbols"),K=u("op-symbols"),J=Object.prototype,U="function"==typeof P,T=a.QObject,V=!T||!T.prototype||!T.prototype.findChild,D=s&&f(function(){return 7!=x(q({},"a",{get:function(){return q(this,"a",{value:7}).a}})).a})?function(t,e,r){var a=C(J,e);a&&delete J[e],q(t,e,r),a&&t!==J&&q(J,e,a)}:q,z=function(t){var e=M[t]=x(P.prototype);return e._k=t,e},B=U&&"symbol"==typeof P.iterator?function(t){return"symbol"==typeof t}:function(t){return t instanceof P},G=function(t,e,r){return t===J&&G(K,e,r),h(t),e=y(e,!0),h(r),i(M,e)?(r.enumerable?(i(t,F)&&t[F][e]&&(t[F][e]=!1),r=x(r,{enumerable:I(0,!1)})):(i(t,F)||q(t,F,I(1,{})),t[F][e]=!0),D(t,e,r)):q(t,e,r)},L=function(t,e){h(t);for(var r,a=v(e=b(e)),i=0,s=a.length;s>i;)G(t,r=a[i++],e[r]);return t},W=function(t,e){return void 0===e?x(t):L(x(t),e)},Y=function(t){var e=N.call(this,t=y(t,!0));return!(this===J&&i(M,t)&&!i(K,t))&&(!(e||!i(this,t)||!i(M,t)||i(this,F)&&this[F][t])||e)},Q=function(t,e){if(t=b(t),e=y(e,!0),t!==J||!i(M,e)||i(K,e)){var r=C(t,e);return!r||!i(M,e)||i(t,F)&&t[F][e]||(r.enumerable=!0),r}},H=function(t){for(var e,r=O(b(t)),a=[],s=0;r.length>s;)i(M,e=r[s++])||e==F||e==l||a.push(e);return a},X=function(t){for(var e,r=t===J,a=O(r?K:b(t)),s=[],n=0;a.length>n;)!i(M,e=a[n++])||r&&!i(J,e)||s.push(M[e]);return s};U||(P=function(){if(this instanceof P)throw TypeError("Symbol is not a constructor!");var t=g(arguments.length>0?arguments[0]:void 0),e=function(r){this===J&&e.call(K,r),i(this,F)&&i(this[F],t)&&(this[F][t]=!1),D(this,t,I(1,r))};return s&&V&&D(J,t,{configurable:!0,set:e}),z(t)},o(P.prototype,"toString",function(){return this._k}),w.f=Q,$.f=G,r(370).f=k.f=H,r(1065).f=Y,r(1068).f=X,s&&!r(225)&&o(J,"propertyIsEnumerable",Y,!0),m.f=function(t){return z(d(t))}),n(n.G+n.W+n.F*!U,{Symbol:P});for(var Z="hasInstance,isConcatSpreadable,iterator,match,replace,search,species,split,toPrimitive,toStringTag,unscopables".split(","),tt=0;Z.length>tt;)d(Z[tt++]);for(var et=S(d.store),rt=0;et.length>rt;)p(et[rt++]);n(n.S+n.F*!U,"Symbol",{for:function(t){return i(R,t+="")?R[t]:R[t]=P(t)},keyFor:function(t){if(!B(t))throw TypeError(t+" is not a symbol!");for(var e in R)if(R[e]===t)return e},useSetter:function(){V=!0},useSimple:function(){V=!1}}),n(n.S+n.F*!U,"Object",{create:W,defineProperty:G,defineProperties:L,getOwnPropertyDescriptor:Q,getOwnPropertyNames:H,getOwnPropertySymbols:X}),E&&n(n.S+n.F*(!U||f(function(){var t=P();return"[null]"!=j([t])||"{}"!=j({a:t})||"{}"!=j(Object(t))})),"JSON",{stringify:function(t){if(void 0!==t&&!B(t)){for(var e,r,a=[t],i=1;arguments.length>i;)a.push(arguments[i++]);return e=a[1],"function"==typeof e&&(r=e),!r&&_(e)||(e=function(t,e){if(r&&(e=r.call(this,t,e)),!B(e))return e}),a[1]=e,j.apply(E,a)}}}),P.prototype[A]||r(84)(P.prototype,A,P.prototype.valueOf),c(P,"Symbol"),c(Math,"Math",!0),c(a.JSON,"JSON",!0)},1080:function(t,e,r){r(1066)("asyncIterator")},1081:function(t,e,r){r(1066)("observable")},1113:function(t,e,r){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=r(1072),i=r.n(a),s=r(364);e.default={data:function(){return{fileState:!0,dialogVisible:!1,loading:!0,count:0,state:!1,imgPath:"",params:{category:"image"},upobj:{type:"image",sessionKey:localStorage.getItem("sessionKey"),appId:this.$store.state.appId},upobj2:{type:"attach",sessionKey:localStorage.getItem("sessionKey"),appId:this.$store.state.appId},advertisingInfo:{},rules:{name:[{required:!0,message:"请输入广告名称",trigger:"blur"}],category:[{required:!0,message:"请选择一个类别",trigger:"change"}]},imageRules:{attr_image_url:[{required:!0,message:"请上传一张图片",trigger:"blur"}],attr_image_width:[{required:!0,type:"number",message:"请输入一个数字",trigger:"blur"}],attr_image_height:[{required:!0,type:"number",message:"请输入一个数字",trigger:"blur"}],attr_image_title:[{required:!0,message:"请输入链接提示",trigger:"blur"}],attr_image_link:[{required:!0,message:"请输入一个链接",trigger:"blur"}],attr_image_target:[{required:!0,message:"请选择一个链接目标",trigger:"change"}]},flashRules:{attr_flash_url:[{required:!0,message:"请上传一个flash文件",trigger:"blur"}],attr_flash_width:[{required:!0,type:"number",message:"请输入一个数字",trigger:"blur"}],attr_flash_height:[{required:!0,type:"number",message:"请输入一个数字",trigger:"blur"}]},codeRules:{code:[{required:!0,message:"请填入代码",trigger:"blur"}]},textRules:{attr_text_link:[{required:!0,message:"请输入一个链接",trigger:"blur"}],attr_text_font:[{required:!0,type:"number",message:"请输入一个数字大小",trigger:"blur"}],attr_text_title:[{required:!0,message:"请输入链接标题",trigger:"blur"}],attr_image_target:[{required:!0,message:"请选择一个链接目标",trigger:"change"}],attr_text_color:[{required:!0,message:"请选择一个颜色",trigger:"blur"}]}}},computed:{},methods:{showBig:function(){this.dialogVisible=!0},getAdvertisingInfo:function(t){var e=this;s.R({id:t}).then(function(t){console.log(t),"100"==t.code?(e.loading=!1,e.advertisingInfo=t.body,""==e.advertisingInfo.attr_image_url?e.state=!0:(e.imgPath=t.body.attr_image_url,e.state=!1)):(e.loading=!1,e.$message.error(t.message))}).catch(function(t){e.loading=!1,e.$message.error("数据拉取异常")})},updateAdvertisingInfo:function(){var t=this,e="image";switch(this.advertisingInfo.category){case"image":e="image";break;case"flash":e="flash";break;case"text":e="text";break;case"code":e="code";break;default:this.advertisingInfo.category="image",e="image"}this.$refs.commom.validate(function(r){if(!r)return!1;t.$refs[e].validate(function(e){if(!e)return!1;var r=t.advertisingInfo;s.T(r).then(function(e){console.log("类型="+(void 0===e?"undefined":i()(e))),"100"==e.code?(t.$message.success("修改成功"),setTimeout(function(){t.$router.push({path:"/advertisinglist"})},1e3)):t.$message.error(e.message)}).catch(function(e){t.$message.error("修改失败")})})})},back:function(){this.$router.push({path:"/advertisinglist"})},beforeAvatarUpload:function(t){this.fileState=!0;var e="image/jpeg"===t.type||"image/png"===t.type||"image/gif"===t.type,r=t.size/1024/1024<2;return e||this.$message.error("上传头像图片只能是 JPG 格式!"),r||this.$message.error("上传头像图片大小不能超过2MB!"),e&&r},getImgpreviewImg:function(t,e,r){this.imgPath=t.body.url,this.state=!1,this.fileState=!1,this.advertisingInfo.attr_image_url=t.body.url},getFlashPath:function(t,e,r){this.advertisingInfo.attr_flash_url=t.body.url},resetForm:function(t){this.$route.query.id;this.$refs[t].resetFields()}},created:function(){var t=this.$route.query.type,e=this.$route.query.id;"add"==t?this.getAdvertisingInfo(e):"edit"==t&&this.getAdvertisingInfo(e)},watch:{$route:function(t,e){var r=this.$route.query.id;this.loading=!0,this.getAdvertisingInfo(r)}}}},1249:function(t,e,r){e=t.exports=r(976)(!1),e.push([t.i,"",""])},1334:function(t,e,r){var a=r(1249);"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);r(977)("071f830e",a,!0)},1420:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"forum-module"},[r("div",{staticClass:"forum-header"},[r("span",{staticClass:"forum-name"},[t._v(t._s(t.$route.name))]),t._v(" "),r("div",{staticClass:"pull-right"},[r("el-button",{on:{click:t.back}},[t._v("返回列表")])],1)]),t._v(" "),r("div",{directives:[{name:"loading",rawName:"v-loading.body",value:t.loading,expression:"loading",modifiers:{body:!0}}],staticClass:"table-body table-responsive"},[r("el-form",{ref:"commom",attrs:{rules:t.rules,model:t.advertisingInfo}},[r("el-form-item",{staticClass:"form-group",attrs:{label:"广告名称",prop:"name"}},[r("el-col",{attrs:{span:6}},[r("el-input",{model:{value:t.advertisingInfo.name,callback:function(e){t.$set(t.advertisingInfo,"name",e)},expression:"advertisingInfo.name"}})],1),t._v(" "),r("el-col",{staticClass:"required",attrs:{span:1}},[t._v("* ")])],1),t._v(" "),r("el-form-item",{staticClass:"form-group",attrs:{label:"类型",prop:"category"}},[r("el-col",{attrs:{span:6}},[r("el-radio-group",{model:{value:t.advertisingInfo.category,callback:function(e){t.$set(t.advertisingInfo,"category",e)},expression:"advertisingInfo.category"}},[r("el-radio",{attrs:{label:"image"}},[t._v("图片")]),t._v(" "),r("el-radio",{attrs:{label:"flash"}},[t._v("flash")]),t._v(" "),r("el-radio",{attrs:{label:"code"}},[t._v("代码")]),t._v(" "),r("el-radio",{attrs:{label:"text"}},[t._v("文字")])],1)],1),t._v(" "),r("el-col",{staticClass:"required",attrs:{span:1}},[t._v("* ")])],1)],1),t._v(" "),r("el-form",{ref:"image",attrs:{rules:t.imageRules,model:t.advertisingInfo}},[r("el-form-item",{directives:[{name:"show",rawName:"v-show",value:"image"==t.advertisingInfo.category,expression:"advertisingInfo.category=='image'"}],staticClass:"form-group",attrs:{label:"广告内容"}},[r("el-col",{attrs:{span:6}},[r("el-form-item",{staticClass:"form-inner",attrs:{label:"图片路径",prop:"attr_image_url"}},[r("el-col",{attrs:{span:15}},[r("el-input",{model:{value:t.advertisingInfo.attr_image_url,callback:function(e){t.$set(t.advertisingInfo,"attr_image_url",e)},expression:"advertisingInfo.attr_image_url"}})],1)],1),t._v(" "),r("el-form-item",{staticClass:"form-inner",attrs:{label:" "}},[r("el-col",{attrs:{span:15}},[r("div",{class:t.state?"pic-box":"pic-box-no"},[t.state?t._e():r("img",{attrs:{src:t.$store.state.baseUrl+t.imgPath,alt:""},on:{click:t.showBig}})])])],1),t._v(" "),r("el-form-item",{staticClass:"form-inner",attrs:{label:"图片尺寸"}},[r("el-col",{attrs:{span:15}},[r("el-col",{attrs:{span:11}},[r("el-form-item",{attrs:{prop:"attr_image_width"}},[r("el-input",{staticStyle:{width:"100%"},attrs:{placeholder:"宽"},model:{value:t.advertisingInfo.attr_image_width,callback:function(e){t.$set(t.advertisingInfo,"attr_image_width",t._n(e))},expression:"advertisingInfo.attr_image_width"}})],1)],1),t._v(" "),r("el-col",{staticClass:"line",staticStyle:{"text-align":"center"},attrs:{span:2}},[t._v("-")]),t._v(" "),r("el-col",{attrs:{span:11}},[r("el-form-item",{attrs:{prop:"attr_image_height"}},[r("el-input",{staticStyle:{width:"100%"},attrs:{placeholder:"高"},model:{value:t.advertisingInfo.attr_image_height,callback:function(e){t.$set(t.advertisingInfo,"attr_image_height",t._n(e))},expression:"advertisingInfo.attr_image_height"}})],1)],1)],1)],1),t._v(" "),r("el-form-item",{staticClass:"form-inner",attrs:{label:"链接地址",prop:"attr_image_link"}},[r("el-col",{attrs:{span:15}},[r("el-input",{model:{value:t.advertisingInfo.attr_image_link,callback:function(e){t.$set(t.advertisingInfo,"attr_image_link",e)},expression:"advertisingInfo.attr_image_link"}})],1)],1),t._v(" "),r("el-form-item",{staticClass:"form-inner",attrs:{label:"链接提示",prop:"attr_image_title"}},[r("el-col",{attrs:{span:15}},[r("el-input",{model:{value:t.advertisingInfo.attr_image_title,callback:function(e){t.$set(t.advertisingInfo,"attr_image_title",e)},expression:"advertisingInfo.attr_image_title"}})],1)],1),t._v(" "),r("el-form-item",{staticClass:"form-inner",attrs:{label:"链接目标",prop:"attr_image_target"}},[r("el-col",{attrs:{span:15}},[r("el-radio-group",{model:{value:t.advertisingInfo.attr_image_target,callback:function(e){t.$set(t.advertisingInfo,"attr_image_target",e)},expression:"advertisingInfo.attr_image_target"}},[r("el-radio",{attrs:{label:"_blank"}},[t._v("新窗口")]),t._v(" "),r("el-radio",{attrs:{label:"_self"}},[t._v("原窗口")])],1)],1)],1)],1),t._v(" "),r("el-col",{attrs:{span:6}},[r("el-upload",{staticClass:"upload-demo",attrs:{action:t.$store.state.upLoadUrl,name:"uploadFile",data:t.upobj,"before-upload":t.beforeAvatarUpload,"on-success":t.getImgpreviewImg,"show-file-list":t.fileState}},[r("el-button",{staticStyle:{"margin-left":"10px"},attrs:{type:"primary"}},[t._v("点击上传")])],1),t._v(" "),r("el-dialog",{staticClass:"pic-dialog",attrs:{title:"按下esc退出全屏",size:"full"},model:{value:t.dialogVisible,callback:function(e){t.dialogVisible=e},expression:"dialogVisible"}},[r("img",{staticClass:"big-img",attrs:{src:t.$store.state.baseUrl+t.imgPath,alt:""}})])],1)],1)],1),t._v(" "),r("el-form",{ref:"flash",attrs:{rules:t.flashRules,model:t.advertisingInfo}},[r("el-form-item",{directives:[{name:"show",rawName:"v-show",value:"flash"==t.advertisingInfo.category,expression:"advertisingInfo.category=='flash'"}],staticClass:"form-group",attrs:{label:"广告内容"}},[r("el-col",{attrs:{span:6}},[r("el-form-item",{staticClass:"form-inner",attrs:{label:"flash路径",prop:"attr_flash_url"}},[r("el-col",{attrs:{span:15}},[r("el-input",{model:{value:t.advertisingInfo.attr_flash_url,callback:function(e){t.$set(t.advertisingInfo,"attr_flash_url",e)},expression:"advertisingInfo.attr_flash_url"}})],1)],1),t._v(" "),r("el-form-item",{staticClass:"form-inner",attrs:{label:"flash尺寸",required:!0}},[r("el-col",{attrs:{span:15}},[r("el-col",{attrs:{span:11}},[r("el-form-item",{attrs:{prop:"attr_flash_width"}},[r("el-input",{staticStyle:{width:"100%"},attrs:{placeholder:"宽"},model:{value:t.advertisingInfo.attr_flash_width,callback:function(e){t.$set(t.advertisingInfo,"attr_flash_width",t._n(e))},expression:"advertisingInfo.attr_flash_width"}})],1)],1),t._v(" "),r("el-col",{staticClass:"line",staticStyle:{"text-align":"center"},attrs:{span:2}},[t._v("-")]),t._v(" "),r("el-col",{attrs:{span:11}},[r("el-form-item",{attrs:{prop:"attr_flash_height"}},[r("el-input",{staticStyle:{width:"100%"},attrs:{placeholder:"高"},model:{value:t.advertisingInfo.attr_flash_height,callback:function(e){t.$set(t.advertisingInfo,"attr_flash_height",t._n(e))},expression:"advertisingInfo.attr_flash_height"}})],1)],1)],1)],1)],1),t._v(" "),r("el-col",{attrs:{span:6}},[r("el-upload",{staticClass:"upload-demo",attrs:{action:t.$store.state.upLoadUrl,name:"uploadFile",data:t.upobj2,"on-success":t.getFlashPath}},[r("el-button",{staticStyle:{"margin-left":"10px"},attrs:{type:"primary"}},[t._v("点击上传")])],1)],1)],1)],1),t._v(" "),r("el-form",{ref:"text",attrs:{rules:t.textRules,model:t.advertisingInfo}},[r("el-form-item",{directives:[{name:"show",rawName:"v-show",value:"text"==t.advertisingInfo.category,expression:"advertisingInfo.category=='text'"}],staticClass:"form-group",attrs:{label:"广告内容"}},[r("el-col",{attrs:{span:6}},[r("el-form-item",{staticClass:"form-inner",attrs:{label:"文字内容",prop:"attr_text_title"}},[r("el-col",{attrs:{span:15}},[r("el-input",{model:{value:t.advertisingInfo.attr_text_title,callback:function(e){t.$set(t.advertisingInfo,"attr_text_title",e)},expression:"advertisingInfo.attr_text_title"}})],1)],1),t._v(" "),r("el-form-item",{staticClass:"form-inner",attrs:{label:"文字链接",prop:"attr_text_link"}},[r("el-col",{attrs:{span:15}},[r("el-input",{model:{value:t.advertisingInfo.attr_text_link,callback:function(e){t.$set(t.advertisingInfo,"attr_text_link",e)},expression:"advertisingInfo.attr_text_link"}})],1)],1),t._v(" "),r("el-form-item",{staticClass:"form-inner",attrs:{label:"文字颜色",prop:"attr_text_color"}},[r("el-col",{attrs:{span:10}},[r("el-input",{model:{value:t.advertisingInfo.attr_text_color,callback:function(e){t.$set(t.advertisingInfo,"attr_text_color",e)},expression:"advertisingInfo.attr_text_color"}})],1),t._v(" "),r("el-col",{staticStyle:{"text-align":"right"},attrs:{span:5}},[r("el-color-picker",{model:{value:t.advertisingInfo.attr_text_color,callback:function(e){t.$set(t.advertisingInfo,"attr_text_color",e)},expression:"advertisingInfo.attr_text_color"}})],1)],1),t._v(" "),r("el-form-item",{staticClass:"form-inner",attrs:{label:"文字大小",prop:"attr_text_font"}},[r("el-col",{attrs:{span:11}},[r("el-input",{model:{value:t.advertisingInfo.attr_text_font,callback:function(e){t.$set(t.advertisingInfo,"attr_text_font",t._n(e))},expression:"advertisingInfo.attr_text_font"}})],1),t._v(" "),r("el-col",{staticStyle:{"text-align":"center"},attrs:{span:4}},[t._v("\n                            如12\n                        ")])],1),t._v(" "),r("el-form-item",{staticClass:"form-inner",attrs:{label:"链接目标",prop:"attr_text_target"}},[r("el-col",{attrs:{span:15}},[r("el-radio-group",{model:{value:t.advertisingInfo.attr_text_target,callback:function(e){t.$set(t.advertisingInfo,"attr_text_target",e)},expression:"advertisingInfo.attr_text_target"}},[r("el-radio",{attrs:{label:"_blank"}},[t._v("新窗口")]),t._v(" "),r("el-radio",{attrs:{label:"_self"}},[t._v("原窗口")])],1)],1)],1)],1)],1)],1),t._v(" "),r("el-form",{ref:"code",attrs:{rules:t.codeRules,model:t.advertisingInfo}},[r("el-form-item",{directives:[{name:"show",rawName:"v-show",value:"code"==t.advertisingInfo.category,expression:"advertisingInfo.category=='code'"}],staticClass:"form-group",attrs:{label:"广告内容"}},[r("el-col",{attrs:{span:6}},[r("el-form-item",{staticClass:"form-inner",attrs:{label:"代码",prop:"code"}},[r("el-col",{attrs:{span:15}},[r("el-input",{attrs:{type:"textarea"},model:{value:t.advertisingInfo.code,callback:function(e){t.$set(t.advertisingInfo,"code",e)},expression:"advertisingInfo.code"}})],1)],1)],1)],1)],1),t._v(" "),r("div",{staticClass:"margin-md"},[r("input",{staticClass:"bbs-submit",attrs:{type:"button",value:"提交"},on:{click:t.updateAdvertisingInfo}}),t._v(" "),r("input",{staticClass:"bbs-reset",attrs:{type:"reset",value:"重置"}})])],1)])},staticRenderFns:[]}}});