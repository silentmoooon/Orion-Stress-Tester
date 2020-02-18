(function(e){function t(t){for(var r,n,l=t[0],i=t[1],c=t[2],p=0,d=[];p<l.length;p++)n=l[p],Object.prototype.hasOwnProperty.call(a,n)&&a[n]&&d.push(a[n][0]),a[n]=0;for(r in i)Object.prototype.hasOwnProperty.call(i,r)&&(e[r]=i[r]);u&&u(t);while(d.length)d.shift()();return o.push.apply(o,c||[]),s()}function s(){for(var e,t=0;t<o.length;t++){for(var s=o[t],r=!0,l=1;l<s.length;l++){var i=s[l];0!==a[i]&&(r=!1)}r&&(o.splice(t--,1),e=n(n.s=s[0]))}return e}var r={},a={app:0},o=[];function n(t){if(r[t])return r[t].exports;var s=r[t]={i:t,l:!1,exports:{}};return e[t].call(s.exports,s,s.exports,n),s.l=!0,s.exports}n.m=e,n.c=r,n.d=function(e,t,s){n.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:s})},n.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},n.t=function(e,t){if(1&t&&(e=n(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var s=Object.create(null);if(n.r(s),Object.defineProperty(s,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var r in e)n.d(s,r,function(t){return e[t]}.bind(null,r));return s},n.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return n.d(t,"a",t),t},n.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},n.p="/";var l=window["webpackJsonp"]=window["webpackJsonp"]||[],i=l.push.bind(l);l.push=t,l=l.slice();for(var c=0;c<l.length;c++)t(l[c]);var u=i;o.push([0,"chunk-vendors"]),s()})({0:function(e,t,s){e.exports=s("56d7")},"034f":function(e,t,s){"use strict";var r=s("64a9"),a=s.n(r);a.a},"56d7":function(e,t,s){"use strict";s.r(t);s("cadf"),s("551c"),s("f751"),s("097d");var r=s("2b0e"),a=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("el-container",{attrs:{id:"app"}},[r("div",{attrs:{id:"mobile-bar"}},[r("img",{staticClass:"menu-button",attrs:{src:s("cf05"),alt:""}}),r("div",{staticClass:"logo"},[r("a",{attrs:{href:"/"}},[e._v("Orion-Stress-Tester")])])]),r("el-header",{attrs:{id:"pc-bar"}},[r("a",{attrs:{id:"logo",href:"/"}},[r("img",{attrs:{src:s("cf05"),alt:""}}),r("span",[e._v("Orion-Stress-Tester")])])]),r("el-container",[r("el-main",{attrs:{id:"main"}},[r("el-form",{ref:"requestTable",attrs:{model:e.requestData,"label-width":"120px","label-position":"right"}},[r("el-form-item",{attrs:{label:e.$t("requestType")}},[r("el-radio-group",{attrs:{placeholder:e.$t("select")},model:{value:e.requestData.requestType,callback:function(t){e.$set(e.requestData,"requestType",t)},expression:"requestData.requestType"}},[r("el-radio",{attrs:{label:"HTTP"}},[e._v("HTTP/S")]),r("el-radio",{attrs:{label:"WebSocket"}},[e._v("WebSocket/S")]),r("el-radio",{attrs:{label:"TCP"}},[e._v("TCP/S")])],1)],1),"HTTP"==e.requestData.requestType?r("el-form-item",{attrs:{label:e.$t("requestUrl"),prop:"url",rules:[{required:!0,message:e.$t("requestTips")},{pattern:/^(http|https):\/\/.+$/,message:e.$t("httpUrlInvalidTips")}],required:""}},[r("el-input",{attrs:{placeholder:e.$t("httpUrlPlaceholder")},model:{value:e.requestData.url,callback:function(t){e.$set(e.requestData,"url",t)},expression:"requestData.url"}},[r("el-select",{staticStyle:{width:"6rem",color:"#222"},attrs:{slot:"prepend",placeholder:"请选择"},slot:"prepend",model:{value:e.requestData.method,callback:function(t){e.$set(e.requestData,"method",t)},expression:"requestData.method"}},[r("el-option",{attrs:{value:"GET"}},[e._v("GET")]),r("el-option",{attrs:{value:"POST"}},[e._v("POST")]),r("el-option",{attrs:{value:"OPTIONS"}},[e._v("OPTIONS")]),r("el-option",{attrs:{value:"HEAD"}},[e._v("HEAD")]),r("el-option",{attrs:{value:"PUT"}},[e._v("PUT")]),r("el-option",{attrs:{value:"DELETE"}},[e._v("DELETE")]),r("el-option",{attrs:{value:"TRACE"}},[e._v("TRACE")]),r("el-option",{attrs:{value:"CONNECT"}},[e._v("CONNECT")]),r("el-option",{attrs:{value:"PATCH"}},[e._v("PATCH")]),r("el-option",{attrs:{value:"OTHER"}},[e._v("OTHER")])],1)],1)],1):e._e(),"WebSocket"==e.requestData.requestType?r("el-form-item",{attrs:{label:e.$t("requestUrl"),prop:"url",rules:[{required:!0,message:e.$t("requestTips")},{pattern:/^(ws|wss):\/\/.+$/,message:e.$t("websocketUrlInvalidTips")}],required:""}},[r("el-input",{attrs:{placeholder:e.$t("websocketUrlPlaceholder")},model:{value:e.requestData.url,callback:function(t){e.$set(e.requestData,"url",t)},expression:"requestData.url"}})],1):e._e(),"TCP"==e.requestData.requestType?r("el-form-item",{attrs:{label:e.$t("requestUrl"),required:""}},[r("el-col",{attrs:{xs:24,sm:2}},[r("el-form-item",[r("el-checkbox",{model:{value:e.requestData.isSSL,callback:function(t){e.$set(e.requestData,"isSSL",t)},expression:"requestData.isSSL"}},[e._v("SSL")])],1)],1),r("el-col",{attrs:{xs:24,sm:14}},[r("el-form-item",{attrs:{prop:"host",rules:[{required:!0,message:e.$t("tcpHostTips")}]}},[r("el-input",{attrs:{placeholder:e.$t("tcpHostPlaceholder")},model:{value:e.requestData.host,callback:function(t){e.$set(e.requestData,"host",t)},expression:"requestData.host"}})],1)],1),r("el-col",{attrs:{xs:0,sm:1}},[e._v(" ")]),r("el-col",{attrs:{xs:24,sm:7}},[r("el-form-item",[r("el-input",{attrs:{type:"number",min:"0",max:"65535",placeholder:e.$t("tcpPortPlaceholder")},model:{value:e.requestData.port,callback:function(t){e.$set(e.requestData,"port",t)},expression:"requestData.port"}})],1)],1)],1):e._e(),e.requestData.isSSL?r("el-form-item",{attrs:{label:e.$t("certSetting")}},[r("el-col",{attrs:{span:24}},[r("el-radio-group",{model:{value:e.requestData.cert,callback:function(t){e.$set(e.requestData,"cert",t)},expression:"requestData.cert"}},[r("el-radio",{attrs:{label:"DEFAULT"}},[e._v(e._s(e.$t("certDefault")))]),r("el-radio",{attrs:{label:"PEM"}},[e._v(e._s(e.$t("certPem")))]),r("el-radio",{attrs:{label:"PFX"}},[e._v(e._s(e.$t("certPfx")))]),r("el-radio",{attrs:{label:"JKS"}},[e._v(e._s(e.$t("certJks")))])],1)],1),"PEM"==e.requestData.cert||"PFX"==e.requestData.cert||"JKS"==e.requestData.cert?r("el-col",{attrs:{span:24}},[r("el-input",{attrs:{type:"textarea",placeholder:e.$t("certKey")},model:{value:e.requestData.certKey,callback:function(t){e.$set(e.requestData,"certKey",t)},expression:"requestData.certKey"}})],1):e._e(),"PEM"==e.requestData.cert||"PFX"==e.requestData.cert||"JKS"==e.requestData.cert?r("el-col",{attrs:{span:24}},[r("el-input",{attrs:{type:"textarea",placeholder:e.$t("certValue")},model:{value:e.requestData.certValue,callback:function(t){e.$set(e.requestData,"certValue",t)},expression:"requestData.certValue"}})],1):e._e(),"PEM"==e.requestData.cert||"PFX"==e.requestData.cert||"JKS"==e.requestData.cert?r("el-col",[r("el-button",{staticStyle:{"margin-right":"5px"},attrs:{type:"primary",size:"small"},on:{click:function(t){return e.$refs.readCertKey.click()}}},[e._v(e._s(e.$t("btnReadCertKey")))]),r("input",{ref:"readCertKey",staticStyle:{display:"none"},attrs:{type:"file",accept:".pem,.key,.p12,.jks,.txt"},on:{change:e.loadCertKey}}),r("el-button",{attrs:{type:"primary",size:"small"},on:{click:function(t){return e.$refs.readCertValue.click()}}},[e._v(e._s(e.$t("btnReadCertValue")))]),r("input",{ref:"readCertValue",staticStyle:{display:"none"},attrs:{type:"file",accept:".pem,.key,.p12,.jks,.txt"},on:{change:e.loadCertValue}})],1):e._e()],1):e._e(),"WebSocket"==e.requestData.requestType?r("el-form-item",{attrs:{label:"WebSocketVersion"}},[r("el-select",{staticStyle:{width:"100%"},attrs:{placeholder:"请选择"},model:{value:e.requestData.webSocketVersion,callback:function(t){e.$set(e.requestData,"webSocketVersion",t)},expression:"requestData.webSocketVersion"}},[r("el-option",{attrs:{value:"V00"}},[e._v("V00")]),r("el-option",{attrs:{value:"V07"}},[e._v("V07")]),r("el-option",{attrs:{value:"V08"}},[e._v("V08")]),r("el-option",{attrs:{value:"V13"}},[e._v("V13")])],1)],1):e._e(),"WebSocket"==e.requestData.requestType?r("el-form-item",{attrs:{label:"SubProtocols"}},[r("el-col",{attrs:{span:24}},e._l(e.requestData.subProtocols,(function(t,s){return r("el-form-item",{key:t.key},[r("el-col",{attrs:{xs:20,sm:21}},[r("el-input",{attrs:{placeholder:e.$t("inputValue")},model:{value:t.value,callback:function(s){e.$set(t,"value",s)},expression:"item.value"}})],1),r("el-col",{attrs:{xs:1,sm:1}},[e._v(" ")]),r("el-col",{attrs:{xs:3,sm:2}},[r("el-button",{attrs:{size:"small",type:"danger"},on:{click:function(s){return s.preventDefault(),e.removeSubProtocols(t)}}},[e._v(e._s(e.$t("btnRemove")))])],1)],1)})),1),r("el-col",{attrs:{span:24}},[r("el-button",{attrs:{type:"primary",size:"small"},on:{click:e.addSubProtocols}},[e._v(e._s(e.$t("btnAdd")))])],1)],1):e._e(),"HTTP"==e.requestData.requestType||"WebSocket"==e.requestData.requestType?r("el-form-item",{attrs:{label:e.$t("requestHeaders")}},[r("el-col",{attrs:{span:24}},e._l(e.requestData.headers,(function(t,s){return r("el-form-item",{key:s},[r("el-col",{attrs:{xs:24,sm:10}},[r("el-input",{attrs:{placeholder:e.$t("inputName")},model:{value:t.key,callback:function(s){e.$set(t,"key",s)},expression:"item.key"}})],1),r("el-col",{attrs:{xs:0,sm:1}},[e._v(" ")]),r("el-col",{attrs:{xs:20,sm:10}},[r("el-input",{attrs:{placeholder:e.$t("inputValue")},model:{value:t.value,callback:function(s){e.$set(t,"value",s)},expression:"item.value"}})],1),r("el-col",{attrs:{xs:1,sm:1}},[e._v(" ")]),r("el-col",{attrs:{xs:3,sm:2}},[r("el-button",{attrs:{size:"small",type:"danger"},on:{click:function(s){return s.preventDefault(),e.removeHeader(t)}}},[e._v(e._s(e.$t("btnRemove")))])],1)],1)})),1),r("el-col",{attrs:{span:24}},[r("el-button",{attrs:{type:"primary",size:"small"},on:{click:e.addHeader}},[e._v(e._s(e.$t("btnAdd")))])],1)],1):e._e(),"TCP"==e.requestData.requestType?r("el-form-item",{attrs:{label:"ServerName(SNI)"}},[r("el-input",{attrs:{placeholder:e.$t("requestServerNamePlaceholder")},model:{value:e.requestData.serverName,callback:function(t){e.$set(e.requestData,"serverName",t)},expression:"requestData.serverName"}})],1):e._e(),r("el-form-item",{attrs:{label:e.$t("requestBody")}},[r("el-input",{attrs:{type:"textarea",placeholder:e.$t("requestBodyPlaceholder")},model:{value:e.requestData.body,callback:function(t){e.$set(e.requestData,"body",t)},expression:"requestData.body"}})],1),r("el-form-item",{attrs:{label:e.$t("requestCount"),required:""}},[r("el-col",{attrs:{xs:24,sm:6}},[r("el-form-item",{attrs:{prop:"count",rules:[{required:!0,type:"number",min:0,message:e.$t("requestCountTips")}],required:""}},[r("el-input",{attrs:{placeholder:"HTTP"==e.requestData.requestType?e.$t("requestCountPlaceholder"):e.$t("requestTcpCountPlaceholder")},model:{value:e.requestData.count,callback:function(t){e.$set(e.requestData,"count",e._n(t))},expression:"requestData.count"}})],1)],1),r("el-col",{attrs:{sm:1}},[e._v(" ")]),r("el-col",{attrs:{xs:24,sm:8}},[r("el-form-item",{attrs:{prop:"average",rules:[{required:!0,type:"number",min:0,message:e.$t("requestAverageTips")}],required:""}},[r("el-input",{attrs:{placeholder:"HTTP"==e.requestData.requestType?e.$t("requestAveragePlaceholder"):e.$t("requestTcpAveragePlaceholder")},model:{value:e.requestData.average,callback:function(t){e.$set(e.requestData,"average",e._n(t))},expression:"requestData.average"}})],1)],1),r("el-col",{attrs:{sm:1}},[e._v(" ")]),r("el-col",{attrs:{xs:24,sm:8}},[r("el-form-item",{attrs:{prop:"interval",rules:[{required:!0,type:"number",min:1,message:e.$t("requestIntervalTips")}],required:""}},[r("el-input",{attrs:{placeholder:"HTTP"==e.requestData.requestType?e.$t("requestIntervalPlaceholder"):e.$t("requestTcpIntervalPlaceholder")},model:{value:e.requestData.interval,callback:function(t){e.$set(e.requestData,"interval",e._n(t))},expression:"requestData.interval"}})],1)],1)],1),r("el-form-item",{directives:[{name:"show",rawName:"v-show",value:e.isStatistics,expression:"isStatistics"}]},[e._v(e._s(e.statisticsInfo))]),r("el-form-item",[r("div",{staticStyle:{display:"flex","flex-wrap":"wrap"}},[r("div",{staticStyle:{"margin-right":"0.6rem"}},[r("el-tooltip",{staticClass:"item",attrs:{effect:"dark",content:e.$t("requestConfigPrintInfoTips"),placement:"top"}},[r("el-checkbox",{attrs:{label:e.$t("requestConfigPrintInfo")},model:{value:e.requestConfig.printResInfo,callback:function(t){e.$set(e.requestConfig,"printResInfo",t)},expression:"requestConfig.printResInfo"}})],1)],1),r("div",{staticStyle:{"margin-right":"0.6rem"}},[r("el-input",{attrs:{placeholder:e.$t("requestConfigTimeout"),type:"number"},model:{value:e.requestConfig.timeout,callback:function(t){e.$set(e.requestConfig,"timeout",e._n(t))},expression:"requestConfig.timeout"}})],1),r("div",{staticStyle:{"margin-right":"0.6rem"}},[r("el-checkbox",{attrs:{label:e.$t("requestConfigKeepAlive")},model:{value:e.requestConfig.keepAlive,callback:function(t){e.$set(e.requestConfig,"keepAlive",t)},expression:"requestConfig.keepAlive"}})],1),r("div",{directives:[{name:"show",rawName:"v-show",value:e.requestConfig.keepAlive&&"HTTP"==e.requestData.requestType,expression:"requestConfig.keepAlive && requestData.requestType == 'HTTP'"}],staticStyle:{flex:"1"}},[r("el-input",{attrs:{placeholder:e.$t("requestConfigPoolSize"),type:"number"},model:{value:e.requestConfig.poolSize,callback:function(t){e.$set(e.requestConfig,"poolSize",e._n(t))},expression:"requestConfig.poolSize"}})],1)])]),r("el-form-item",[r("el-button",{attrs:{type:"primary",loading:e.isExecuting},on:{click:e.execute}},[e._v(e._s(e.isExecuting?e.$t("btnIsExecute"):e.$t("btnExecute")))]),r("el-button",{directives:[{name:"show",rawName:"v-show",value:e.isExecuting,expression:"isExecuting"}],on:{click:e.executeCancel}},[e._v(e._s(e.$t("btnCancel")))])],1)],1),r("div",{directives:[{name:"show",rawName:"v-show",value:e.isExecuted,expression:"isExecuted"}]},[r("div",{style:e.watchResponseHeaderColor,attrs:{id:"response-heander"}},[r("div",{directives:[{name:"show",rawName:"v-show",value:0!=e.responseSucceeded||0!=e.responseFailed,expression:"responseSucceeded != 0 || responseFailed != 0"}],staticClass:"response-header-box"},[r("div",{staticClass:"response-header-label"},[r("b",[e._v(e._s(e.$t("executeProgress")))])]),r("div",{staticClass:"response-header-body"},[r("div",{staticClass:"sm-margin-left-0x",staticStyle:{"line-height":"34px"}},[e._v(e._s(e.$t("succee"))+": ")]),r("div",{staticStyle:{color:"#28a745","font-size":"36px"}},[e._v(e._s(e.responseSucceeded))]),r("div",{staticClass:"small"},[e._v(e._s(e.$t("fail"))+": ")]),r("div",{staticStyle:{color:"#dc3545","font-size":"36px"}},[e._v(e._s(e.responseFailed))]),r("div",{staticClass:"small"},[e._v("\n\t\t\t\t\t\t\t\t "+e._s(e.responseSucceeded+e.responseFailed)+" / "+e._s("HTTP"==e.requestData.requestType?e.requestData.count*e.requestData.average:e.requestData.count)+"\n\t\t\t\t\t\t\t")])])]),r("div",{directives:[{name:"show",rawName:"v-show",value:e.responseTimeCount.length>0,expression:"responseTimeCount.length > 0"}],staticClass:"response-header-box"},[r("div",{staticClass:"response-header-label",staticStyle:{"align-items":"flex-start"}},[r("b",{staticStyle:{"line-height":"34px"}},[e._v(e._s(e.$t("responseTimesCount")))])]),r("div",{staticClass:"response-header-body",staticStyle:{"flex-direction":"column","align-items":"flex-start"}},e._l(e.responseTimeCount,(function(t,s){return r("div",{key:s,staticStyle:{display:"flex","flex-wrap":"wrap"}},[r("div",{staticClass:"small sm-margin-left-0x"},[e._v(e._s(e.$t("responseCode"))+":"+e._s(t.code))]),r("div",{staticClass:"small"},[e._v(e._s(e.$t("responseCount"))+":"+e._s(t.count))]),r("div",{staticClass:"small"},[e._v(e._s(e.$t("responseTotalTime"))+":"+e._s(t.total)+"ms")]),r("div",{staticClass:"small"},[e._v(e._s(e.$t("responseMaxTime"))+":"+e._s(t.max)+"ms")]),r("div",{staticClass:"small"},[e._v(e._s(e.$t("responseMeanTime"))+":"+e._s(t.mean)+"ms")])])})),0)]),r("div",{directives:[{name:"show",rawName:"v-show",value:0!=e.requestDataLen||0!=e.responseDataLen,expression:"requestDataLen != 0 || responseDataLen != 0"}],staticClass:"response-header-box"},[r("div",{staticClass:"response-header-label"},[r("b",[e._v(e._s(e.$t("dataLenInfo")))])]),r("div",{staticClass:"response-header-body"},[r("div",{staticStyle:{display:"flex","flex-wrap":"wrap"}},[r("div",{staticClass:"small sm-margin-left-0x"},[e._v(e._s(e.$t("requestDataLen"))+":"+e._s(e.requestDataLen)+" byte")]),r("div",{staticClass:"small"},[e._v(e._s(e.$t("responseDataLen"))+":"+e._s(e.responseDataLen)+" byte")])])])]),r("div",{directives:[{name:"show",rawName:"v-show",value:null!=e.serverMetrics&&0!=e.serverMetrics.processors,expression:"serverMetrics != null && serverMetrics.processors != 0"}],staticClass:"response-header-box"},[r("div",{staticClass:"response-header-label"},[r("b",[e._v(e._s(e.$t("serverMemory")))])]),r("div",{staticClass:"response-header-body"},[r("div",{staticStyle:{display:"flex","flex-wrap":"wrap"}},[r("div",{staticClass:"small sm-margin-left-0x"},[e._v(e._s(e.$t("serverProcessors"))+":"+e._s(e.serverMetrics.processors))]),r("div",{staticClass:"small"},[e._v(e._s(e.$t("serverMaxMemory"))+":"+e._s(e.serverMetrics.maxMemory)+" m")]),r("div",{staticClass:"small"},[e._v(e._s(e.$t("serverTotalMemory"))+":"+e._s(e.serverMetrics.totalMemory)+" m")]),r("div",{staticClass:"small"},[e._v(e._s(e.$t("serverFreeMemory"))+":"+e._s(e.serverMetrics.freeMemory)+" m")])])])])]),r("div",{ref:"responseConsoleBody",attrs:{id:"response-body"}})])],1)],1)],1)},o=[],n=(s("8ea5"),s("4f37"),s("6b54"),s("87b3"),s("78ce"),s("57e7"),s("a481"),"HTTP"),l="WebSocket",i="TCP",c="DEFAULT",u="INFO",p="SUCCESS",d="ERROR",m=1,v=2,f=3,q=4,h=99,y=412,b=1e3,T={name:"Orion-Stress-Tester",data:function(){return{isStatistics:!1,statisticsInfo:"",isExecuting:!1,isExecuted:!1,requestConfig:{printResInfo:!0,keepAlive:!0,poolSize:null,timeout:null},requestData:{requestType:n,webSocketVersion:"V13",subProtocols:[],serverName:"",isSSL:!1,cert:"DEFAULT",certKey:null,certValue:null,host:"",port:null,method:"GET",headers:[],url:"",body:null,count:null,average:null,interval:null},requestDataLen:0,responseDataLen:0,responseSucceeded:0,responseFailed:0,responseTimeCount:[],serverMetrics:{processors:0,totalMemory:0,maxMemory:0,freeMemory:0},websocket:null}},methods:{isShowStatistics:function(){this.requestData.count>0&&this.requestData.average>0&&this.requestData.interval>0?(this.isStatistics=!0,this.statisticsInfo=(this.requestData.requestType==n?this.$t("statisticsInfo"):this.$t("statisticsWsTcpInfo")).replace("{interval}",this.requestData.interval).replace("{average}",this.requestData.average).replace("{count}",this.requestData.count).replace("{conn}",null==this.requestConfig.poolSize?this.requestData.count:this.requestConfig.poolSize).replace("{sum}",this.requestData.count*this.requestData.average)):this.isStatistics=!1},removeSubProtocols:function(e){var t=this;this.$confirm("确认关闭？").then((function(s){var r=t.requestData.subProtocols.indexOf(e);-1!==r&&t.requestData.subProtocols.splice(r,1)})).catch((function(e){}))},addSubProtocols:function(){this.requestData.subProtocols.push({value:"",key:Date.now()})},removeHeader:function(e){var t=this;this.$confirm("确认关闭？").then((function(s){var r=t.requestData.headers.indexOf(e);-1!==r&&t.requestData.headers.splice(r,1)})).catch((function(e){}))},addHeader:function(){this.requestData.headers.push({key:"",value:""})},loadCertKey:function(){var e=new FileReader,t=this.$refs.readCertKey.files[0];e.readAsText(t);var s=this;e.onload=function(e){try{s.requestData.certKey=e.target.result}catch(t){console.log(t)}}},loadCertValue:function(){var e=new FileReader,t=this.$refs.readCertValue.files[0];e.readAsText(t);var s=this;e.onload=function(e){try{s.requestData.certValue=e.target.result}catch(t){console.log(t)}}},clearToDefaultData:function(){this.requestDataLen=0,this.responseDataLen=0,this.responseSucceeded=0,this.responseFailed=0,this.responseTimeCount=[]},toFixed:function(e,t){var s=e.toString().indexOf(".");return-1!=s&&e.toString().substring(s).length>t?e.toFixed(t):e},execute:function(){var e=this;this.$refs.requestTable.validate((function(t){if(t){e.isExecuting=!0,e.isExecuted=!0;var s=e.$t("consoleConnecting");e.consolePrintInfo(u,s);var r="wlhost";null!=r&&"wlhost"!=r||(r=window.location.host),e.websocket=new WebSocket("ws://"+r+"/ws/ost"),e.websocket.onopen=function(){var t=e.$t("consoleConnected");e.consolePrintInfo(p,t);var s=e.requestData;e.clearToDefaultData();var r={type:s.requestType,url:s.url.trim(),count:s.count,average:s.average,interval:s.interval};if(s.isSSL&&(r.isSSL=!0,s.cert==c?r.cert=c:null!=s.certKey&&""!=s.certKey.trim()&&null!=s.certValue&&""!=s.certValue.trim()&&(r.cert=s.cert,r.certKey=s.certKey.trim(),r.certValue=s.certValue.trim())),s.requestType==l&&(r.webSocketVersion=s.webSocketVersion,s.subProtocols.length>0)){for(var a=[],o=0;o<s.subProtocols.length;o++){var u=s.subProtocols[o].value;null!=u&&""!=u.trim()&&a.push(u.trim())}a.length>0&&(r.subProtocols=a)}if(s.requestType==i&&(r.host=s.host.trim(),""==s.port||s<0||s>65535||isNaN(parseInt(s.port))?r.port=s.isSSL?443:80:r.port=parseInt(s.port),null!=s.serverName&&""!=s.serverName.trim()&&(r.serverName=s.serverName.trim())),s.requestType==n&&(r.method=s.method),null!=s.headers&&0!=s.headers.length&&s.requestType!=i){var d=[];for(o=0;o<s.headers.length;o++){var v=s.headers[o];""!=v.key.trim()&&""!=v.value.trim()&&d.push(v.trim())}d.length>0&&(r.headers=d)}if(null!=s.body&&""!=s.body.trim()&&(r.body=s.body.trim()),r.keepAlive=e.requestConfig.keepAlive,r.keepAlive){var f=e.requestConfig.poolSize;(null==f||0==f||""==f||isNaN(parseInt(f)))&&(f=s.count),r.poolSize=parseInt(f)}null!=e.requestConfig.timeout&&e.requestConfig.timeout>0&&(r.timeout=parseInt(e.requestConfig.timeout)),r.printResInfo=e.requestConfig.printResInfo,console.log(r);var q={code:m,data:r};e.websocket.send(JSON.stringify(q))},e.websocket.onerror=function(t){console.log("Connection error:"),console.log(t),e.isExecuting=!1,e.consolePrintInfo(d,e.$t("consoleConnectFailed"))},e.websocket.onclose=function(){e.isExecuting=!1,console.log(e.$t("consoleClosed"))},e.websocket.onmessage=function(t){var s=JSON.parse(t.data);if(s.code==y)e.consolePrintInfo(d,e.$t("commandInvalidParameter")+s.msg),e.websocket.close();else if(s.code==v)1==s.data?e.consolePrintInfo(p,e.$t("commandBeforeRequestTestSucceeded")):(e.consolePrintInfo(d,e.$t("commandBeforeRequestTestFailed")+s.msg),e.websocket.close());else if(s.code==b){var r=s.data;e.serverMetrics.processors=r.processors,e.serverMetrics.totalMemory=e.toFixed(r.totalMemory/1048576,5),e.serverMetrics.maxMemory=e.toFixed(r.maxMemory/1048576,5),e.serverMetrics.freeMemory=e.toFixed(r.freeMemory/1048576,5)}else if(s.code==q){var a=s.data,o=1==a.state?e.$t("succee"):e.$t("fail"),n=e.$t("commandTestResponseCount").replace("{count}",a.count);0!=a.index&&(n+=", "+e.$t("commandTestResponseIndex").replace("{index}",a.index)),n+=", "+e.$t("commandTestResponseState").replace("{state}",o),n+=", "+e.$t("commandTestResponseCode").replace("{code}",a.code),null!=a.body&&(n+=", "+e.$t("commandTestResponseBody").replace("{body}",a.body)),e.requestConfig.printResInfo&&e.consolePrintInfo(u,n)}else if(s.code==f){a=s.data;var l=a["vertx.http.client.bytesReceived"];if(null==l&&(l=a["vertx.net.client.bytesReceived"]),null!=l&&l.length>0){for(var i=0,c=0;c<l.length;c++)i+=l[c].total;e.responseDataLen=i}var m=a["vertx.http.client.bytesSent"];if(null==m&&(m=a["vertx.net.client.bytesSent"]),null!=m&&m.length>0){for(i=0,c=0;c<m.length;c++)i+=m[c].total;e.requestDataLen=i}var T=a["vertx.http.client.responseTime"];if(null!=T&&T.length>0){e.responseTimeCount=[];for(c=0;c<T.length;c++){var g=T[c],C={};C.code=g.tags.code,C.count=g.count,C.total=e.toFixed(g.totalTimeMs,5),C.max=e.toFixed(g.maxMs,5),C.mean=e.toFixed(g.meanMs,5),e.responseTimeCount.push(C)}}e.responseSucceeded=a.succeeded,e.responseFailed=a.failed}else s.code==h&&e.consolePrintInfo(p,e.$t("commandTestComplete"))},e.$nextTick((function(){document.documentElement.scrollTop=document.body.scrollHeight}))}}))},executeCancel:function(){this.isExecuting=!1,null!=this.websocket&&this.websocket.close()},consolePrintInfo:function(e,t){var s=this,r=(new Date).toISOString().replace("T"," ").replace("Z",""),a=document.createElement("div");"SUCCESS"==e?a.style.color="#28a745":"ERROR"==e&&(a.style.color="#dc3545");var o=document.createElement("p");o.style.marginBottom="0",o.innerText="[ "+e+" ] "+r,a.appendChild(o);var n=document.createElement("p");n.style.marginTop="3px",n.innerText=t,a.appendChild(n),document.getElementById("response-body").appendChild(a),this.$nextTick((function(){s.$refs.responseConsoleBody.scrollTop=s.$refs.responseConsoleBody.scrollHeight}))}},watch:{"requestData.url":function(e){-1!=e.indexOf("https://")&&this.requestData.requestType==n?this.requestData.isSSL=!0:-1!=e.indexOf("wss://")&&this.requestData.requestType==l?this.requestData.isSSL=!0:"TCP"!=this.requestData.requestType&&(this.requestData.isSSL=!1)},"requestData.requestType":function(){-1!=this.requestData.url.indexOf("https://")&&this.requestData.requestType==n?this.requestData.isSSL=!0:-1!=this.requestData.url.indexOf("wss://")&&this.requestData.requestType==l?this.requestData.isSSL=!0:this.requestData.requestType!=i&&(this.requestData.isSSL=!1)},"requestData.count":function(){this.requestData.count*this.requestData.average>1e4?this.requestConfig.printResInfo=!1:this.requestConfig.printResInfo=!0,this.isShowStatistics()},"requestData.average":function(){this.requestData.count*this.requestData.average>1e4?this.requestConfig.printResInfo=!1:this.requestConfig.printResInfo=!0,this.isShowStatistics()},"requestData.interval":function(){this.isShowStatistics()},"requestConfig.poolSize":function(){this.isShowStatistics()}},computed:{watchResponseHeaderColor:function(){return this.responseSucceeded>this.responseFailed?"background-color:#e8f6f0;":this.responseSucceeded<this.responseFailed?"background-color:#fae7e7;":""}}},g=T,C=(s("034f"),s("2877")),x=Object(C["a"])(g,a,o,!1,null,null,null),S=x.exports,D=s("5c96"),P=s.n(D),_=(s("0fae"),s("1c01"),s("58b2"),s("8e6e"),s("f3e2"),s("d25f"),s("ac6a"),s("456d"),s("bd86")),$=s("a925"),k=s("a78e"),w=s.n(k),R=s("b2d6"),I=s.n(R),M=s("f0d9"),E=s.n(M),O={requestType:"Request type",select:"Choose",requestUrl:"Request URL",requestTips:"Request  cannot be empty",httpUrlPlaceholder:"Request URL,for example http://127.0.0.1/test?id=1",httpUrlInvalidTips:"Invalid request URL, must be start with http:// or https:// ",websocketUrlPlaceholder:"Request URL,for example ws://127.0.0.1/test?id=1",websocketUrlInvalidTips:"Invalid request URL, must be start with ws:// or wss://",tcpHostTips:"Request host cannot be empty",tcpHostPlaceholder:"Request host,for example test.com 或 127.0.0.1",tcpPortPlaceholder:"Request port,default(80/443)",certSetting:"Certificate",certDefault:"Use default",certPfx:"Use PFX",certPem:"Use PEM",certJks:"Use JKS",certKey:"Please enter certificate key or password",certValue:"Please enter certificatevalue",btnReadCertKey:"Load key",btnReadCertValue:"Load value",requestServerNamePlaceholder:"Please enter the Server name",requestHeaders:"headers",inputKey:"Please enter key",inputName:"Please enter name",inputValue:"Please enter value",requestBody:"Body data",requestBodyPlaceholder:"Please enter the body content to be sent",requestCount:"Request settings",requestCountPlaceholder:"Total requests",requestTcpCountPlaceholder:"Total connections",requestCountTips:"Must be greater than 0",requestAveragePlaceholder:"Requests times",requestTcpAveragePlaceholder:"Body sent times",requestAverageTips:"Must be greater than 0",requestIntervalPlaceholder:"Time interval per request (MS)",requestTcpIntervalPlaceholder:"Time interval to send(MS)",requestIntervalTips:"Interval must be greater than 1 ms",statisticsInfo:"Request {average} times every {interval} ms, {count} times in total, max connection {conn} , cumulative request {sum} times",statisticsWsTcpInfo:"Open {count} connections, send requests {average} times per connection, Every request is {interval} ms apart, cumulative request {sum} times",requestConfigPrintInfo:"Print response log",requestConfigPrintInfoTips:"A large number of request printing response logs, very test the performance of the browser, please choose carefully!",requestConfigTimeout:"Timeout(ms)",requestConfigKeepAlive:"Keep alive",requestConfigPoolSize:"Max  connections, default is Total requests",btnExecute:"Execute",btnIsExecute:"Executing...",btnCancel:"Cancel",btnAdd:"Add",btnRemove:"Remove",succee:"succee",fail:"fail",executeProgress:"Progress",responseFailedCount:"Failures",responseTimesCount:"Statistics",responseCode:"Status code",responseCount:"Times",responseTotalTime:"累计用时",responseMaxTime:"Max time",responseMeanTime:"Mean time",dataLenInfo:"Bytes statistics",requestDataLen:"Request data length",responseDataLen:"Response data length",serverMemory:"Console performance",serverProcessors:"Processors",serverMaxMemory:"Max memory",serverTotalMemory:"Total memory",serverFreeMemory:"Free memory",consoleConnecting:"Connecting to console...",consoleConnected:"Successfully connected to console!",consoleConnectFailed:"Failed to connect to the console. Please check the browser console for more information!",consoleClosed:"Console closed!",commandTestResponseCount:"Request {count}",commandTestResponseIndex:"The {index} time",commandTestResponseState:"state:{state}",commandTestResponseCode:"Status code:{code}",commandTestResponseConnTime:"Connection time:{connTime} ms",commandTestResponseEndTime:"Response time:{endTime} ms",commandTestResponseBody:"Response:{body}",commandInvalidParameter:"Invalid request parameter or missing required parameter, console response information:",commandBeforeRequestTestSucceeded:"Pre test for request succeeded, test in progress...",commandBeforeRequestTestFailed:"Pre test  request failed: please check if your service is available, console response information:",commandTestComplete:"Test completed!"},L={requestType:"请求类型",select:"请选择",requestUrl:"请求地址",requestTips:"请求地址不能为空",httpUrlPlaceholder:"请输入请求路径,比如 http://127.0.0.1/test?id=1",httpUrlInvalidTips:"无效的请求地址,必须以http://或https://开头,主机不能为空",websocketUrlPlaceholder:"请输入WebSocket的路径,比如 ws://127.0.0.1/test?id=1",websocketUrlInvalidTips:"无效的请求地址,必须以ws://或wss://开头,主机不能为空",tcpHostTips:"请求主机地址不能为空",tcpHostPlaceholder:"请输入请求主机地址,比如 test.com 或 127.0.0.1",tcpPortPlaceholder:"请输入端口号,默认(80/443)",certSetting:"证书设置",certDefault:"使用默认证书",certPfx:"使用pfx证书",certPem:"使用pem证书",certJks:"使用jks证书",certKey:"请输入证书的key或password",certValue:"请输入证书的value",btnReadCertKey:"加载key",btnReadCertValue:"加载value",requestServerNamePlaceholder:"请输入主机名称",requestHeaders:"header内容",inputKey:"请输入key",inputName:"请输入name",inputValue:"请输入value",requestBody:"请求内容",requestBodyPlaceholder:"请输入需要发送的内容",requestCount:"请求设置",requestCountPlaceholder:"请求总次数",requestTcpCountPlaceholder:"总连接数",requestCountTips:"必须大于0",requestAveragePlaceholder:"每次请求数量",requestTcpAveragePlaceholder:"发送内容次数",requestAverageTips:"必须大于0",requestIntervalPlaceholder:"每次请求的时间间隔(毫秒)",requestTcpIntervalPlaceholder:"发送内容的时间间隔(毫秒)",requestIntervalTips:"间隔必须大于1毫秒",statisticsInfo:"每{interval}毫秒请求{average}次, 共执行{count}次,最大建立连接{conn}, 累计请求{sum}次",statisticsWsTcpInfo:"建立连接{count}, 每个连接发送{average}次请求, 每次请求间隔{interval}毫秒, 累计发送请求{sum}次",requestConfigPrintInfo:"打印响应日志",requestConfigPrintInfoTips:"大量的请求打印响应日志,很考验浏览器的性能,请谨慎选择!",requestConfigTimeout:"请求超时(ms)",requestConfigKeepAlive:"控制台与URL服务器保持连接",requestConfigPoolSize:"最大建立连接数,默认为请求总次数",btnExecute:"开始执行",btnIsExecute:"正在执行中...",btnCancel:"取消",btnAdd:"添加",btnRemove:"删除",succee:"成功",fail:"失败",executeProgress:"执行进度",responseFailedCount:"失败数量",responseTimesCount:"统计信息",responseCode:"状态码",responseCount:"数量",responseTotalTime:"累计用时",responseMaxTime:"最高用时",responseMeanTime:"平均用时",dataLenInfo:"数据统计",requestDataLen:"请求数据长度",responseDataLen:"响应数据长度",serverMemory:"控制台性能",serverProcessors:"处理器数量",serverMaxMemory:"最大内存",serverTotalMemory:"可用内存",serverFreeMemory:"剩余内存",consoleConnecting:"正在连接控制台...",consoleConnected:"连接控制台成功!",consoleConnectFailed:"连接控制台失败,更多信息请查看浏览器控制台!",consoleClosed:"控制台已关闭!",commandTestResponseCount:"第{count}批请求",commandTestResponseIndex:"第{index}次",commandTestResponseState:"状态:{state}",commandTestResponseCode:"状态码:{code}",commandTestResponseConnTime:"连接用时:{connTime} ms",commandTestResponseEndTime:"响应用时:{endTime} ms",commandTestResponseBody:"响应信息:{body}",commandInvalidParameter:"无效的请求参数或缺失必填的参数,控制台响应信息:",commandBeforeRequestTestSucceeded:"进行请求前测成功,正在进行测试...",commandBeforeRequestTestFailed:"进行请求前测试失败:请检查你的服务是否可用,控制台响应信息:",commandTestComplete:"操作已完成!"};function F(e,t){var s=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),s.push.apply(s,r)}return s}function V(e){for(var t=1;t<arguments.length;t++){var s=null!=arguments[t]?arguments[t]:{};t%2?F(Object(s),!0).forEach((function(t){Object(_["a"])(e,t,s[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(s)):F(Object(s)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(s,t))}))}return e}r["default"].use($["a"]);var A={en:V({},O,{},I.a),zh:V({},L,{},E.a)};function N(){var e=w.a.get("language");if(e)return e;for(var t=(navigator.language||navigator.browserLanguage).toLowerCase(),s=Object.keys(A),r=0,a=s;r<a.length;r++){var o=a[r];if(t.indexOf(o)>-1)return o}return"zh"}var H=new $["a"]({locale:N(),messages:A}),U=H;r["default"].config.productionTip=!1,r["default"].use(P.a,{i18n:function(e,t){return U.t(e,t)}}),new r["default"]({el:"#app",i18n:U,render:function(e){return e(S)}})},"64a9":function(e,t,s){},cf05:function(e,t,s){e.exports=s.p+"img/logo.9059a594.png"}});
//# sourceMappingURL=app.493f2ef3.js.map