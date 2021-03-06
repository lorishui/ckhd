<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>sdk用户解除绑定</title>
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
		<li><a href="${ctx}/user/sdkUser/list">sdk用户列表</a></li>
		<li class="active"><a href="${ctx}/user/sdkUser/unbind?id=${userInfo.id}">sdk用户<shiro:hasPermission name="user:sdkUser:edit">解除绑定</shiro:hasPermission><shiro:lacksPermission name="user:sdkUser:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="userInfo" action="${ctx}/user/sdkUser/saveUnbind" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">用户账号:</label>
			<div class="controls">
				<input name="userAccount" htmlEscape="false" maxlength="100" class="required" value="${userInfo.userAccount }"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">绑定的邮箱:</label>
			<div class="controls">
				<input name="bindEmail" htmlEscape="false" maxlength="50" value="${userInfo.bindEmail }">
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">绑定的手机:</label>
			<div class="controls">
				<input name="bindMobile" htmlEscape="false" maxlength="50" value="${userInfo.bindMobile }">
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="form-actions">
			<shiro:hasPermission name="user:sdkUser:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>