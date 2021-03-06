<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>APP管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/app/appck/list");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/app/appck/list">APP列表</a></li>
		<shiro:hasPermission name="app:appck:edit"><li><a href="${ctx}/app/appck/form">APP添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="APPCk" action="${ctx}/app/appck/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>创酷appid：</label><form:input path="ckappId" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li class="clearfix"></li>
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
				<th class="sort-column ckapp_id">创酷appid</th>
				<th class="sort-column name">名称</th>
				<th class="sort-column childId">子游戏id</th>
				<th class="sort-column childName">子游戏名称</th>
				<th>cpid</th>
				<th>cpname</th>
				<th>折扣率</th>
				<th>回调地址</th>
				<th>密钥</th>
				<th>排序(越大越前)</th>
				<shiro:hasPermission name="app:appck:edit">
				<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="appck">
			<tr>
				<td><a href="${ctx}/app/appck/form?id=${appck.id}">${appck.ckappId}</a></td>
				<td>${appck.name}</td>
				<td>${appck.childId}</td>
				<td>${appck.childName}</td>
				<td>${appck.cpid}</td>
				<td>${appck.cpname}</td>
				<td>${appck.discount}</td>
				<td>${appck.payCallbackUrl}</td>
				<td>${appck.secretKey }</td>
				<td>${appck.orderIndex }</td>
				<shiro:hasPermission name="app:appck:edit"><td>
					<a href="${ctx}/app/appck/addChild?ckappId=${appck.ckappId}">添加子游戏</a>
    				<a href="${ctx}/app/appck/form?id=${appck.id}">修改</a>
					<a href="${ctx}/app/appck/delete?id=${appck.id}" onclick="return confirmx('确认要删除该APP吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>