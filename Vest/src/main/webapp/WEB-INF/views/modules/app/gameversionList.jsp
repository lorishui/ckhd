<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>游戏版本管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/app/gameversion/list");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/app/gameversion/list">游戏版本列表</a></li>
		<shiro:hasPermission name="app:gameversion:edit"><li><a href="${ctx}/app/gameversion/form">游戏版本添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="gameVersion" action="${ctx}/app/gameversion/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>游戏ID：</label><form:input path="gameId" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li><label>游戏版本：</label><form:input path="version" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li><label>名称：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
			<th class="sort-column game_id">游戏ID</th>
			<th class="sort-column version">版本</th>
			<th class="sort-column name">名称</th>
			<th>备注</th>
			<shiro:hasPermission name="app:gameversion:edit">
				<th>操作</th>
			</shiro:hasPermission>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="gameversion">
			<tr>
				<td><a href="${ctx}/app/gameversion/form?id=${gameversion.id}">${gameversion.gameId}</a></td>
				<td>${gameversion.version}</td>
				<td>${gameversion.name}</td>
				<td>${gameversion.remarks}</td> 
				<shiro:hasPermission name="app:gameversion:edit"><td>
    				<a href="${ctx}/app/gameversion/form?id=${gameversion.id}">修改</a>
					<a href="${ctx}/app/gameversion/delete?id=${gameversion.id}" onclick="return confirmx('确认要删除该游戏版本吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>