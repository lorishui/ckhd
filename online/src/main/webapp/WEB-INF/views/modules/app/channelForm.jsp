<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>渠道管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#channelId").focus();
			$("#inputForm").validate({
				rules: {
					name: {remote: "${ctx}/app/channel/checkName?oldName=" + encodeURIComponent('${channel.name}')}
				},
				messages: {
					name: {remote: "渠道商名称已存在"}
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
		<li><a href="${ctx}/app/channel/list">渠道列表</a></li>
		<li class="active"><a href="${ctx}/app/channel/form?id=${channel.id}">渠道<shiro:hasPermission name="app:channel:edit">${not empty channel.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="app:channel:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="channel" action="${ctx}/app/channel/save" method="post" class="form-horizontal">

		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">渠道ID:</label>
			<div class="controls">
				<input id="action" name="action" type="hidden" value="${channel.id}">
				<form:input path="channelId" htmlEscape="false" maxlength="10" class="required" readonly="${not empty channel.id?true:false}" value="${channel.id}" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">渠道名称:</label>
			<div class="controls">
				<input id="oldName" name="oldName" type="hidden" value="${channel.name}">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">SDK类型:</label>
			<div class="controls">
				<input id="oldEngName" name="oldEngName" type="hidden" value="${channel.engName}">
				<form:input path="engName" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">公司名:</label>
			<div class="controls">
				<form:input path="company" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">是否CPS:</label>
			<div class="controls">
				<form:select path="isCPS">
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="app:channel:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>