<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>APP管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		changeAPPID(true);
		$("#ckAppId").focus();
		$("#searchForm").validate({
			submitHandler : function(form) {
				$("#searchForm").attr("action", "${ctx}/buyflow/activity/list");
				form.submit();
				merge();
			},
			errorContainer : "#messageBox"
		});
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
	function page(n, s) {
		if (n)
			$("#pageNo").val(n);
		if (s)
			$("#pageSize").val(s);
		$("#searchForm").attr("action", "${ctx}/buyflow/activity/list");
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/buyflow/activity/list">活动列表</a></li>
		<shiro:hasPermission name="buyflow:activity:edit">
			<li><a href="${ctx}/buyflow/activity/form">活动添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="buyFlowActivity" action="${ctx}/buyflow/activity/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();" />
		<label>游戏：</label>
		<form:select id="ckAppId" path="ckappId" class="input-medium" onChange="changeAPPID()">
			<form:option value="" label="(全部)" />
			<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name" itemValue="ckappId" htmlEscape="false" />
		</form:select>
		<form:select id="childCkAppId" path="childckappId" class="input-medium" style="width:250px">
		</form:select>
		<label>媒体平台:</label>
			<form:select path="mediaId" class="input-medium">
				<form:option value="" label="" />
				<form:options items="${fns:getDictList('adPush_media')}" itemLabel="label" itemValue="value" htmlEscape="false" />
			</form:select>&nbsp;&nbsp;&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable" class="table table-bordered table-condensed">
		<thead>
			<tr>
				<th>游戏名</th>
				<th>子游戏名</th>
				<th>设备平台</th>
				<th>活动名称</th>
				<th>媒体平台</th>
				<th>推广点</th>
				<th>推广链接</th>
				<th>创建时间</th>
				<shiro:hasPermission name="buyflow:activity:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="buyFlowActivity">
				<tr>
					<td>${fns:getByCkAppName(buyFlowActivity.ckappId)}(${buyFlowActivity.ckappId})</td>
					<td>${fns:getByChildAppName(buyFlowActivity.ckappId,buyFlowActivity.childckappId)}(${buyFlowActivity.childckappId})</td>
					<td>${buyFlowActivity.deviceType }</td>
					<td>${buyFlowActivity.name}</td>
					<td>${fns:getDictLabels(buyFlowActivity.mediaId,'adPush_media','')}</td>
					<td>${buyFlowActivity.adItem}</td>
					<td>${buyFlowActivity.adUrl}</td>
					<td><fmt:formatDate value="${buyFlowActivity.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<shiro:hasPermission name="buyflow:activity:edit">
						<td><a href="${ctx}/buyflow/activity/form?id=${buyFlowActivity.id}">修改</a> <a href="${ctx}/buyflow/activity/delete?id=${buyFlowActivity.id}" onclick="return confirmx('确认要删除该活动吗？', this.href)">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	<br />
</body>
</html>