<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>APP管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	function page(n, s) {
		if (n)
			$("#pageNo").val(n);
		if (s)
			$("#pageSize").val(s);
		$("#searchForm").attr("action", "${ctx}/app/adpush/list");
		$("#searchForm").submit();
		return false;
	}
	function changeColor(a){
		var trs = document.getElementsByName("trColor");
		for (var i = 0; i < trs.length; i++) {
		trs[i].style.background = "white";
		}
		var tr = document.getElementById(a);
		tr.style.background = "#EEEEEE";
		var iframe = document.getElementById("adPushDetail")
		iframe.src = "${ctx}/app/adpushDetail/list?adPushId="+a
		iframe.reload;
	} 
</script>
</head>
<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/app/adpush/list">推广列表</a></li>
		<shiro:hasPermission name="app:adPush:edit">
			<li><a href="${ctx}/app/adpush/form">推广添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="adPush"
		action="${ctx}/app/adpush/list" method="post"
		class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}"
			callback="page();" />
			<label>游戏名称:</label>
				<form:select id="ckAppId" path="appId" class="input-medium">
		            <form:option value="" label="(全部)" />
		            <form:options items="${fns:getAPPCkListByChild()}" itemLabel="name" itemValue="ckappId" htmlEscape="false" />
                 </form:select>
			<label class="control-label">平台:</label>
				<form:select path="platform" class="input-medium" >
					<form:option value="" label="(全部)" />
					<form:option value="IOS" label="IOS" />
					<form:option value="Android" label="Android" />
				</form:select>
			<label>媒体:</label>
				<form:select path="mediaName" class="input-medium">
	                <form:option value="" label="(全部)" />
	                <form:options items="${fns:getMineMediaList()}" itemLabel="mediaName" itemValue="mediaId" htmlEscape="false" />
	            </form:select>&nbsp;&nbsp;&nbsp;
			<label>账户：</label> <form:input path="account"
					htmlEscape="false" maxlength="50" />
			<input id="btnSubmit" class="btn btn-primary"
				type="submit" value="查询" onclick="return page();" />
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable" class="table table-bordered table-condensed">
		<thead>
			<tr>
				<th>游戏名称</th>
				<th>平台(ios/android)</th>
				<th>媒体</th>
				<th>账户</th>
				<th>链接管理</th>
				<shiro:hasPermission name="app:adPush:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="adPush">
				<tr id="${adPush.id}" name="trColor">
					<td>${fns:getByCkAppName(adPush.appId)}(${adPush.appId})</td>
					<td>${adPush.platform}</td>
					<td>${fns:getDictLabels(adPush.mediaName,'adPush_media','')}</td>
					<td>${adPush.account}</td>

					<!-- 待跳转iframe -->
					<td><a class="changeColor" onclick="changeColor('${adPush.id}')" href="javascript:void(0)">查看推广链接</a>
					</td>

					<shiro:hasPermission name="app:adPush:edit">
						<td><a href="${ctx}/app/adpush/form?id=${adPush.id}">修改</a> <a
							href="${ctx}/app/adpush/delete?id=${adPush.id}"
							onclick="return confirmx('确认要删除该推广吗？', this.href)">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	<br/>
	<iframe id="adPushDetail" style="width: 100%; height: 800px;"
		scrolling="no" frameborder="0" marginheight="0" marginwidth="1" ></iframe>
</body>
</html>