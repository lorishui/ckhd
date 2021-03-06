<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>网游设备新增统计信息</title>
<meta name="decorator" content="default" />
<script type="text/javascript" src="/static/tableOrder/tableOrder.js?a=1"></script>
<script type="text/javascript">
	$(document).ready(function() {
		changeAPPID(true);
		$("#ckAppId").focus();
		$("#searchForm").validate({
			submitHandler : function(form) {
				$("#searchForm").attr("action", "${ctx}/stats/money");
				form.submit();
				merge();
			},
			errorContainer : "#messageBox"
		});
	});
	function changeAPPID(pageLoad) {
		var id = '${statMoney.childCkAppId}';
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
	<div id="position">网络订单统计信息</div>
	<form:form id="searchForm" modelAttribute="statMoney"
		action="${ctx}/stats/money" method="post" class="breadcrumb form-search">

		<div>
			<label>游戏：</label>
			<form:select id="ckAppId" path="ckAppId" class="input-medium"
				onChange="changeAPPID()">
				<form:option value="" label="(全部)" />
				<form:options items="${fns:getAPPCkListByChild()}" itemLabel="name"
					itemValue="ckappId" htmlEscape="false" />
			</form:select>
			<span><font color="red">*</font> </span>
			<form:select id="childCkAppId" path="childCkAppId"
				class="input-medium" style="width:250px">
			</form:select>
			<label>渠道：</label>
			<form:select id="channelId" path="ckChannelId" class="input-medium">
				<form:option value="" label="(全部)" />
				<form:options items="${fns:getChannelList()}" itemLabel="name"
					itemValue="id" htmlEscape="false" />
			</form:select>
		</div>
		<br>
		<div>
			<label>时间范围：</label> <a href="#" onclick="javascript:selectDateFn(0);void(0)">今天</a> 
			<a href="#" onclick="javascript:selectDateFn(1);void(0)">昨天</a>
			<a href="#" onclick="javascript:selectDateFn(7);void(0)">近七天</a>
			<a href="#" onclick="javascript:selectDateFn(30);void(0)">近30天</a> 
			<span><font color="red">*</font> </span> 
			<label>统计日期：</label> 
			<span><font color="red">*</font> </span> 
			<input id="startDate" name="startTime" type="text" readonly="readonly" maxlength="20"
				value="${statMoney.startTime}" class="WdateTime required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />&nbsp;&nbsp;
			<span><font color="red">*</font> </span> 
			<input id="endDate" name="endTime" type="text" readonly="readonly"
				maxlength="20" value="${statMoney.endTime}"
				class="WdateTime required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
		</div>
		<br>
		<div class="query_column_space">
			<label>统计精度选择：</label>
			<input type="checkbox" name="groupChildCkAppId" value="1" ${statMoney.groupChildCkAppId == 1 ?'checked="checked"':'' }>统计各子游戏
			<input type="checkbox" name="groupChannel" value="1" ${statMoney.groupChannel == 1 ?'checked="checked"':'' }>统计各渠道
			<input type="checkbox" name="groupChildChannel" value="1" ${statMoney.groupChildChannel == 1 ?'checked="checked"':''}>统计各子渠道
			&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		</div>
	</form:form>
	<table id="statsOnline"
		class="table2excel table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th onclick="$.sortTable.sort('statsOnline',0,compareNumber)" style="cursor: pointer;">序号</th>
				<th onclick="$.sortTable.sort('statsOnline',1,compareNumber)" style="cursor: pointer;">日期</th>
				<th onclick="$.sortTable.sort('statsOnline',2,compareNumber)" style="cursor: pointer;">游戏</th>
				<c:choose>
				   <c:when test="${statMoney.groupChildCkAppId == 1}"> 
				    <th onclick="$.sortTable.sort('statsOnline',3,compareNumber)" style="cursor: pointer;">子游戏id</th>      
				   </c:when>
				</c:choose>
				<c:choose>
				   <c:when test="${statMoney.groupChannel == 1}"> 
				    <th onclick="$.sortTable.sort('statsOnline',${3+statMoney.groupChildCkAppId},compareNumber)" style="cursor: pointer;">渠道</th>      
				   </c:when>
				</c:choose>
				<c:choose>
				   <c:when test="${statMoney.groupChildChannel == 1}"> 
				    <th onclick="$.sortTable.sort('statsOnline',${3+statMoney.groupChildCkAppId+statMoney.groupChannel},compareNumber)" style="cursor: pointer;">子渠道号</th>     
				   </c:when>
				</c:choose>
				<th onclick="$.sortTable.sort('statsOnline',${3+statMoney.groupChildCkAppId+statMoney.groupChannel+statMoney.groupChildChannel},compareNumber)" style="cursor: pointer;">总金额</th>
				<th onclick="$.sortTable.sort('statsOnline',${4+statMoney.groupChildCkAppId+statMoney.groupChannel+statMoney.groupChildChannel},compareNumber)" style="cursor: pointer;">成功金额</th>
				<th onclick="$.sortTable.sort('statsOnline',${5+statMoney.groupChildCkAppId+statMoney.groupChannel+statMoney.groupChildChannel},compareNumber)" style="cursor: pointer;">总充值人数</th>
				<th onclick="$.sortTable.sort('statsOnline',${6+statMoney.groupChildCkAppId+statMoney.groupChannel+statMoney.groupChildChannel},compareNumber)" style="cursor: pointer;">成功充值人数</th>
				<th onclick="$.sortTable.sort('statsOnline',${7+statMoney.groupChildCkAppId+statMoney.groupChannel+statMoney.groupChildChannel},compareNumber)" style="cursor: pointer;">总充值次数</th>
				<th onclick="$.sortTable.sort('statsOnline',${8+statMoney.groupChildCkAppId+statMoney.groupChannel+statMoney.groupChildChannel},compareNumber)" style="cursor: pointer;">成功充值次数</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${data}" var="stat" varStatus="status">
				<tr>
					<td>${status.count}</td>
					<td>${stat.timeframes}</td>
					<td>${fns:getByCkAppName(stat.ckAppId)}(${stat.ckAppId})</td>
					<c:choose>
					   <c:when test="${statMoney.groupChildCkAppId == 1}"> 
					    <td>${fns:getByChildAppName(stat.ckAppId,stat.childCkAppId)}(${stat.childCkAppId}) </td>      
					   </c:when>
					</c:choose>
					<c:choose>
					   <c:when test="${statMoney.groupChannel == 1}"> 
					    <td>${fns:findChannelName(stat.ckChannelId,"")}(${stat.ckChannelId})</td>     
					   </c:when>
					</c:choose>
					<c:choose>
					   <c:when test="${statMoney.groupChildChannel == 1}"> 
					    <td>${stat.childChannelId}</td>     
					   </c:when>
					</c:choose>
					<td>${stat.money/100}</td>
					<td>${stat.successMoney/100}</td>
					<td>${stat.payPeopleNum}</td>
					<td>${stat.paySuccessPeople }</td>
					<td>${stat.payTimes}</td>
					<td>${stat.paySuccessTimes}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>