<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>运营商渠道管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/app/channelcarriers/list");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/app/channelcarriers/list">运营商渠道列表</a></li>
		<shiro:hasPermission name="app:channelcarriers:edit"><li><a href="${ctx}/app/channelcarriers/form">运营商渠道添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="channelCarriers" action="${ctx}/app/channelcarriers/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>渠道Id：</label><form:input path="channelId" htmlEscape="false" maxlength="50" class="input-mini"/></li>
			<li><label>渠道名称：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li><label>运营商渠道：</label><form:input path="carriersChannelId" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li><label>运营商：</label><form:input path="carriersType" htmlEscape="false" maxlength="50" class="input-mini"/></li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th class="sort-column channel_id">渠道ID</th>
				<th class="sort-column name">名称</th>
				<th class="sort-column carriers_type">运营商</th>
				<th class="sort-column carriers_channelid">运营商渠道号</th>
				<shiro:hasPermission name="app:channelcarriers:edit">
				<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="channelcarriers">
			<tr>
				<td><a href="${ctx}/app/channelcarriers/form?id=${channelcarriers.id}">${channelcarriers.channelId}</a></td>
				<td>${channelcarriers.name}</td>
				<td>${channelcarriers.carriersName}</td>
				<td>${channelcarriers.carriersChannelId}</td>
				<shiro:hasPermission name="app:channelcarriers:edit"><td>
    				<a href="${ctx}/app/channelcarriers/form?id=${channelcarriers.id}">修改</a>
					<a href="${ctx}/app/channelcarriers/delete?id=${channelcarriers.id}" onclick="return confirmx('确认要删除该运营商渠道吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>