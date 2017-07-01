<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>上传文件管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/game/updateVersion/list");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/game/updateVersion/list">上传文件列表</a></li>
		<shiro:hasPermission name="game:updateVersion:edit"><li><a href="${ctx}/game/updateVersion/form">上传文件添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="updateVersion" action="${ctx}/game/updateVersion/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>创酷appid：</label>
				<form:select path="ckAppId" class="input-medium">
						<form:option value="" label="(全部)"/>
						<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
				</form:select> 
			</li>
			<%-- <form:input path="ckappId" htmlEscape="false" maxlength="50" class="input-medium"/> --%></li>
			<li><label class="control-label">渠道：</label>
				<form:select id="channelId" path="ckChannelId" class="input-medium">
					<form:option value="" label="(全部)" />
					<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
			   	</form:select>
		   	</li>
			<li class="clearfix"></li>
			<li><label>版本号：</label><form:input path="versionName" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th class="sort-column ckAppId">创酷appid</th>
				<th class="sort-column ckChannelId">渠道</th>
				<th class="sort-column versionName">版本</th>
				<th>地址</th>
				<th>文件名</th>
				<th>大小</th>
				<th>上传时间</th>
				<th>是否启用</th>
				<shiro:hasPermission name="game:updateVersion:edit">
				<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="updateVersion">
			<tr>
				<td>${updateVersion.ckAppId}</td>
				<td>${updateVersion.ckChannelId}</td>
				<td>${updateVersion.versionName}</td>
				<td>${updateVersion.path}</td>
				<td>${updateVersion.fileName}</td>
				<td>${updateVersion.size}</td>
				<td>${updateVersion.createTime}</td>
				<td>${updateVersion.isUse}</td>
				<shiro:hasPermission name="game:updateVersion:edit">
				<td>
    				<a href="${ctx}/game/updateVersion/form?id=${updateVersion.id}">修改</a>
					<a href="${ctx}/game/updateVersion/delete?id=${updateVersion.id}" onclick="return confirmx('确认要删除该运营商APP吗？', this.href)">删除</a>
				</td>
				</shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>