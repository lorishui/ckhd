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
			$("#searchForm").attr("action","${ctx}/ad/adResource/list1");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/ad/adResource/list1">ad资源列表</a></li>
		<shiro:hasPermission name="ad:adResource:edit"><li><a href="${ctx}/ad/adResource/form1">ad资源添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="adResource" action="${ctx}/ad/adResource/list1" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>广告主：</label>
				 <form:select path="uid"  disabled="${not empty adResource.uid?true:false}"  class="input-medium">
					<form:option value="" label="(全部)" />
					<c:forEach var="user" items="${adUser }" varStatus="status">
						<form:option value="${user.uid}" label="${user.name}" />
					</c:forEach>
				</form:select>  
				<span class="help-inline"><font color="red">*</font> </span>
			</li>
			
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th class="sort-column uid">广告主</th>
				<th>资源id</th>
				<th>广告类型</th>
				<th>资源类型</th>
				<th>资源格式</th>
				<th>资源地址</th>
				<th>触发后android地址</th>
				<th>触发后ios地址</th>
				<th>触发后的资源格式</th>
				<th>ios触发后的资源格式</th>
				<th>尺寸</th>
				<th>使用结束时间</th>
				<th>是否启用</th>
				<th>在ios/android上推广</th>
				<th>创建时间</th>
				<shiro:hasPermission name="ad:adResource:edit">
				<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="adResource">
			<tr>
				<td>${adResource.uid}</td>
				<td>${adResource.id}</td>
				<td>${adResource.adType}</td>
				<td>${adResource.resourceType}</td>
				<td>${adResource.resourceFormat}</td>
				<td>${adResource.url}</td>
				<td>${adResource.androidUrl}</td>
				<td>${adResource.iosUrl}</td>
				<td>${adResource.clickFormat}</td>
				<td>${adResource.iosClickFormat}</td>
				<td>${adResource.size}</td>
				<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${adResource.endDate}" /></td>
				<td>${adResource.isUse eq 1?'启用':'未启用'}</td>
				<td><c:if test="${adResource.iosUse eq 1 and adResource.androidUse eq 1}">全启用</c:if>
					<c:if test="${adResource.iosUse eq 1 and adResource.androidUse eq 0}">ios启用</c:if>
					<c:if test="${adResource.iosUse eq 0 and adResource.androidUse eq 1}">android启用</c:if>
					<c:if test="${adResource.iosUse eq 0 and adResource.androidUse eq 0}">全禁用</c:if></td>
				<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${adResource.createDate}" /></td>
				<shiro:hasPermission name="ad:adResource:edit">
				<td>
					<c:if test="${adResource.isUse eq 1 }">
						<a href="${ctx}/ad/adResource/use?id=${adResource.id}&ckAppId=${adResource.ckAppId}&isUse=0">禁用</a>
					</c:if>
					<c:if test="${adResource.isUse ne 1 }">
						<a href="${ctx}/ad/adResource/use?id=${adResource.id}&ckAppId=${adResource.ckAppId}&isUse=1">启用</a>
					</c:if>
    				<a href="${ctx}/ad/adResource/form1?id=${adResource.id}&ckAppId=${adResource.ckAppId}">修改</a>
				</td>
				</shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>