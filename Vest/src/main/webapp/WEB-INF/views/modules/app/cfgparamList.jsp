<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>配置文件管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		
	</script>
</head>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/app/list">配置文件列表</a></li>
		<li><a href="${ctx}/app/insert">配置添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="cfgparam" action="${ctx}/app/list/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>游戏：</label>
		<form:select id="ckAppId" path="ckAppId" class="input-medium" >
			<form:option value="" label="(全部)"/>
			<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
	    </form:select>
    	<label>渠道：</label>
		<form:select id="ckChannelId" path="ckChannelId" class="input-medium">
			<form:option value="" label="(全部)"/>
			<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
	    </form:select>
	    <label>版本号：</label>
		<form:input id="versionName" path="versionName" htmlEscape="false" class="input-medium"/>
	    <label>类型：</label>
		<form:select id="rqType" path="rqType" class="input-medium">
			<form:option value="" label="(全部)"/>
			 <form:options items="${fns:getDictList('cfgtype')}" itemLabel="value" itemValue="value" htmlEscape="false"/>
	    </form:select>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>产品</th><th>子游戏</th><th>渠道</th><th>版本号</th><th>省份</th><th>时间区间</th>
				<th>类型</th><th>扩张参数</th><th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="appCfgParam">
			<tr>
				<td>${fns:getByCkAppNameByChild(appCfgParam.ckAppId,appCfgParam.childCkAppId)}(${appCfgParam.ckAppId})</td>
				<td>${appCfgParam.childCkAppId}</td>
				<td>${fns:findChannelName(appCfgParam.ckChannelId,appCfgParam.ckChannelId) eq null?appCfgParam.ckChannelId:fns:findChannelName(appCfgParam.ckChannelId,appCfgParam.ckChannelId)}</td>
				<td>${appCfgParam.versionName}</td>
				<td>${appCfgParam.province}</td>
				<td>${appCfgParam.time}</td>
				<td>${appCfgParam.rqType}</td>
				<td>${appCfgParam.exInfo}</td>
				<td><a href="${ctx }/app/copyCfg?id=${appCfgParam.id}" >复制</a>
					&nbsp;&nbsp;<a href="javascript:click('${appCfgParam.id}');" value="查看">查看</a>
					&nbsp;&nbsp;<a href="${ctx }/app/edit?id=${appCfgParam.id}" >修改</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>