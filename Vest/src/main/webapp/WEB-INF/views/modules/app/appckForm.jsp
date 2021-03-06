<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>APP管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				rules: {
					name: {remote: "${ctx}/app/appck/checkName?oldName=" + encodeURIComponent('${appck.name}')},
					appid: {remote: "${ctx}/app/appck/checkAppId?oldAppId=" + encodeURIComponent('${appck.ckappId}')},
				},
				messages: {
					name: {remote: "APP名已存在"},
					appid: {remote: "APPID已存在"}
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
		
		function createSecretKey(){
			$.ajax({
				url : '${ctx}/app/appck/createSecretKey',
				type : 'post',
				error : function() {
				},
				success : function(data) {
					$("#secretKey").val(data);
				}
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/app/appck/list">APP列表</a></li>
		<li class="active"><a href="${ctx}/app/appck/form?id=${appck.id}">APP<shiro:hasPermission name="app:appck:edit">${not empty appck.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="app:appck:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="appck" action="${ctx}/app/appck/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">创酷appid:</label>
			<div class="controls">
				<input id="oldAppId" name="oldAppId" type="hidden" value="${appck.ckappId}">
				<form:input path="ckappId" htmlEscape="false"   readonly="${not empty appck.id?true:false}" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">APP名称:</label>
			<div class="controls">
				<input id="oldName" name="oldName" type="hidden" value="${appck.name}">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">子游戏id:</label>
			<div class="controls">
				<form:input path="childId" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">子APP名称:</label>
			<div class="controls">
				<input id="oldChildName" name="oldChildName" type="hidden" value="${appck.childName}">
				<form:input path="childName" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">cpid:</label>
			<div class="controls">
				<form:input path="cpid" htmlEscape="false"  maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">密钥:</label>
			<div class="controls">
				<form:input path="secretKey" htmlEscape="false"  maxlength="50"/>
				<shiro:hasPermission name="app:appck:edit">
				<input id="btnCreateSec" class="btn btn-primary" type="button"
						onclick="createSecretKey()" value="生成密钥" />
				</shiro:hasPermission>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">cpname:</label>
			<div class="controls">
				<form:input path="cpname" htmlEscape="false"  maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">折扣率:</label>
			<div class="controls">
				<form:input path="discount" htmlEscape="false"  maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">回调地址:</label>
			<div class="controls">
				<form:input path="payCallbackUrl" htmlEscape="false"  maxlength="200"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序:</label>
			<div class="controls">
				<form:input path="orderIndex" htmlEscape="false" rows="3" maxlength="200" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="app:appck:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>