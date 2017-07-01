<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>礼包配置信息管理</title>
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
			$("#ckAppId").change(function(){
				getGift();
			});
			getGift();
		});
		
		function getGift(){
			$.ajax({ url: "${ctx }/game/gift/getList?ckAppId="+$("#ckAppId").val(), success: function(data){
				goodsData = data;
				var gift = $("#giftId");
				gift.empty();
				html="<select id='giftId' name='giftId' class='input-medium'>";
				var n = '${giftDesc.giftId}';
				for(var i=0;i<data.length;i++){
					if(n==data[i].id){
						html+="<option value='"+data[i].id+"' selected=\"selected\" '>"+data[i].name+"</option>";
					}else{
						html+="<option value='"+data[i].id+"'>"+data[i].name+"</option>";	
					}
				}
				html+="</select>";
				gift.append(html);
			}});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/game/giftDesc/list">礼包配置信息列表</a></li>
		<li class="active"><a href="${ctx}/game/giftDesc/form?id=${giftDesc.id}">礼包配置信息<shiro:hasPermission name="game:giftDesc:edit">${not empty giftDesc.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="game:giftDesc:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="giftDesc" action="${ctx}/game/giftDesc/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">APP:</label>
			<div class="controls">
				 <form:select path="ckAppId" id="ckAppId" disabled="${not empty giftDesc.id?true:false}"  class="input-medium">
					<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
				</form:select>  
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">渠道：</label>
			<div class="controls">
				<form:select id="channelId" path="ckChannelId" class="input-medium">
					<form:option value="" label="(全部)" />
					<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
			   	</form:select>
		   	</div>
		</div>
		<div class="control-group">
			<label class="control-label">礼包：</label>
			<div class="controls" id="giftId">
		   	</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">数量:</label>
			<div class="controls">
				<form:input path="number" htmlEscape="false" maxlength="50" class="input-medium"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">批次:</label>
			<div class="controls">
				<form:input path="batch" htmlEscape="false" maxlength="50" class="input-medium"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">开始时间:</label>
			<div class="controls">
				<input id="startTime" name="startTime" type="text" readonly="readonly" maxlength="21" class="WdateTime required"
				value="${giftDesc.startTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">截止时间:</label>
			<div class="controls">
				<input id="endTime" name="endTime" type="text" readonly="readonly" maxlength="21" class="WdateTime required"
				value="${giftDesc.endTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="game:giftDesc:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>