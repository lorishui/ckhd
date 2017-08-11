<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>APP管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/app/adpushDetail/list?adPushId=${adPushId}">链接列表</a></li>
		<li class="active"><a href="${ctx}/app/adpushDetail/form?id=${adPushDetail.id}">链接<shiro:hasPermission name="app:adPushDetail:edit">${not empty adPushDetail.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="app:adPushDetail:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="adPushDetail" action="${ctx}/app/adpushDetail/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" name="adPush.id" value="${adPushId}" />
		<sys:message content="${message}"/>
		<%-- <div class="control-group">
			<label class="control-label">负责人:</label>
			<div class="controls">
				<form:input path="UserName" htmlEscape="false"  maxlength="50" readonly="readonly"/>
			</div>
		</div> --%>
		
		<div class="control-group">
			<label class="control-label">广告位:</label>
			<div class="controls">
				<form:input path="adPlace" htmlEscape="false"  maxlength="100"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">推广链接:</label>
			<div class="controls">
				<form:input path="adUrl" htmlEscape="false" maxlength="100"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">推广描述:</label>
			<div class="controls">
				<form:textarea path="description" htmlEscape="false"  maxlength="200"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="app:adPushDetail:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>