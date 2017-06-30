<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>运营商APP管理</title>
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
		<li><a href="${ctx}/app/appcarriers/list">运营商APP列表</a></li>
		<li class="active"><a href="${ctx}/app/appcarriers/form?id=${appCarriers.id}">运营商APP<shiro:hasPermission name="app:appCarriers:edit">${not empty appCarriers.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="app:appCarriers:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="appCarriers" action="${ctx}/app/appcarriers/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">APP:</label>
			<div class="controls">
				 <form:select path="ckappId"  disabled="${not empty appCarriers.id?true:false}"  class="input-medium">
					<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
				</form:select>  
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">运营商标识:</label>
			<div class="controls">
				<form:select path="carriersType"  disabled="${not empty appCarriers.id?true:false}"  class="input-medium">
					<form:options items="${fns:getDictList('carriers')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">运营商中APP号:</label>
			<div class="controls">
				<form:input path="appId" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label">运营商中APP名:</label>
			<div class="controls">
				<form:input path="appName" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label">下发CP服务地址:</label>
			<div class="controls">
				<form:textarea path="cpServerUrl" htmlEscape="false" maxlength="250" class="input-xlarge"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">导量商服务地址:</label>
			<div class="controls">
				<form:textarea path="flowServerUrl" htmlEscape="false" maxlength="250" class="input-xlarge"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">导量计费代码列表:</label>
			<div class="controls">
				<form:textarea path="paycodes" htmlEscape="false" maxlength="250" class="input-xlarge"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">指定渠道转发:</label>
			<div style="color:red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;例子：url1@2200165911,2200127261;url2@2200192421;ur3@ALL @后是转发渠道组，ALL表示全部渠道都转发</div>
			<div class="controls">
				<form:textarea path="forwardByChannel" htmlEscape="false" rows="3" maxlength="2500" class="input-xlarge"/>
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