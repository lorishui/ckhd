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
		$("#searchForm").attr("action",
				"${ctx}/app/adStatistics/findUrlStaList");
		$("#searchForm").submit();
		return false;
	}
	function selectDateFn(val) {
		var startDate = $("#startDate");
		var endDate = $("#endDate");
		if (val == 0) {
			var day = GetDateStr(0);
			startDate.val(day);
			endDate.val(day);
		} else if (val == 1) {
			var day = GetDateStr(-1);
			startDate.val(day);
			endDate.val(day);
		} else if (val == 7) {
			startDate.val(GetDateStr(-6));
			endDate.val(GetDateStr(0));
		} else if (val == 30) {
			startDate.val(GetDateStr(-29));
			endDate.val(GetDateStr(0));
		}
		if (val >= 0) {
			$("#searchForm").submit();
		}
	}
	function GetDateStr(AddDayCount) {
		var dd = new Date();
		dd.setDate(dd.getDate() + AddDayCount);//获取AddDayCount天后的日期
		var y = dd.getFullYear();
		var m = dd.getMonth() + 1;//获取当前月份的日期
		var d = dd.getDate();
		m = m < 10 ? "0" + m : m;
		d = d < 10 ? "0" + d : d;
		return y + "-" + m + "-" + d;
	}
</script>
</head>
<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/app/adStatistics/findUrlStaList">统计列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="adQueryEntity" action="${ctx}/app/adStatistics/findUrlStaList" method="post" class="breadcrumb form-search ">
		<div>
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" /> <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
			<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();" />
			<li><label>时间范围：</label> <a href="#" onclick="javascript:selectDateFn(0);void(0)">今天</a> <a href="#" onclick="javascript:selectDateFn(1);void(0)">昨天</a> <a href="#"
				onclick="javascript:selectDateFn(7);void(0)">近七天</a> <a href="#" onclick="javascript:selectDateFn(30);void(0)">近30天</a> <input id="startDate" name="startDate" type="text"
				maxlength="20" class="input Wdate" width="60" value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" /> <input id="startDate" name="endDate" type="text" maxlength="20" class="input Wdate" width="60"
				value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" /> &nbsp;&nbsp; <form:checkbox path="sum"
					value="a" />求和 &nbsp;&nbsp;</li>
		</div>
		<br>
		<div>
			<li><label>媒体:</label> <form:select path="mediaId" class="input-medium">
					<form:option value="" label="(全部)" />
					<form:options items="${fns:getMineMediaList()}" itemLabel="mediaName" itemValue="mediaId" htmlEscape="false" />
				</form:select> <label>游戏名称:</label> <form:select id="ckAppId" path="appId" class="input-medium" onChange="changeAPPID()">
					<form:option value="" label="(全部)" />
					<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name" itemValue="ckappId" htmlEscape="false" />
				</form:select> <shiro:hasPermission name="app:adStatistics:admin">
					<label>负责人:</label>
					<form:select path="operator" class="input-medium">
						<form:option value="" label="(全部)" />
						<c:forEach var="operator" items="${allOperators}" varStatus="status">
							<form:option value="${operator.id}" label="${operator.name}" />
						</c:forEach>
					</form:select>
				</shiro:hasPermission> <label class="control-label">平台:</label> <form:select path="platform" class="input-medium">
					<form:option value="" label="(全部)" />
					<form:option value="IOS" label="IOS" />
					<form:option value="Andriod" label="Andriod" />
				</form:select> <%-- <label>账户：</label> <form:input path="account"
					htmlEscape="false" maxlength="50" /> --%> <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" /></li>
		</div>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable" class="table table-bordered table-condensed">
		<thead>
			<tr>
				<th onclick="$.sortTable.sort('contentTable',0,compareNumber)" style="cursor: pointer;">链接</th>
				<th onclick="$.sortTable.sort('contentTable',1,compareNumber)" style="cursor: pointer;">日期</th>
				<th onclick="$.sortTable.sort('contentTable',2,compareNumber)" style="cursor: pointer;">媒体</th>
				<th onclick="$.sortTable.sort('contentTable',3,compareNumber)" style="cursor: pointer;">游戏</th>
				<th onclick="$.sortTable.sort('contentTable',4,compareNumber)" style="cursor: pointer;">负责人</th>
				<th onclick="$.sortTable.sort('contentTable',5,compareNumber)" style="cursor: pointer;">平台</th>
				<th onclick="$.sortTable.sort('contentTable',6,compareNumber)" style="cursor: pointer;">注册设备数</th>
				<th onclick="$.sortTable.sort('contentTable',7,compareNumber)" style="cursor: pointer;">消耗金额</th>
				<th onclick="$.sortTable.sort('contentTable',7,compareNumber)" style="cursor: pointer;">注册成本</th>
				<th onclick="$.sortTable.sort('contentTable',9,compareNumber)" style="cursor: pointer;">收入金额</th>
				<th onclick="$.sortTable.sort('contentTable',10,compareNumber)" style="cursor: pointer;">平均收入</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="sta">
				<tr>
					<td>${sta.adUrl}</td>
					<td><c:choose>
							<c:when test="${adQueryEntity.sum eq 'a'}"> -- </c:when>
							<c:otherwise>
								<fmt:formatDate value="${sta.date}" pattern="yyyy-MM-dd" />
							</c:otherwise>
						</c:choose></td>
					<td>${fns:getDictLabels(sta.mediaId,'adPush_media','')}</td>
					<td>${fns:getByCkAppName(sta.appId)}(${sta.appId})</td>
					<td>${sta.operatorName}</td>
					<td>${sta.platform}</td>
					<td>${sta.registNum}</td>
					<td>${sta.cost}</td>
					<td><fmt:formatNumber value="${sta.registCost}" pattern="0.00" /></td>
					<td>${sta.earn}</td>
					<td><fmt:formatNumber value="${sta.averageEarn}" pattern="0.00" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<c:if test="${adQueryEntity.sum != null}">
		<div class="pagination">${page}</div>
	</c:if>
	<br />
	<iframe id="adPushDetail" style="width: 100%; height: 800px;" scrolling="no" frameborder="0" marginheight="0" marginwidth="1"></iframe>
</body>
</html>