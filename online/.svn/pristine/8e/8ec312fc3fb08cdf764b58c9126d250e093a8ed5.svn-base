<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>物品管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/game/goods/list");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/game/goods/list">游戏物品列表</a></li>
		<shiro:hasPermission name="game:goods:edit"><li><a href="${ctx}/game/goods/form">游戏物品添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="goods" action="${ctx}/game/goods/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>创酷appid：</label>
				<form:select path="ckAppId" class="input-medium">
						<form:option value="" label="(全部)"/>
						<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
				</form:select> 
			<%-- <form:input path="ckappId" htmlEscape="false" maxlength="50" class="input-medium"/> --%></li>
			<li><label>物品名称：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th class="sort-column ckAppId">游戏</th>
				<th class="sort-column id">物品id</th>
				<th>名称</th>
				<th>是否启用</th>
				<shiro:hasPermission name="game:goods:edit">
				<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="goods">
			<tr>
				<td>${fns:getByCkAppName(goods.ckAppId)}(${goods.ckAppId})</td>
				<td>${goods.id}</td>
				<td>${goods.name}</td>
				<td>${goods.isUse eq 1?'启用':'未启用'}</td>
				<shiro:hasPermission name="game:goods:edit">
				<td>
					<c:if test="${goods.isUse eq 1 }">
						<a href="${ctx}/game/goods/use?id=${goods.id}&ckAppId=${goods.ckAppId}&isUse=0">禁用</a>
					</c:if>
					<c:if test="${goods.isUse ne 1 }">
						<a href="${ctx}/game/goods/use?id=${goods.id}&ckAppId=${goods.ckAppId}&isUse=1">启用</a>
					</c:if>
    				<a href="${ctx}/game/goods/form?id=${goods.id}&ckAppId=${goods.ckAppId}">修改</a>
					<%-- <a href="${ctx}/game/goods/delete?id=${goods.id}" onclick="return confirmx('确认要删除该运营商APP吗？', this.href)">删除</a> --%>
				</td>
				</shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>