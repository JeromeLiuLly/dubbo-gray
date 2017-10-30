<%--
  Created by IntelliJ IDEA.
  User: bieber
  Date: 2015/6/6
  Time: 8:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/codemirror/lib/codemirror.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/codemirror/mode/javascript/javascript.js"></script>
<script src="${pageContext.request.contextPath}/js/codemirror/addon/selection/active-line.js"></script>
<script src="${pageContext.request.contextPath}/js/codemirror/addon/edit/matchbrackets.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/esprima.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/jquery/jquery-2.1.4.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/jquery/jquery.fullscreen.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/cytoscape.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/angularjs/angular.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/angularjs/angular-route.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/angularjs/angular-animate.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/angularjs/angular-cookies.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/modules/common/filter.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/modules/common/util.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/modules/common/libs-mapping.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/angularjs-ui-tree/treeView.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/css/libs/themes/assets/js/bootswatch.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/showdown/showdown.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/showdown/showdown-table.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/mustache.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/datepicker/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/datepicker/locale/bootstrap-datepicker.zh-CN.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/multiselect/isteven-multi-select.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/jquery/jquery.fullscreen.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/bootstrap/bootstrap-switch.js"></script>

<script>
var editStrategyData = new Map();
var editUserData = new Map();

$(document).ready(function() {
	//获取数据库服务进行灰度操作信息
	$.ajax({
		url : "/eureka/apps/allStrategy",
		type : "GET",
		success : function(data) {
			if(data != null && data != ""){
				 getAllStrategy(data)
			}
		},
		error : function(data) {
			alert("服务异常");
		}
	})
	
	var getAllStrategy = function(data){
		var html;
		data.forEach(function(val, index, arr) {
			html += '<tr>';
			html += '<td>'+arr[index].id+'</td>';
			html += '<td>'+arr[index].strategyName+'</td>';
			html += '<td>'+arr[index].serviceId+'</td>';
			html += '<td>'+arr[index].serviceName+'</td>';
			html += '<td>'+arr[index].serviceTag+'</td>';
			html += '<td>'+arr[index].strategyValue+'</td>';
			var weight = arr[index].weight == null ? " " : arr[index].weight;
			html += '<td>'+weight+'</td>';
			html += '<td>'+arr[index].version+'</td>';
			var status = arr[index].status==1 ? "生效" : "关闭";
			html += '<td>'+status+'</td>';
			html += '<td>';
			var serviceTag;
			var serviceValue;
			if(arr[index].serviceTag == ""){
				serviceTag = "weight";
				serviceValue = weight;
			}else{
				serviceTag = arr[index].serviceTag;
				serviceValue = arr[index].strategyValue;
			}
			html += '<button type="button" onclick="synMetaDataStrategy(\''+arr[index].id+'\',\''+arr[index].serviceName+'\',\''+arr[index].serviceId+'\',\''+serviceTag +"="+serviceValue+'\')" class="btn btn-lg btn-success">同步</button>';
			html += '&nbsp;&nbsp;&nbsp;';
			html += '<button type="button" onclick="removeMetaDataStrategy(\''+arr[index].id+'\',\''+arr[index].serviceName+'\',\''+arr[index].serviceId+'\',\''+serviceTag +"="+serviceValue+'\')" class="btn btn-lg btn-danger">分离</button>';
			html += '&nbsp;&nbsp;&nbsp;';
			var result = arr[index].status==1 ? "关闭" : "开启";
			html += '<button type="button" onclick="opStrategyStatus(\''+arr[index].id+'\')" class="btn btn-lg btn-default">'+result+'</button>';
			html += '&nbsp;&nbsp;&nbsp;';
			editStrategyData[arr[index].id]=arr[index];
			html += '<button type="button" onclick="editStrategy(\''+arr[index].id+'\')" class="btn btn-lg btn-warning" >编辑</button>';
			html += '&nbsp;&nbsp;&nbsp;';
			html += '<button type="button" onclick="deleteStrategy(\''+arr[index].id+'\')" class="btn btn-lg btn-danger">删除</button>';
			html += '</td>';
			html += '</tr>';
		})
		
		$("#all-strategy").empty(); 
		$("#all-strategy").append(html);
	}
	
	//获取数据库服务进行灰度操作信息
	$.ajax({
		url : "/user/allUser",
		type : "GET",
		success : function(data) {
			if(data != null && data != ""){
				 getAllUser(data)
			}
		},
		error : function(data) {
			alert("服务异常");
		}
	})
	
	var getAllUser = function(data){
		var html;
		data.forEach(function(val, index, arr) {
			html += '<tr>';
			html += '<td>'+arr[index].id+'</td>';
			html += '<td>'+arr[index].userName+'</td>';
			html += '<td>'+arr[index].serviceTag+'</td>';
			html += '<td>'+arr[index].serviceValue+'</td>';
			html += '<td>'+arr[index].strategy+'</td>';
			var weight = arr[index].weight == null ? " " : arr[index].weight;
			html += '<td>'+weight+'</td>';
			var status = arr[index].status==1 ? "生效" : "关闭";
			html += '<td>'+status+'</td>';
			html += '<td>';
			var result = arr[index].status==1 ? "关闭" : "开启";
			html += '<button type="button" onclick="opUserStatus(\''+arr[index].id+'\')" class="btn btn-lg btn-default">'+result+'</button>';
			html += '&nbsp;&nbsp;&nbsp;';
			editUserData[arr[index].id]=arr[index];
			html += '<button type="button" onclick="editUser(\''+arr[index].id+'\')" class="btn btn-lg btn-warning" >编辑</button>';
			html += '&nbsp;&nbsp;&nbsp;';
			html += '<button type="button" onclick="deleteUser(\''+arr[index].id+'\')" class="btn btn-lg btn-danger">删除</button>';
			html += '</td>';
			html += '</tr>';
		})
		
		$("#all-user").empty(); 
		$("#all-user").append(html);
	}
	
	
	//获取灰度服务开关状态
	$.ajax({
		url : "/eureka/apps/switch",
		type : "GET",
		success : function(data) {
			if(data.statusCode == "200"){
				if(data.errorMsg == "true"){
					$(".switch-animate").removeClass("switch-off")
					$(".switch-animate").addClass("switch-on")
				}else{
					$(".switch-animate").removeClass("switch-on")
					$(".switch-animate").addClass("switch-off")
				}
			}else{
				alert(data.errorMsg)
			}
		},
		error : function(data) {
			alert("服务异常");
		}
	})
});

//===================================操作灰度服务列表模块开始============================//

//同步灰度服务标签到注册中心
var synMetaDataStrategy = function(id,applicationName,instanceId,tag){
	$.ajax({
		url : "/eureka/apps/"+id+"/"+applicationName+"/"+instanceId+"/synMetaDataStrategy?"+tag,
		type : "GET",
		success : function(data) {
			if(data.statusCode == "200"){
				alert("同步成功,请等待一分钟后信息生效");
			}else{
				alert(data.errorMsg)
			}
		},
		error : function(data) {
			alert("服务异常");
		}
	})
}

//分离灰度服务标签到注册中心
var removeMetaDataStrategy = function(id,applicationName,instanceId,tag){
	$.ajax({
		url : "/eureka/apps/"+id+"/"+applicationName+"/"+instanceId+"/removeMetaDataStrategy?"+tag,
		type : "GET",
		success : function(data) {
			if(data.statusCode == "200"){
				alert("分离成功,请等待一分钟后信息生效");
			}else{
				alert(data.errorMsg)
			}
		},
		error : function(data) {
			alert("服务异常");
		}
	})
}

//更新灰度服务的状态
var opStrategyStatus = function(id){
	$.ajax({
		url : "/eureka/apps/"+id+"/updateStrategy",
		type : "GET",
		success : function(data) {
			if(data.statusCode == "200"){
				alert(data.errorMsg);
				window.location.reload()
			}else{
				alert(data.errorMsg)
			}
		},
		error : function(data) {
			alert("服务异常");
		}
	})
}

//展示编辑灰度服务策略界面
var editStrategy = function(id){
	data = editStrategyData[id];
	$("#myModal").modal('show');
	$("#serviceName").attr("readonly","readonly");
	$("#instanceId").attr("readonly","readonly");
	$("#myModalLabel").html("编辑灰度服务")
	$("#id").val(data.id);
	$("#strategyName").val(data.strategyName);
	$("#serviceName").val(data.serviceName);
	$("#serviceId").val(data.serviceId);
	$("#weight").val(data.weight);
	$("#serviceTag").val(data.serviceTag);
	$("#strategyValue").val(data.strategyValue);
	$("#version").val(data.version);
	$("#status").find("option[value="+data.status+"]").attr("selected",true);
	$("#submitOP").attr("onclick","editStrategySubmit()");
}

//更新灰度服务信息
var editStrategySubmit = function(){
	var strategy=new Object();
	strategy.id = $("#id").val();
	strategy.strategyName = $("#strategyName").val();
	strategy.weight = $("#weight").val();
	strategy.serviceTag = $("#serviceTag").val();
	strategy.strategyValue = $("#strategyValue").val();
	strategy.version = $("#version").val();
	strategy.status = $("#status").val();
	$.ajax({
		url : "/eureka/apps/editStrategy",
		data: strategy,
		type : "POST",
		success : function(data) {
			if(data.statusCode == "200"){
				alert(data.errorMsg);
				window.location.reload()
			}else{
				alert(data.errorMsg)
			}
		},
		error : function(data) {
			alert("服务异常");
		}
	})
}

//展示新增灰度服务信息
var addStrategy = function(){
	$("#myModal").modal('show');
	$("#myModalLabel").html("新增灰度服务")
	$(".modal-body input").val("");
	$("#serviceName").removeAttr("readonly");
	$("#serviceId").removeAttr("readonly");
	$("#submitOP").attr("onclick","addStrategySubmit()");
}

//新增灰度服务信息
var addStrategySubmit = function(){
	var strategy=new Object();
	strategy.strategyName = $("#strategyName").val();
	strategy.weight = $("#weight").val();
	strategy.serviceTag = $("#serviceTag").val();
	strategy.strategyValue = $("#strategyValue").val();
	strategy.version = $("#version").val();
	strategy.status = $("#status").val();
	strategy.serviceName = $("#serviceName").val();
	strategy.serviceId = $("#serviceId").val();
	$.ajax({
		url : "/eureka/apps/addStrategy",
		data: strategy,
		type : "POST",
		success : function(data) {
			if(data.statusCode == "200"){
				alert(data.errorMsg);
				window.location.reload()
			}else{
				alert(data.errorMsg)
			}
		},
		error : function(data) {
			alert("服务异常");
		}
	})
}

//删除灰度服务
var deleteStrategy = function(id){
	$.ajax({
		url : "/eureka/apps/"+id+"/deleteStrategy",
		type : "GET",
		success : function(data) {
			if(data.statusCode == "200"){
				alert(data.errorMsg);
				window.location.reload()
			}else{
				alert(data.errorMsg)
			}
		},
		error : function(data) {
			alert("服务异常");
		}
	})
}
//===================================操作灰度服务列表模块结束============================//

//===================================操作灰度用户策略列表模块开始============================//
//更新用户灰度策略的状态
var opUserStatus = function(id){
	$.ajax({
		url : "/user/"+id+"/updateUser",
		type : "GET",
		success : function(data) {
			if(data.statusCode == "200"){
				alert(data.errorMsg);
				window.location.reload()
			}else{
				alert(data.errorMsg)
			}
		},
		error : function(data) {
			alert("服务异常");
		}
	})
}

//展示编辑灰度用户的界面
var editUser = function(id){
	data = editUserData[id];
	$("#myUserModal").modal('show');
	$("#userName").attr("readonly","readonly");
	$("#myUserModalLabel").html("编辑用户灰度策略")
	$("#id").val(data.id);
	$("#userName").val(data.userName);
	$("#weightUser").val(data.weight);
	$("#serviceTagUser").val(data.serviceTag);
	$("#serviceValueUser").val(data.serviceValue);
	$("#strategy").val(data.strategy);
	$("#statusUser").find("option[value="+data.status+"]").attr("selected",true);
	$("#submitUserOP").attr("onclick","editUserSubmit()");
}

//更新灰度用户信息
var editUserSubmit = function(){
	var user=new Object();
	user.id = $("#id").val();
	user.weight = $("#weightUser").val();
	user.serviceTag = $("#serviceTagUser").val();
	user.serviceValue = $("#serviceValueUser").val();
	user.strategy = $("#strategy").val();
	user.status = $("#statusUser").val();
	$.ajax({
		url : "/user/editUser",
		data: user,
		type : "POST",
		success : function(data) {
			if(data.statusCode == "200"){
				alert(data.errorMsg);
				window.location.reload()
			}else{
				alert(data.errorMsg)
			}
		},
		error : function(data) {
			alert("服务异常");
		}
	})
}

//删除用户灰度策略
var deleteUser = function(id){
	$.ajax({
		url : "/user/"+id+"/deleteUser",
		type : "GET",
		success : function(data) {
			if(data.statusCode == "200"){
				alert(data.errorMsg);
				window.location.reload()
			}else{
				alert(data.errorMsg)
			}
		},
		error : function(data) {
			alert("服务异常");
		}
	})
}

//展示添加灰度用户界面
var addUser = function(){
	$("#myUserModal").modal('show');
	$("#myUserModalLabel").html("新增用户灰度策略")
	$(".modal-body input").val("");
	$("#userName").removeAttr("readonly");
	$("#submitUserOP").attr("onclick","addUserSubmit()");
}

//更新灰度用户信息
var addUserSubmit = function(){
	var user=new Object();
	user.userName = $("#userName").val();
	user.weight = $("#weightUser").val();
	user.serviceTag = $("#serviceTagUser").val();
	user.serviceValue=$("#serviceValueUser").val();
	user.strategy=$("#strategy").val();
	user.status = $("#statusUser").val();
	$.ajax({
		url : "/user/addUser",
		data: user,
		type : "POST",
		success : function(data) {
			if(data.statusCode == "200"){
				alert(data.errorMsg);
				window.location.reload()
			}else{
				alert(data.errorMsg)
			}
		},
		error : function(data) {
			alert("服务异常");
		}
	})
}
//===================================操作灰度用户策略列表模块结束============================//


</script>