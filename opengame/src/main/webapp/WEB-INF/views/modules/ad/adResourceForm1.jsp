<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>ad资源管理</title>
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
		<li><a href="${ctx}/ad/adResource/list1">ad资源列表</a></li>
		<li class="active"><a href="${ctx}/ad/adResource/form1?id=${adResource.id}">ad资源<shiro:hasPermission name="ad:adResource:edit">${not empty adResource.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="ad:adResource:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="adResource" action="${ctx}/ad/adResource/save1" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">广告主:</label>
			<div class="controls">
				 <form:select path="uid"  disabled="${not empty adResource.uid?true:false}"  class="input-medium">
					<form:option value="" label="(全部)" />
					<c:forEach var="user" items="${adUser }" varStatus="status">
						<form:option value="${user.uid}" label="${user.name}" />
					</c:forEach>
				</form:select>  
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">ad类型:</label>
			<div class="controls">
				<form:select id="adType" path="adType" class="input-mini" >
					<form:option value="" label="(全部)"/>
					<form:options items="${fns:getDictList('ad_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		    	</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">资源类型:</label>
			<div class="controls">
				<form:select id="resourceType" path="resourceType" class="input-mini" >
					<form:option value="" label="(全部)"/>
					<form:options items="${fns:getDictList('ad_resource_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		    	</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">资源格式:</label>
			<div class="controls">
				<form:input path="resourceFormat" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">资源url:</label>
			<div class="controls">
				<form:input path="url" htmlEscape="false" maxlength="500" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">尺寸:</label>
			<div class="controls">
				<form:input path="size" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">android触发地址:</label>
			<div class="controls">
				<form:input path="androidUrl" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">ios触发地址:</label>
			<div class="controls">
				<form:input path="iosUrl" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">触发结果格式:</label>
			<div class="controls">
				<form:input path="clickFormat" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">ios触发结果格式:</label>
			<div class="controls">
				<form:input path="iosClickFormat" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">使用结束时间:</label>
			<div class="controls">
				<input id="endTime" name="endDate" type="text" readonly="readonly" maxlength="21" class="WdateTime required"
				value="<fmt:formatDate value='${adResource.endDate}'  type='both'/>"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否 ios使用:</label>
			<div class="controls">
				<input type="checkbox" name="iosUse" value="1" ${adResource.iosUse ne null ?'checked="checked"':'' }/>
				<span class="help-inline"><font color="red">选中代表是</font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否 android使用:</label>
			<div class="controls">
				<input type="checkbox" name="androidUse" value="1" ${adResource.androidUse ne null ?'checked="checked"':'' }/>
				<span class="help-inline"><font color="red">选中代表是</font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否启用:</label>
			<div class="controls">
				<input type="checkbox" name="isUse" value="1" ${adResource.isUse ne null ?'checked="checked"':'' }/>
				<span class="help-inline"><font color="red">选中代表是</font></span>
			</div>
		</div>
		
		<div class="form-actions">
			<shiro:hasPermission name="ad:adResource:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html> 