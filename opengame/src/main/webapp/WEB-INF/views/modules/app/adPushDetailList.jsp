<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>消耗管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/app/adPushDetail/list");
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
			var iframe = document.getElementById("adPushCost")
			/* iframe.src = "${ctx}/app/adPushCost/list?adPushDetailId="+a
			iframe.reload; */
			openwin(a);
		} 
		function openwin(a) { 
			window.open ("${ctx}/app/adPushCost/list?adPushDetailId="+a, "newwindow", "left=250,top=80,channelmode=yes, height=600, width=800, toolbar =no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no") //写成一行
		} 
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/app/adpushDetail/list?adPushId=${adPushId}">链接列表</a></li>
		<shiro:hasPermission name="app:adPushDetail:edit"><li><a href="${ctx}/app/adpushDetail/form?adPushId=${adPushId}">链接添加</a></li></shiro:hasPermission>
	</ul>
	<%-- <form:form id="searchForm" modelAttribute="adPushDetail" action="${ctx}/app/adpushDetail/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>游戏名称：</label><form:input path="appName" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li><label>平台：</label><form:input path="platform" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li class="clearfix"></li>
		</ul>
		<ul class="ul-form">
			<li><label>媒体：</label><form:input path="mediaName" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li><label>账户：</label><form:input path="account" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form> --%>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-bordered table-condensed">
		<thead>
			<tr>
				<th>负责人</th>
				<th>广告位</th>
				<th>推广链接</th>
				<th>推广描述</th>
				<th>消耗管理</th>
				<shiro:hasPermission name="app:adPushDetail:edit">
				<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="adPushDetail">
			<tr  id="${adPushDetail.id}" name="trColor">
				<td>${adPushDetail.userName}</td>
				<td>${adPushDetail.adPlace}</td>
				<td>${adPushDetail.adUrl}</td>
				<td>${adPushDetail.description}</td>
				
				<!-- 待跳转iframe -->
				<!-- <iframe src=""  name="main"></iframe> -->
				<td><a class="changeColor" href="javascript:void(0)" onclick="changeColor('${adPushDetail.id}')">每日消耗</a></td>
				
				<shiro:hasPermission name="app:adPushDetail:edit"><td>
    				<a href="${ctx}/app/adpushDetail/form?adPushId=${adPushId}&id=${adPushDetail.id}">修改</a>
					<a href="${ctx}/app/adpushDetail/delete?id=${adPushDetail.id}&adPushId=${adPushId}" onclick="return confirmx('确认要删除该推广吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<%-- <div class="pagination">${page}</div> --%>
	<iframe id="adPushCost"  style="width: 50%; height: 500px;"
		scrolling="yes" frameborder="0" marginheight="0" marginwidth="1" ></iframe>
</body>
</html>