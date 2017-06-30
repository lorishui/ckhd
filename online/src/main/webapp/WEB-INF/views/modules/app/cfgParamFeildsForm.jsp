<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>参数显示配置</title>
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
		<li><a href="${ctx}/app/CfgParamFeilds/list">参数显示配置列表</a></li>
		<li class="active"><a href="${ctx}/app/CfgParamFeilds/form?id=${cfgParamFeilds.id}">参数显示添加</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="cfgParamFeilds" action="${ctx}/app/CfgParamFeilds/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">游戏:</label>
			<div class="controls">
				 <form:select path="ckAppId" class="input-medium">
				 	<form:option value="#" label="(全部)"/>
					<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
				</form:select>  
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">参数类型:</label>
			<div class="controls">
				<form:select path="type"   class="input-medium">
					<form:option value="" label="(全部)"/>
					<form:options items="${fns:getDictList('cfgtype')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">参数key:</label>
			<div class="controls">
				<form:input path="value" htmlEscape="false" maxlength="50" class="required" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">参数key的描述:</label>
			<div class="controls">
				<form:input path="label" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">参数值填写的详细描述:</label>
			<div class="controls">
				<form:input path="description" htmlEscape="false" maxlength="250" />
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">参数值类型的选择:</label>
			<div class="controls">
				<select name="classType" readonly="readonly" class="required">
					<option value=""  ${cfgParamFeilds eq null or cfgParamFeilds.classType eq null?'selected':''}>请选择</option>
					<option value="1" ${cfgParamFeilds ne null and cfgParamFeilds.classType==1?'selected':''} >int</option>
					<option value="2" ${cfgParamFeilds ne null and cfgParamFeilds.classType==2?'selected':''}>boolean</option>
					<option value="3" ${cfgParamFeilds ne null and cfgParamFeilds.classType==3?'selected':''}>JSONObject</option>
					<option value="4" ${cfgParamFeilds ne null and cfgParamFeilds.classType==4?'selected':''}>JSONArray</option>
					<option value="5" ${cfgParamFeilds ne null and cfgParamFeilds.classType==5?'selected':''}>String</option>
				</select>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">排序:</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" maxlength="250" class="required"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<%-- <shiro:hasPermission name="app:appcarriers:edit"> --%><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;<%-- </shiro:hasPermission> --%>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>