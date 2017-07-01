<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>礼包配置管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/game/giftDesc/list");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/game/giftDesc/list">礼包配置列表</a></li>
		<shiro:hasPermission name="game:giftDesc:edit"><li><a href="${ctx}/game/giftDesc/form">礼包配置添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="giftDesc" action="${ctx}/game/giftDesc/list" method="post" class="breadcrumb form-search ">
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
			<li><label>礼包名称：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li class="clearfix"></li>
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
				<th class="sort-column a.ckapp_id">创酷appid</th>
				<th>渠道</th>
				<th>名称</th>
				<th>数量</th>
				<th>批次</th>
				<th>礼包明细</th>
				<th>礼包使用开始时间</th>
				<th>礼包使用截止时间</th>
				<th>是否已生成</th>
				<shiro:hasPermission name="game:giftDesc:edit">
				<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="giftDesc">
			<tr>
				<td>${fns:getByCkAppName(giftDesc.ckAppId)}(${giftDesc.ckAppId })</td>
				<td>${fns:findChannelName(giftDesc.ckChannelId,'')}</td>
				<td>${giftDesc.name}</td>
				<td>${giftDesc.number}</td>
				<td>${giftDesc.batch}</td>
				<td>${giftDesc.giftDesc}</td>
				<td>${giftDesc.startTime}</td>
				<td>${giftDesc.endTime}</td>
				<td>${giftDesc.isGenerate}</td>
				<shiro:hasPermission name="game:giftDesc:edit">
				<td>
					<c:if test="${giftDesc.isGenerate eq 0}">
						<a href="${ctx}/game/giftDesc/generate?id=${giftDesc.id}">生成礼包码</a>
					</c:if>
    				<a href="${ctx}/game/giftDesc/form?id=${giftDesc.id}">修改</a>
					<%-- <a href="${ctx}/game/giftDesc/delete?id=${giftDesc.id}" onclick="return confirmx('确认要删除该礼包配置吗？', this.href)">删除</a> --%>
				</td>
				</shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>