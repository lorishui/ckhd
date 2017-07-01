<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>渠道管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/app/channel/list");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/app/channel/list">渠道列表</a></li>
		<shiro:hasPermission name="app:channel:edit"><li><a href="${ctx}/app/channel/form">渠道添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="channel" action="${ctx}/app/channel/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>渠道名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li class="btns">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th class="sort-column id">ID</th>
				<th class="sort-column name">名称</th>
				<th class="sort-column engName">SDK类型</th>
				<th>公司</th>
				<th>是否CPS</th>
				<th>备注</th>
				<shiro:hasPermission name="app:channel:edit">
				<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="channel">
			<tr>
				<td><a href="${ctx}/app/channel/form?id=${channel.id}">${channel.id}</a></td>
				<td>${channel.name}</td>
				<td>${channel.engName}</td>
				<td>${channel.company}</td>
				<td>${channel.isCPS}</td>
				<td>${channel.remarks}</td>
				<shiro:hasPermission name="app:channel:edit"><td>
    				<a href="${ctx}/app/channel/form?id=${channel.id}">修改</a>
					<a href="${ctx}/app/channel/delete?id=${channel.id}" onclick="return confirmx('确认要删除该渠道吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>