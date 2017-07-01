<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>消耗管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			/* $("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出消耗数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/app/adPushCost/export?adPushDetailId=${adPushDetailId}");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			}); */
			$("#btnImport").click(function(){
				$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
					bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
			});
		});
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/app/adPushCost/list?adPushDetailId=${adPushDetailId}");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<div id="importBox" class="hide">
		<form id="importForm" action="${ctx}/app/adPushCost/import?adPushDetailId=${adPushDetailId}" method="post" enctype="multipart/form-data"
			class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
			<a href="${ctx}/app/adPushCost/import/template?adPushDetailId=${adPushDetailId}">下载模板</a>
		</form>
	</div>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/app/adPushCost/list">消耗列表</a></li>
		<shiro:hasPermission name="app:adPushCost:edit"><li><a href="${ctx}/app/adPushCost/form?adPushDetailId=${adPushDetailId}">消耗添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="adPushCost" action="${ctx}/app/adPushCost/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<%-- <li><label>登录名：</label><form:input path="loginName" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li class="clearfix"></li> --%>
			
			<li><label>开始日期：</label>
			<input id="startDate" name="startDate" type="text" maxlength="20" class="input Wdate" width="60"
				value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			<li><label>结束日期：</label>
			<input id="startDate" name="endDate" type="text" maxlength="20" class="input Wdate" width="60"
				value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			
			
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
				<!-- <input id="btnExport" class="btn btn-primary" type="button" value="导出"/> -->
				<input id="btnImport" class="btn btn-primary" type="button" value="导入"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th class="sort-column login_name">消耗日期</th><th>注册设备数</th><th class="sort-column name">当日消耗</th><th>注册成本(元/台)</th><th>当日收入</th><th>平均收入(元/台)</th><shiro:hasPermission name="app:adPushCost:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="apc">
			<tr>
				<%-- <td><a href="${ctx}/app/adPushCost/form?id=${user.id}">${user.loginName}</a></td> --%>
				<td><fmt:formatDate value="${apc.date}" pattern="yyyy-MM-dd"/></td>
				<td>${apc.registNum}</td>
				<td>${apc.dayCost}</td>
				<td>
					<fmt:formatNumber value="${apc.registCost}" pattern="0.00"/>
				</td>
				<td>${apc.dayEarn}</td>
				<td>
					<fmt:formatNumber value="${apc.averageEarn}" pattern="0.00"/>
				</td>
				<shiro:hasPermission name="app:adPushCost:edit"><td>
    				<a href="${ctx}/app/adPushCost/form?id=${apc.id}&adPushDetailId=${adPushDetailId}">修改</a>
					<a href="${ctx}/app/adPushCost/delete?id=${apc.id}&	adPushDetailId=${adPushDetailId}" onclick="return confirmx('确认要删除该数据吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>