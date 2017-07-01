<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html> 
<html>
<head>
<title>模板生成</title>
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
	
	function add(n){
		console.log(n);
		var num = n.attr("id");
		var html = "<input id="+n+" type=\"file\" name=\"file"+num+"\" /><button onclick=\"addNode(this)\" value=\"+\"\/><br>";
		$("#add").app(append);
	};
</script>
<style type="text/css">
.add{
	left:70px;
}
</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/ad/adResource/templateList">模板列表</a></li>
		<li><a href="${ctx}/ad/adResource/generateTemplate">模板添加</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="updateVersion" action="${ctx}/ad/adResource/generate" method="post" enctype="multipart/form-data" class="form-horizontal">
		<div class="control-group">
			<label class="control-label">APP:</label>
			<div class="controls">
				 <form:select path="ckAppId"  disabled="${not empty updateVersion.ckAppId?true:false}"  class="input-medium">
				 	<form:option value="" label="(全部)" />
					<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
				</form:select>  
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">渠道:</label>
			<div class="controls">
				 <form:select id="channelId" path="ckChannelId" class="input-medium">
					<form:option value="" label="(全部)" />
					<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
		   		</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">上传的文件:</label>
			<div id="add" class="controls">
				<input id="0" type="file" name="file0" /> <!-- <button onclick="addNode(this)">+</button> --><br>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="生成模板"/>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>