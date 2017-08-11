<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>计费点管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnImport").click(function(){
				$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
					bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
			});
		});
	
	
	
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/app/appcarriers/paycode/list");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<div id="importBox">
		<form id="importForm" action="${ctx}/app/appcarriers/paycode/import" method="post" enctype="multipart/form-data"
			class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
			<a href="${ctx}/app/appcarriers/paycode/import/template">下载模板</a>
		</form>
	</div>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/app/appcarriers/paycode/list">计费点列表</a></li>
		<shiro:hasPermission name="app:appcarriers:edit">
		<li><a href="${ctx}/app/appcarriers/paycode/form">计费点添加</a></li> 
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="paycode" action="${ctx}/app/appcarriers/paycode/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>appid：</label><form:input path="appId" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li><label>APP名称：</label><form:input path="appName" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li><label>运营商：</label><form:input path="carriersType" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li class="clearfix"></li>
			<li><label>计费点编码：</label><form:input path="paycode" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li><label>计费点名称：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th class="sort-column a.appid">appid</th>
				<th>app名称</th>
				<th>运营商</th>
				<th class="sort-column a.paycode">计费点编码</th>
				<th>计费点名称</th>
				<th class="sort-column a.price">价格(分)</th>
				<th>计费点类型</th>
				<shiro:hasPermission name="app:appcarriers:edit">
				<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="paycode">
			<tr>
				<td>${paycode.appId}</td>
				<td>${paycode.appName}</td>
				<td>${paycode.carriersName}</td>
				<td>${paycode.paycode}</td>
				<td>${paycode.name}</td>
				<td>${paycode.price}</td>
				<td>${paycode.paytype}</td>
				<shiro:hasPermission name="app:appcarriers:edit">
				<td>
    				<a href="${ctx}/app/appcarriers/paycode/form?id=${paycode.id}">修改</a>
					<a href="${ctx}/app/appcarriers/paycode/delete?id=${paycode.id}" onclick="return confirmx('确认要删除该计费点吗？', this.href)">删除</a>
				</td> 
				</shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>