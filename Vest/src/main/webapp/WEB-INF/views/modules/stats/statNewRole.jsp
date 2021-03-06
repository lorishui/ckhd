<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>网络订单统计信息</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		changeAPPID(true);
		$("#ckAppId").focus();
		$("#searchForm").validate({
			submitHandler : function(form) {
				$("#searchForm").attr("action", "${ctx}/stats/role/new");
				form.submit();
				merge();
			},
			errorContainer : "#messageBox"
		});
	});
	function changeAPPID(pageLoad) {
		var id = '${statRelate.childCkAppId}';
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
	<form:form id="searchForm" modelAttribute="statRelate"
		action="${ctx}/stats/role/new" method="post" class="breadcrumb form-search">

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
				value="${statRelate.startTime}" class="WdateTime required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />&nbsp;&nbsp;
			<span><font color="red">*</font> </span> 
			<input id="endDate" name="endTime" type="text" readonly="readonly"
				maxlength="20" value="${statRelate.endTime}"
				class="WdateTime required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
		</div>
		<br>
		<div class="query_column_space">
			<label>统计精度选择：</label>
			<input type="checkbox" name="groupChannel" value="1" ${statRelate.groupChannel == 1 ?'checked="checked"':'' }>统计各渠道
			&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		</div>
	</form:form>
	<table id="statsOnline"
		class="table2excel table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th colspan="14">按日期统计</th>
			</tr>
			<tr>
				<th>序号</th>
				<th class="sort-column timeframes">日期</th>
				<th class="sort-column ckAppId">游戏</th>
				<c:choose>
				   <c:when test="${statRelate.groupChannel == 1}"> 
				    <th class="sort-column ckChannelId">渠道</th>      
				   </c:when>
				</c:choose>
				<th class="sort-column newNum">新增数</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${data}" var="stat" varStatus="status">
				<tr>
					<td>${status.count}</td>
					<td>${stat.timeframes}</td>
					<td>${fns:getByCkAppNameByChild(stat.ckAppId,stat.childCkAppId)}(${stat.ckAppId})</td>
					<c:choose>
					   <c:when test="${statRelate.groupChannel == 1}"> 
					    <td>${fns:findChannelName(stat.ckChannelId,"")}(${stat.ckChannelId})</td>     
					   </c:when>
					</c:choose>
					<td>${stat.newNum}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>