<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>上传文件管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				rules: {
				},
				messages: {
				},
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/game/updateVersion/list">上传文件列表</a></li>
		<li class="active"><a href="${ctx}/game/updateVersion/form?id=${updateVersion.id}">上传文件<shiro:hasPermission name="game:updateVersion:edit">${not empty updateVersion.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="game:updateVersion:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="updateVersion" action="${ctx}/game/updateVersion/save" method="post" class="form-horizontal" enctype="multipart/form-data">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">APP:</label>
			<div class="controls">
				 <form:select path="ckAppId"  disabled="${not empty updateVersion.id?true:false}"  class="input-medium">
					<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
				</form:select>  
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">渠道：</label>
			<div class="controls">
				<form:select id="channelId" path="ckChannelId" class="input-medium">
					<form:option value="" label="(全部)" />
					<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
			   	</form:select>
		   	</div>
		</div>
		<div class="control-group">
			<label class="control-label">版本号:</label>
			<div class="controls">
				<form:input path="versionName" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">上传的文件:</label>
			<div class="controls">
				<input type="file" name="file" htmlEscape="false" maxlength="250" />
				<c:if test="${updateVersion.fileName ne null}">
					<label><font color="red">文件名:${updateVersion.fileName }</font></label>
				</c:if>
			</div>
		</div>
		
		<div class="form-actions">
			<shiro:hasPermission name="game:updateVersion:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>