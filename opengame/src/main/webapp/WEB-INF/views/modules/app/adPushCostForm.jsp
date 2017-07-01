<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/app/adPushCost/list?adPushDetailId=${adPushCost.adPushDetailId}">消耗列表</a></li>
		<li class="active"><a href="${ctx}/app/adPushCost/form?adPushDetailId=${adPushCost.adPushDetailId}">消耗<shiro:hasPermission name="app:adPushCost:edit">${not empty user.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="app:adPushCost:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="adPushCost" action="${ctx}/app/adPushCost/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="adPushDetailId"/>
		<sys:message content="${message}"/>
		
		<%-- 
		<div class="control-group">
			<label class="control-label">detailId:</label>
			<div class="controls">
				<form:input path="adPushDetailId" htmlEscape="false" maxlength="50"/>
			</div>
		</div> --%>
		<div class="control-group">
			<label class="control-label">消耗日期:</label>
			<div class="controls">
				<input id="date" name="date" type="text" maxlength="20" class="input Wdate" width="60"
				value="<fmt:formatDate value="${adPushCost.date}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">当日消耗（￥）:</label>
			<div class="controls">
				<form:input path="dayCost" htmlEscape="false" maxlength="50"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">当日收入（￥）:</label>
			<div class="controls">
				<form:input path="dayEarn" htmlEscape="false" maxlength="50"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">注册设备数:</label>
			<div class="controls">
				<form:input path="registNum" htmlEscape="false" maxlength="100"/>
			</div>
		</div>
		
		<div class="form-actions">
			<shiro:hasPermission name="app:adPushCost:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>