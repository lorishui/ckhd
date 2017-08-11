<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>参数显示配置管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/app/CfgParamFeilds/list");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/app/CfgParamFeilds/list">参数显示配置列表</a></li>
		<%-- <shiro:hasPermission name="app:appcarriers:edit"> --%><li><a href="${ctx}/app/CfgParamFeilds/form">运营商APP添加</a></li><%-- </shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="cfgParamFeilds" action="${ctx}/app/CfgParamFeildss/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>创酷appid：</label>
				<form:select path="ckAppId" class="input-medium">
					<form:option value="" label="(全部)"/>
					<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
				</form:select> 
			<li><label>参数类型：</label>
				<form:select path="type"   class="input-medium">
					<form:option value="" label="(全部)"/>
					<form:options items="${fns:getDictList('cfgtype')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th class="sort-column a.ckApp_id">创酷appid</th>
				<th>参数类型</th>
				<th>参数key</th>
				<th>参数key的描述</th>
				<th>参数值详细描述</th>
				<th>参数值的类型</th>
				<th>排序</th>
				<%-- <shiro:hasPermission name="app:appcarriers:edit"> --%>
				<th>操作</th>
				<%-- </shiro:hasPermission> --%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="cfg">
			<tr>
				<c:if test="${cfg.ckAppId eq '#'}">
					<td>#</td>
				</c:if>
				<c:if test="${cfg.ckAppId ne '#'}">
					<td>${fns:getByCkAppNameByChild(appCfgParam.ckAppId,1)}(${appCfgParam.ckAppId})</td>
				</c:if>
				<td>${fns:getDictLabel(cfg.type,'cfgtype','')}</td>
				<td>${cfg.value}</td>
				<td>${cfg.label}</td>
				<td>${cfg.description}</td>
				<c:if test="${cfg.classType eq null}">
					<td></td>
				</c:if>
				<c:if test="${cfg.classType ne null and cfg.classType == 1}">
					<td>int</td>
				</c:if>
				<c:if test="${cfg.classType ne null and cfg.classType == 2}">
					<td>boolean</td>
				</c:if>
				<c:if test="${cfg.classType ne null and cfg.classType == 3}">
					<td>JSONObject</td>
				</c:if>
				<c:if test="${cfg.classType ne null and cfg.classType == 4}">
					<td>JSONArray</td>
				</c:if>
				<c:if test="${cfg.classType ne null and cfg.classType == 5}">
					<td>String</td>
				</c:if>
				<td>${cfg.sort}</td>
				<%-- <shiro:hasPermission name="app:appcarriers:edit"> --%>
				<td>
    				<a href="${ctx}/app/CfgParamFeilds/form?id=${cfg.id}">修改</a>
					<a href="${ctx}/app/CfgParamFeilds/delete?id=${cfg.id}" onclick="return confirmx('确认要删除该参数显示配置吗？', this.href)">删除</a>
				</td>
				<%-- </shiro:hasPermission> --%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>