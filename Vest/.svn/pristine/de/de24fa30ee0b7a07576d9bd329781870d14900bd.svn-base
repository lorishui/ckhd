<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>强更APP管理</title>
<meta name="decorator" content="default"/>
<script type="text/javascript">
	$(document).ready(function() {
		changeAPPID(true);
		$("#ckAppId").focus();
		$("#inputForm").validate({
			submitHandler: function(form){
				form.submit();
			},
			errorContainer: "#messageBox"
			 
		});
	});	
	function changeAPPID(pageLoad){
		$.ajax({
		    url:'${ctx}/stats/queryAppCarriers',
		    type:'get',
		    data:'ckAppId=' + $("#ckAppId").val() + '&carriersType=MM',
		    error:function(){
		    },
		    success:function(data){
		    	$("#mmAppId").empty();
		    	$("#mmAppId").append("<option value='' selected>(全部)</option>");
		       for(var item in data){
		       	$("#mmAppId").append("<option value='"+data[item].appId +"'>"+data[item].appName + "(" + data[item].appId +")</option>");
		       }
		       if(pageLoad){
		    	   $("#mmAppId").val("${appversion.mmAppId}");
				   $(".select2-chosen")[1].innerHTML = $("#mmAppId option:selected").text();
		       }else{
		    	   $("#mmAppId").val("");
			       $(".select2-chosen")[1].innerHTML = "(全部)";
		       }
		    }
		});
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/app/appversion/list">强更APP版本列表</a></li>
		<li class="active"><a href="${ctx}/app/appVersion/form?id=${appversion.id}">强更APP版本${not empty appversion.id?'修改':'添加'}</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="appversion" action="${ctx}/app/appversion/save" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<input id="id" name="id" type="hidden" value="${appversion.id}"/>
		<div class="control-group">
			<label class="control-label">游戏：</label>
			<div class="controls">
				<form:select id="ckAppId" path="ckAppId" class="input-medium" onChange="changeAPPID()">
					<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
		    	</form:select>
			<form:select id="mmAppId" path="mmAppId" class="input-medium" style="width:300px">
		    </form:select>
		    <span><font color="red">*</font> </span>
		    </div>
	    </div>
		<div class="control-group">
			<label class="control-label">渠道：</label>
			<div class="controls">
				<select id="ckChannelId" name="ckChannelId" class="input-medium">
			    	<c:forEach items="${map}" var="ver">
						<c:choose>
							<c:when test="${appversion.ckChannelId==ver.key}">
								<option value="${appversion.ckChannelId}" selected="selected">${ver.value}</option>
							</c:when>
							<c:otherwise>
								<option value="${ver.key}">${ver.value}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
		    	</select>
				<span class="help-inline"><font color="red">*</font></span>
				<c:choose>
					<c:when test="${ppVersionList.ckChannelId}==${ver.key}">
						<option value="${ver.key}" selected="selected">${ver.value}</option>
					</c:when>
					<c:otherwise>
						<option value="${ver.key}">${ver.value}</option>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">mm版本号:</label>
			<div class="controls">
				<form:input path="cmccMmVersion" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">和游戏版本号:</label>
			<div class="controls">
				<form:input path="cmccAndgameVersion" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">电信爱游戏版本号:</label>
			<div class="controls">
				<form:input path="ctccEgameVersion" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font></span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">电信翼支付版本号:</label>
			<div class="controls">
				<form:input path="ctccCteVersion" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">联通沃支付版本号:</label>
			<div class="controls">
				<form:input path="cuccWoVersion" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">联通宽带版本号:</label>
			<div class="controls">
				<form:input path="cuccKdVersion" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">url:</label>
			<div class="controls">
				<form:textarea path="url" htmlEscape="false" maxlength="250" class="input-xlarge"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="back" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>