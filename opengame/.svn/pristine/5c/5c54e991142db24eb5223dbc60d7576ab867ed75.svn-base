<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>游戏礼包管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/game/gift/list");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/game/gift/list">游戏礼包列表</a></li>
		<shiro:hasPermission name="game:gift:edit"><li><a href="${ctx}/game/gift/form">游戏礼包添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="gift" action="${ctx}/game/gift/list" method="post" class="breadcrumb form-search ">
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
			
			
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th class="sort-column a.ckapp_id">创酷appid</th>
				<th>礼包id</th>
				<th>礼包名称</th>
				<th>礼包明细</th>
				<shiro:hasPermission name="game:gift:edit">
				<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="gift">
			<tr>
				<td>${fns:getByCkAppName(gift.ckAppId)}${gift.ckAppId }</td>
				<td>${gift.id}</td>
				<td>${gift.name}</td>
				<td>${gift.giftDesc}</td>
				<shiro:hasPermission name="game:gift:edit">
				<td>
    				<a href="${ctx}/game/gift/form?id=${gift.id}&ckAppId=${gift.ckAppId}">修改</a>
					<a href="${ctx}/game/gift/delete?id=${gift.id}&ckAppId=${gift.ckAppId}" onclick="return confirmx('确认要删除该游戏礼包吗？', this.href)">删除</a>
				</td>
				</shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>