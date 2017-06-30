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
			$("#ckappId").change(function(e){
				 var data = $("#ckappId").val();
				 $.ajax({
					 type: 'POST',
					  url:  "${ctx}/app/appck/getChildIdList",
					  data: "ckappId="+data,
					  datatype:"json",
					  success: function(data){
						 		var node = $("#childCkAppId");
						 		node.empty();
						 		var html = "<option value=\"\">全部</option>";
						 		var html = "";
						 		for(var n=0;n<data.length;n++){
						 			html+="<option value='"+data[n].childId+"'>"+data[n].name+"_"+data[n].childId+"</option>";
						 		}
						 		node.append(html);
						 		var id = '${onlinePay.childCkAppId}';
						 		node.val(id);
								var name = node.find("option:selected").text();
						 		if( id != null && id != '' ){
						 			$(".select2-chosen")[1].innerHTML = name;	
						 		}
							  }
					 });
			});
			
			var ckappid = $("#ckappId").val();
			 $.ajax({
				 type: 'POST',
				  url:  "${ctx}/app/appck/getChildIdList",
				  data: "ckappId="+ckappid,
				  datatype:"json",
				  success: function(data){
					 		var node = $("#childCkAppId");
					 		node.empty();
					 		var html = "<option value=\"#\">全部</option>";
					 		var html = "";
					 		for(var n=0;n<data.length;n++){
					 			html+="<option value='"+data[n].childId+"'>"+data[n].name+"_"+data[n].childId+"</option>";
					 		}
					 		node.append(html);
						  }
				 });
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/app/appPayList/list">支付方式列表</a></li>
		<li class="active"><a href="${ctx}/app/appPayList/form?id=${appPayList.id}">支付方式<shiro:hasPermission name="app:appPayList:edit">${not empty appPayList.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="app:appPayList:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="appPayList" action="${ctx}/app/appPayList/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">APP:</label>
			<div class="controls">
				 <form:select id="ckappId" path="ckAppId"  disabled="${not empty appPayList.id?true:false}"  class="input-medium">
				 	<form:option value="#" label="(全部)" />
					<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name" itemValue="ckappId"  htmlEscape="false"/>
				</form:select>  
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">子游戏id:</label>
			<div class="controls">
				 <select id="childCkAppId" name="childCkAppId" class="input-medium">
					<option value="#">全部</option>
				</select>  
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">渠道:</label>
			<div class="controls">
				<form:select id="channelId" path="ckChannelId" class="input-medium">
					<form:option value="#" label="(全部)" />
					<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
		    	</form:select>
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">支付方式:</label>
			<div class="controls">
				<c:forEach items="${paytype }" var="payType" varStatus="status">
					<c:set var="isExist" scope="session" value="0"/>
					<c:forEach items="${payTypeArr }" var="arr">
						<c:if test="${payType.value eq arr }">
						<input type="checkbox" name="paytype" value="${payType.value }" checked="checked" >${payType.label }
						<c:set var="isExist" scope="session" value="1"/>
						</c:if>
					</c:forEach>
					<c:if test="${isExist == 0 }">
						<input type="checkbox" name="paytype" value="${payType.value }" >${payType.label }
					</c:if>
					<c:if test="${status.index % 6 == 5 }">
						<br/>
					</c:if>
				</c:forEach>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否启用:</label>
			<div class="controls">
				<input type="checkbox" name="isUse" value="1" ${appPayList.isUse eq 1 ?'checked="checked"':'' }/>
				<span class="help-inline"><font color="red">选中代表是</font></span>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="app:appPayList:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>