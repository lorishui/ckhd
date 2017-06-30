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
		
		function click(id,type){
			var lastClientId = $("#clientId").val();
			
			$("#tr_"+lastClientId).children().each(function() {
				$(this).css("background-color", "");
			});
			
			$("#tr_"+id).children().each(function() {
				$(this).css("background-color", "#FFFFBB");
			});
			
			$("#clientId").val(id);
			
			$("#myFrame").attr("src","${ctx}/app/zonetree/cfg?id="+id+"&type="+type);
		}
	</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="cfgZoneLevel" action="${ctx}/app/zonetree/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>游戏：</label>
		<form:select id="ckappid" path="ckappid" class="input-medium" >
			<form:option value="" label="(全部)"/>
			<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
	    </form:select>
    	<label>渠道：</label>
		<form:select id="channelid" path="channelid" class="input-medium">
			<form:option value="" label="(全部)"/>
			<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
	    </form:select>
	    <label>类型：</label>
		<form:select id="biztype" path="biztype" class="input-medium">
			<form:option value="" label="(全部)"/>
			<form:option value="fee" label="计费"/>
			<form:option value="game" label="弹窗"/>
			<form:option value="ad" label="广告"/>
	    </form:select>
	    <input id="type" name="type" type="hidden" value=""/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		<input id="btnSubmit" class="btn btn-primary" type="submit" onclick="$('#type').val('insert')" value="新增"/>
	</form:form>
	<span style="color:red">说明：渠道的配置是在游戏的配置基础上追加</span>
	<sys:message content="${message}"/>
	<sys:message type="warning" content="${warning}"/>
	<input id="clientId" type="hidden" value=""/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>产品</th><th>渠道</th><th>类型</th><th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="cfgZoneLevel">
			<tr id="tr_${cfgZoneLevel.id}">
				<td>${fns:getByCkAppName(cfgZoneLevel.ckappid)}(${cfgZoneLevel.ckappid })</td>
				<td>${cfgZoneLevel.channelid }</td>
				<td>
				<c:if test="${cfgZoneLevel.biztype eq 'fee' }">
					计费
				</c:if>
				<c:if test="${cfgZoneLevel.biztype eq 'game' }">
					弹窗
				</c:if>
				<c:if test="${cfgZoneLevel.biztype eq 'ad' }">
					广告
				</c:if></td>
				<td><a href="javascript:click('${cfgZoneLevel.id}','1');" >红区</a>
					&nbsp;&nbsp;<a href="javascript:click('${cfgZoneLevel.id}','2');" >黄区</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	<iframe id="myFrame" marginwidth="0" marginheight="0" frameborder="0" scrolling="yes"  width="100%" height="500px" />
</body>
</html>