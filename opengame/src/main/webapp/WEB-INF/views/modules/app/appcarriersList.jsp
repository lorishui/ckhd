<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>运营商APP管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/app/appcarriers/list");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/app/appcarriers/list">运营商APP列表</a></li>
		<shiro:hasPermission name="app:appcarriers:edit"><li><a href="${ctx}/app/appcarriers/form">运营商APP添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="appCarriers" action="${ctx}/app/appcarriers/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>创酷appid：</label>
				<form:select path="ckappId" class="input-medium">
						<form:option value="" label="(全部)"/>
						<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
				</form:select> 
			<%-- <form:input path="ckappId" htmlEscape="false" maxlength="50" class="input-medium"/> --%></li>
			<li><label>创酷名称：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li class="clearfix"></li>
			<li><label>运营商：</label><form:input path="carriersType" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li><label>appid：</label><form:input path="appId" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th class="sort-column a.ckapp_id">创酷appid</th>
				<th>名称</th>
				<th class="sort-column carriers_type">运营商</th>
				<th class="sort-column app_id">运营商APP号</th>
				<th>运营商APP名称</th>
				<th>下发CP服务地址</th>
				<th>导量商服务地址</th>
				<th>导量计费代码列表</th>
				<th>指定渠道转发</th>
				<shiro:hasPermission name="app:appcarriers:edit">
				<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="appcarriers">
			<tr>
				<td>${appcarriers.ckappId}</td>
				<td>${appcarriers.name}</td>
				<td>${appcarriers.carriersName}</td>
				<td>${appcarriers.appId}</td>
				<td>${appcarriers.appName}</td>
				<td>${appcarriers.cpServerUrl}</td>
				<td>${appcarriers.flowServerUrl}</td>
				<td>${appcarriers.paycodes}</td>
				<td>${appcarriers.forwardByChannel}</td>
				<shiro:hasPermission name="app:appcarriers:edit">
				<td>
    				<a href="${ctx}/app/appcarriers/form?id=${appcarriers.id}">修改</a>
					<a href="${ctx}/app/appcarriers/delete?id=${appcarriers.id}" onclick="return confirmx('确认要删除该运营商APP吗？', this.href)">删除</a>
				</td>
				</shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>