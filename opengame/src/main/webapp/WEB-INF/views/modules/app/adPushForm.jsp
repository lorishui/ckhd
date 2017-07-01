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
		<li><a href="${ctx}/app/adpush/list">推广列表</a></li>
		<li class="active"><a href="${ctx}/app/adpush/form?id=${adPush.id}">推广<shiro:hasPermission name="app:adPush:edit">${not empty adPush.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="app:adPush:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="adPush" action="${ctx}/app/adpush/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">游戏名称:</label>
			<div class="controls">
				<form:select path="appId" class="input-medium" required="required">
					<form:option value="" label="" />
					<c:forEach var="game" items="${allGames}" varStatus="status">
						<form:option value="${game.appId}" label="${game.name}" />
					</c:forEach>
				</form:select>
				<span class="help-inline">
					<font color="red">*</font>
				</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">平台:</label>
			<div class="controls">
				<form:select path="platform" class="input-medium" >
					<form:option value="IOS" label="IOS" />
					<form:option value="Andriod" label="Andriod" />
				</form:select>
				<span class="help-inline">
					<font color="red">*</font>
				</span>
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label">媒体名称:</label>
			<div class="controls">
				<form:select path="mediaName" class="input-medium" >
					<form:option value="" label="" />
					<c:forEach var="media" items="${allMedias}" varStatus="status">
						<form:option value="${media.id}" label="${media.label}" />
					</c:forEach>
				</form:select>
				<span class="help-inline">
					<font color="red">*</font>
				</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">账户:</label>
			<div class="controls">
				<form:input path="account" htmlEscape="false"  maxlength="50"/>
			</div>
		</div>
		
		<div class="form-actions">
			<shiro:hasPermission name="app:adPush:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>