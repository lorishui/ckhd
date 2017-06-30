<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>支付通道配置管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/app/paymentway/list");
			$("#searchForm").submit();
	    	return false;
	    }
		
		function changeStatus(id){
			document.location.href = "${ctx}/app/paymentway/change?id=" + id + "&conCkappId=" + $("#ckappId").val() + "&conVersion=" + $("#version").val() + "&conChannelId=" + $("#channelId").val() ;
		}
		
		function recordParam(){
			document.location.href = "${ctx}/app/paymentway/form?ckappId=" + $("#ckappId").val() + "&version=" + $("#version").val();
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/app/paymentway/list">支付通道配置列表</a></li>
		<shiro:hasPermission name="app:paymentway:edit"><li><a href="javascript:void(0);" onclick="recordParam()">支付通道配置添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="paymentWay" action="${ctx}/app/paymentway/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li>
			<label>运营商：</label>
			<form:select id="carriers" path="carriers" class="input-medium">
				<form:option value="" label="所有运营商"/>
				<form:option value="CMCC" label="移动"/>
				<form:option value="CUCC" label="联通"/>
				<form:option value="CTCC" label="电信"/>
		    </form:select>
			</li>
		
			<li><label>创酷APP：</label>
				<form:select id="ckappId" path="ckappId" class="input-large">
				<form:option value="" label="所有APP"/>
				<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
		    </form:select>
			</li>
			<li><label>版本：</label><form:input path="version" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li>
			<label>渠道：</label>
			<form:select id="channelId" path="channelId" class="input-medium">
				<form:option value="" label="所有渠道"/>
				<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
		    </form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
			<th class="sort-column carriers_type">运营商</th>
			<th class="sort-column ckapp_id">创酷APP</th>
			<th class="sort-column app_ids">APPIDS</th>
			<th class="sort-column version">版本</th>
			<th class="sort-column channel_id">渠道</th>
			<th class="sort-column province_code">省份</th>
			<th class="sort-column pay_way_sign">支付通道标识</th>
			<th>是否启用</th>
			<shiro:hasPermission name="app:paymentway:edit">
				<th>操作</th>
			</shiro:hasPermission>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="paymentway">
			<tr>
				<td>${paymentway.carriers}</td>
				<td>${paymentway.ckappId}</td>
				<td>${paymentway.appIds}</td>
				<td>${paymentway.version}</td>
				<td>${paymentway.channelName}-${paymentway.channelId}</td>
				<td>${paymentway.provinceName}-${paymentway.provinceCode}</td> 
				<td>${paymentway.payWaySign}</td>
				<td>${paymentway.delFlag eq 0? '是':'否'}</td> 
				<shiro:hasPermission name="app:paymentway:edit"><td>
    				<a href="${ctx}/app/paymentway/form?id=${paymentway.id}">修改</a>
					<a href="javascript:void(0);" onclick="changeStatus('${paymentway.id}')" >启用/停用</a>
					<a href="${ctx}/app/paymentway/delete?id=${paymentway.id}" >删除</a>
					<!-- onclick="return confirmx('确认要禁用该支付通道配置吗？', this.href)" -->
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>