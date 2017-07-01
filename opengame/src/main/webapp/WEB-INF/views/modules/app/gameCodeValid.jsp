<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>消耗管理</title>
<meta name="decorator" content="default"/>
<script type="text/javascript">
	$(document).ready(function() {
		
	});
	function page(n, s) {
		if (n)
			$("#pageNo").val(n);
		if (s)
			$("#pageSize").val(s);
		$("#searchForm").attr("action", "${ctx}/game/gamecode/validweb");
		$("#searchForm").submit();
		return false;
	}
	/* function changeCkAppId(){
		var ckApp = document.getElementById("ckAppId");
		var index = ckApp.selectedIndex;
		var ckAppId = ckApp.options[index].value;
		var ac = document.getElementById("changeLink").getAttribute("action")
		document.getElementById("importForm").setAttribute("action", ac + "&ckAppId=" +ckAppId)
		
	} */
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/game/gamecode/importweb">礼包码上传</a></li>
		<li class="active"><a href="${ctx}/game/gamecode/validweb">礼包码管理</a></li>
	</ul>
	
	<form:form id="searchForm" modelAttribute="gameCodeVer"
		action="" method="post"
		class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}"
			callback="page();" />
			<label>游戏名称:</label>
				<form:select path="ckAppId" class="input-medium">
					<form:option value="" label="(全部)" />
					<c:forEach var="app" items="${allCkApp}" varStatus="status">
						<form:option value="${app.appId}" label="${app.name}" />
					</c:forEach>
				</form:select>
			<input id="btnSubmit" class="btn btn-primary"
				type="submit" value="查询" onclick="return page();" />
	</form:form>
	<sys:message content="${message}"/>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
			<th>创酷APP</th><th>上传日期</th><th>备注</th><th>状态</th><th>操作</th>
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="gcv">
			<tr>
				<td>${gcv.ckAppId}</td>
				<td>
					<fmt:formatDate value="${gcv.date}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>${gcv.remark}</td>
				<td>${gcv.status eq '0'?"有效":"已失效"}</td>
				<%-- <shiro:hasPermission name="app:adPushDetail:edit"> --%>
				<td>
    				<a id="changeLink" href="${ctx}/game/gamecode/changeVer?id=${gcv.id}&status=${gcv.status}">修改状态</a>
				</td>
				<%-- </shiro:hasPermission> --%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>