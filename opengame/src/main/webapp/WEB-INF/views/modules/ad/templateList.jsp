<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>ad资源管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/ad/adResource/templateList");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/ad/adResource/templateList">模板列表</a></li>
		<li class="active"><a href="${ctx}/ad/adResource/generateTemplate">模板添加</a></li>
	</ul><br/>
	<form:form id="searchForm" modelAttribute="updateVersion" action="${ctx}/ad/adResource/list" method="post" class="breadcrumb form-search ">
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
			<li><label>渠道：</label>
				<form:select id="channelId" path="ckChannelId" class="input-medium">
					<form:option value="" label="(全部)" />
					<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
		   	</form:select></li>
			
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th class="sort-column ckAppId">游戏</th>
				<th class="sort-column ckChannelId">渠道</th>
				<th>地址</th>
				<th>图片大小</th>
				<th>创建时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="updateVersion">
			<tr>
				<td>${fns:getByCkAppName(updateVersion.ckAppId)}(${updateVersion.ckAppId})</td>
				<td>${updateVersion.ckChannelId}</td>
				<td>${updateVersion.url}</td>
				<td>${updateVersion.size}</td>
				<td>${updateVersion.createTime}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>