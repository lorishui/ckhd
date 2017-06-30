<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>强更APP版本</title>
<meta name="decorator" content="default"/>
<script type="text/javascript">
	function query(){
		$("#inputForm").attr("action","${ctx}/app/appversion/list");
		$("#inputForm").submit();
		return false;
	}
	$(document).ready(function() {
		changeAPPID(true);
		$("#ckAppId").focus();
		$("#inputForm").validate({
			submitHandler: function(form){
				form.submit();
			},
			errorContainer: "#messageBox"
		});
	});
	
	function changeAPPID(pageLoad){
		$.ajax({
		    url:'${ctx}/stats/queryAppCarriers',
		    type:'get',
		    data:'ckAppId=' + $("#ckAppId").val() + '&carriersType=MM',
		    error:function(){
		    },
		    success:function(data){
		    	$("#mmAppId").empty();
		    	$("#mmAppId").append("<option value='' selected>(全部)</option>");
		       for(var item in data){
		       	$("#mmAppId").append("<option value='"+data[item].appId +"'>"+data[item].appName + "(" + data[item].appId +")</option>");
		       }
		       if(pageLoad){
		    	   $("#mmAppId").val("${appversion.mmAppId}");
				   $(".select2-chosen")[1].innerHTML = $("#mmAppId option:selected").text();
		       }else{
		    	   $("#mmAppId").val("");
			       $(".select2-chosen")[1].innerHTML = "(全部)";
		       }
		    }
		});
	}
	function save(){
		$("#inputForm").attr("action","${ctx}/app/appversion/saveAppVersion");
		$("#inputForm").submit();
		return false;
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/app/appversion/list">强更APP版本列表</a></li>
		<shiro:hasPermission name="app:appversion:edit"><li><a href="${ctx}/app/appversion/form">强更APP版本添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="inputForm" name="inputForm" modelAttribute="appversion" action="${ctx}/app/appversion/list" method="post" class="form-horizontal">
		<div>
			<label>游戏：</label>
			 <span><font color="red">*</font> </span>
			<form:select id="ckAppId" path="ckAppId" class="input-medium" onChange="changeAPPID()">
				<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
		    </form:select>
			<form:select id="mmAppId" path="mmAppId" class="input-medium" style="width:300px">
		    </form:select>
		    <label>渠道：</label>
			<form:select id="ckChannelId" path="ckChannelId" class="input-medium">
				<form:option value="" label="(全部)"/>
				<form:options items="${fns:getCarriersChannelList('MM')}" itemLabel="name" itemValue="id" htmlEscape="false"/>
		    </form:select>
		    <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return query();"/>
		</div><br>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>渠道名称</th>
				<th>创酷Id</th>
				<th>mmId</th>
				<th>mm版本号</th>
				<th>和游戏版本号</th>
				<th>电信爱游戏版本号</th>
				<th>电信翼支付版本号</th>
				<th>联通沃支付版本号</th>
				<th>联通宽带版本号</th>
				<th>url</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="appVersionList" varStatus="status">
			<tr>
				<td>${status.count}</td>
				<td>${appVersionList.name}(${appVersionList.ckChannelId})</td>
				<td>${appVersionList.ckAppId}</td>
				<td>${appVersionList.mmAppId}</td>
				<td>${appVersionList.cmccMmVersion}</td>
				<td>${appVersionList.cmccAndgameVersion}</td>
				<td>${appVersionList.ctccEgameVersion}</td>
				<td>${appVersionList.ctccCteVersion}</td>
				<td>${appVersionList.cuccWoVersion}</td>
				<td>${appVersionList.cuccKdVersion}</td>
				<td>${appVersionList.url}</td>
				<shiro:hasPermission name="app:appversion:edit"><td><a href="${ctx}/app/appversion/update?id=${appVersionList.id}">${not empty appVersionList.id?'修改':'添加'}</a>
					<a href="${ctx}/app/appversion/delete?id=${appVersionList.id}" onclick="return confirmx('确认要删除该强更APP版本吗？', this.href)">删除</a></td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	</form:form>
</body>
</html>