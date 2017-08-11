<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>APP管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	$(document).ready(function() {
		changeAPPID(true);
		$("#ckAppId").focus();
	});
	function changeAPPID(pageLoad) {
		var id = '${buyFlowActivity.childckappId}';
		$.ajax({
			url : "${ctx}/app/appck/getChildIdList",
			type : 'get',
			data : 'ckappId=' + $("#ckAppId").val(),
			error : function() {
			},
			success : function(data) {
				var node = $("#childCkAppId");
				node.empty();
				var html = "<option value=\"\">全部</option>";
				for (var n = 0; n < data.length; n++) {
					html += "<option value='"+data[n].childId+"'>"
							+ data[n].name + "_" + data[n].childId
							+ "</option>";
				}
				node.append(html);
				node.val(id);
				var name = node.find("option:selected").text();
				if (id != null && id != '') {
					$(".select2-chosen")[1].innerHTML = name;
				}
			}
		});
	}
	function createUrl(){
		var ckappId = $("#ckAppId").val();
		if(ckappId == null || $.trim(ckappId) == ''){
			alert("请填写游戏名称");
			return false;
		}
		var childckappId = $("#childCkAppId").val();
	 	if(childckappId == null || $.trim(childckappId) == ''){
			alert("请填写子游戏名称");
			return false;
		}
		var adItem = $("#adItem").val();
		if(adItem == null || $.trim(adItem) == ''){
			alert("请填写推广点");
			return false;
		}
	/* 	$.ajax({
			url : "${ctx}/buyflow/activity/createUrl",
			type : 'post',
			data : 'ckappId='+ckappId+'&childckappId='+childckappId+'&adItem='+adItem,
			async: false,
			error : function() {
			},
			success : function(data) {
				var adUrl = $("#adUrl");
				adUrl.val(data)
			}
		}); */
		return true;
	}
	function sub() {
		if(!createUrl()){
			return false;
		};
		$("#inputForm").submit();
	}
	
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/buyflow/activity/list">活动列表</a></li>
		<li class="active"><a href="${ctx}/buyflow/activity/form?id=${buyFlowActivity.id}">活动<shiro:hasPermission name="buyflow:activity:edit">${not empty buyFlowActivity.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="buyflow:activity:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="buyFlowActivity" action="${ctx}/buyflow/activity/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
            <label class="control-label">设备类型:</label>
            <div class="controls">
                <form:select id="deviceType" path="deviceType" class="input-medium" style="width:162px">
                <form:option value="android" label="android" />
                <form:option value="ios" label="ios" />
            </form:select>
                <span class="help-inline">
                    <font color="red">*</font>
                </span>
            </div>
        </div>
		<div class="control-group">
			<label class="control-label">游戏名称:</label>
			<div class="controls">
				<form:select id="ckAppId" path="ckappId" class="input-medium" onChange="changeAPPID()" style="width:162px">
				<form:option value="" label="" />
				<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name" itemValue="ckappId" htmlEscape="false" />
			</form:select>
				<span class="help-inline">
					<font color="red">*</font>
				</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">子游戏名称:</label>
			<div class="controls">
				<form:select id="childCkAppId" path="childckappId" class="input-medium" style="width:162px">
				</form:select>
				<span class="help-inline">
					<font color="red">*</font>
				</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">活动名称:</label>
			<div class="controls">
				<form:textarea path="name" htmlEscape="false"  rows="5" cols="30"/>
				<span class="help-inline">
					<font color="red">批量添加时会在末尾加上   _推广点   以示区分*</font>
				</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">媒体平台:</label>
			<div class="controls">
				<form:select path="mediaId" class="input-medium" >
					<form:option value="" label="" />
				<form:options items="${fns:getDictList('adPush_media')}" itemLabel="label" itemValue="value" htmlEscape="false" />" itemLabel="lable" itemValue="value" htmlEscape="false" />
				</form:select>
				<span class="help-inline">
					<font color="red">*</font>
				</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">推广点:</label>
			<div class="controls">
				<form:input id="adItem" path="adItem" htmlEscape="false"  maxlength="50"/>
				<span class="help-inline">
					<font color="red">写成  1,2,3 可添加多个 (请使用英文逗号分隔)*</font>
				</span>
			</div>
		</div>
<%--    <div class="control-group">
			<label class="control-label">推广链接:</label>
			<div class="controls">
				<form:textarea id="adUrl" path="adUrl" htmlEscape="false"  rows="5" cols="30"/>&nbsp;&nbsp;&nbsp;
				<input class="btn btn-primary" type="button" value="生成链接" onclick="createUrl()"/>
				<span class="help-inline">
					<font color="red">修改以上内容后请重新生成链接</font>
				</span>
			</div>
		</div>                 --%>
		<div class="form-actions">
			<shiro:hasPermission name="buyflow:activity:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" onclick="return sub()"; value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>