<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>计费点管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
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
		<li><a href="${ctx}/app/appcarriers/paycode/list">计费点列表</a></li>
		<li class="active"><a href="${ctx}/app/appcarriers/paycode/form?id=${paycode.id}">计费点<shiro:hasPermission name="app:appCarriers:edit">${not empty paycode.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="app:appCarriers:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="paycode" action="${ctx}/app/appcarriers/paycode/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">APP:</label>
			<div class="controls">
				 <form:select path="appId"  disabled="${not empty paycode.id?true:false}"  class="input-large">
					<form:options items="${fns:getAPPList()}" itemLabel="appName" itemValue="appId" htmlEscape="false"/>
				</form:select>  
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">运营商标识:</label>
			<div class="controls">
				<form:select path="carriersType"  disabled="${not empty paycode.id?true:false}"  class="input-large">
					<form:options items="${fns:getDictList('carriers')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">计费点编码:</label>
			<div class="controls">
				<form:input path="paycode" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">计费点名称:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">计费价格(单位分):</label>
			<div class="controls">
				<form:input path="price" htmlEscape="false" maxlength="50" class="required"  data-constraints='@Digits(integer=5, fraction=0)'/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">计费类型:</label>
			<div class="controls">
				<form:input path="paytype" htmlEscape="false" maxlength="50" />
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="app:appcarriers:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>