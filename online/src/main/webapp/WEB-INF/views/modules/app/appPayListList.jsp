<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>支付列表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/app/appPayList/list");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/app/appPayList/list">支付列表</a></li>
		<li><a href="${ctx}/app/appPayList/form">支付列表添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="appPayList" action="${ctx}/app/appPayList/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>创酷appid：</label>
				<form:select path="ckAppId" class="input-medium">
						<form:option value="" label="(全部)"/>
						<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
				</form:select> 
			<%-- <form:input path="ckappId" htmlEscape="false" maxlength="50" class="input-medium"/> --%></li>
			<li><label>渠道：</label>
				<form:select id="channelId" path="ckChannelId" class="input-medium">
					<form:option value="" label="(全部)" />
					<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
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
				<th class="sort-column a.ckapp_id">创酷appid</th>
				<th>子创酷appid</th>
				<th>渠道</th>
				<th>支付方式</th>
				<th>是否启用</th>
				<th>创建时间</th>
				<shiro:hasPermission name="app:appPayList:edit"> 
				<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="appPayList">
			<tr>
				<td>${fns:getByCkAppNameByChild(appPayList.ckAppId,appPayList.childCkAppId)}(${appPayList.ckAppId})</td>
				<td>${appPayList.childCkAppId}</td>
				<td>${fns:findChannelName(appPayList.ckChannelId,"")}(${appPayList.ckChannelId})</td>
				<td>${appPayList.payType}</td>
				<td>
					<c:if test="${appPayList.isUse ==1}">启用</c:if>
					<c:if test="${appPayList.isUse ==0}">未启用</c:if>
				</td>
				<td><fmt:formatDate value="${appPayList.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<shiro:hasPermission name="app:appPayList:edit">
				<td>
    				<a href="${ctx}/app/appPayList/form?id=${appPayList.id}">修改</a>
				</td>
				</shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>