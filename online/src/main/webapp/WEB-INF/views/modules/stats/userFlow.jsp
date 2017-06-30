<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>用户流向</title>
<script type="text/javascript" src="/static/tableOrder/tableOrder.js?a=1"></script>
<script type="text/javascript">
	$(document).ready(function() {
		changeAPPID(true);
		$("#ckAppId").focus();
		$("#searchForm").validate({
			submitHandler : function(form) {
				$("#searchForm").attr("action", "${ctx}/stats/userFlow/list");
				form.submit();
				merge();
			},
			errorContainer : "#messageBox"
		});
	});
	function changeAPPID(pageLoad) {
		var id = '${userFlow.childCkAppId}';
		$.ajax({
			url : "${ctx}/app/appck/getChildIdList",
			type : 'get',
			data : 'ckappId=' + $("#ckAppId").val(),
			error : function() {
			},
			success : function(data) {
				var node = $("#childCkAppId");
				node.empty();
				var html = "<option value=\"\">全部</option>";
				for (var n = 0; n < data.length; n++) {
					html += "<option value='"+data[n].childId+"'>"
							+ data[n].name + "_" + data[n].childId
							+ "</option>";
				}
				node.append(html);
				node.val(id);
				var name = node.find("option:selected").text();
				if (id != null && id != '') {
					$(".select2-chosen")[1].innerHTML = name;
				}
			}
		});
	}

</script>
</head>
<body>
	<div id="position">用户流向</div>
	<form:form id="searchForm" modelAttribute="userFlow" action="${ctx}/stats/userFlowList" method="post" class="breadcrumb form-search">

		<div>
			<label>游戏：</label>
			<form:select id="ckAppId" path="ckAppId" class="input-medium"
				onChange="changeAPPID()">
				<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name"
					itemValue="ckappId" htmlEscape="false" />
			</form:select>
			<span><font color="red">*</font> </span>
			<form:select id="childCkAppId" path="childCkAppId"
				class="input-medium" style="width:250px">
			</form:select>
			<label>来源渠道：</label>
			<form:select id="fromchannel" path="fromChannel" class="input-medium">
				<form:option value="" label="(全部)" />
				<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false" />
			</form:select>
			<label>流向渠道：</label>
			<form:select id="toChannel" path="toChannel" class="input-medium">
				<form:option value="" label="(全部)" />
				<form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false" />
			</form:select>
		</div>
		<br>
		<div>
			<label>统计日期：</label> 
			<span><font color="red">*</font> </span> 
			<input id="startTime" name="startTime" type="text" readonly="readonly" maxlength="20"
				value="${userFlow.startTime}" class="WdateTime required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />&nbsp;&nbsp;
			<span><font color="red">*</font> </span> 
			<input id="endTime" name="endTime" type="text" readonly="readonly"
				maxlength="20" value="${userFlow.endTime}"
				class="WdateTime required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp; 
		</div>
	</form:form>
	<table id="statsUserFlowList" class="table2excel table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>游戏</th>
				<th>子游戏</th>
				<th>来源渠道</th>
				<th>流向渠道</th>
				<th onclick="$.sortTable.sort('statsUserFlowList',4,compareNumber)" style="cursor: pointer;">设备数量</th>
				<th onclick="$.sortTable.sort('statsUserFlowList',5,compareNumber)" style="cursor: pointer;">金额</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${data}" var="user" varStatus="status">
				<tr>
					<td>${fns:getByCkAppName(user.ckAppId)}(${user.ckAppId})</td>
					<td>${fns:getByChildAppName(user.ckAppId,user.childCkAppId)}(${user.childCkAppId})</td>
					<td>${fns:findChannelName(user.fromChannel,"")}(${user.fromChannel})</td>
					<td>${fns:findChannelName(user.toChannel,"")}(${user.toChannel})</td>
					<td>${user.num}</th>
					<td>${user.totalMoney/100}</th>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>