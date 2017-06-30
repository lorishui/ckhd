<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>网游ltv统计</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		changeAPPID(true);
		$("#ckAppId").focus();
		$("#searchForm").validate({
			submitHandler : function(form) {
				$("#searchForm").attr("action", "${ctx}/stats/LTV");
				form.submit();
				merge();
			},
			errorContainer : "#messageBox"
		});
	});
	function changeAPPID(pageLoad) {
		var id = '${statLTV.childCkAppId}';
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
	<div id="position">LTV统计信息</div>
	<form:form id="searchForm" modelAttribute="statRetention"
		action="${ctx}/stats/LTV" method="post" class="breadcrumb form-search">

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
				value="${statLTV.startTime}" class="WdateTime required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />&nbsp;&nbsp;
			<span><font color="red">*</font> </span> 
			<input id="endDate" name="endTime" type="text" readonly="readonly"
				maxlength="20" value="${statLTV.endTime}"
				class="WdateTime required"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
		</div>
		<br>
		<div class="query_column_space">
			<label>统计精度选择：</label>
			<input type="checkbox" name="groupChildCkAppId" value="1" ${statLTV.groupChildCkAppId == 1 ?'checked="checked"':'' }>统计各子游戏
			<input type="checkbox" name="groupChannel" value="1" ${statLTV.groupChannel == 1 ?'checked="checked"':'' }>统计各渠道
			<input type="checkbox" name="groupChildChannel" value="1" ${statLTV.groupChildChannel == 1 ?'checked="checked"':''}>统计各子渠道
			&nbsp;&nbsp;&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		</div>
	</form:form>
	<table id="statsOnline"
		class="table2excel table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>日期</th>
				<c:choose>
				   <c:when test="${statLTV.groupChildCkAppId == 1}"> 
				    <th class="sort-column childCkAppId">子游戏名称</th>      
				   </c:when>
				</c:choose>
				<c:choose>
				   <c:when test="${statLTV.groupChannel == 1}"> 
				    <th class="sort-column ckChannelId">渠道</th>      
				   </c:when>
				</c:choose>
				<c:choose>
				   <c:when test="${statLTV.groupChildChannel == 1}"> 
				    <th class="sort-column childChannelId">子渠道号</th>     
				   </c:when>
				</c:choose>
				<th>新增数</th>
				<th>活跃数</th>
				<th>活跃新增比</th>
				<th colspan="10">LTV(元)</th>
			</tr>
			<tr>
				<th colspan="${4 + statLTV.groupChildCkAppId + statLTV.groupChannel + statLTV.groupChildChannel}"></th>
				<th>0日</th>
				<th>1日</th>
				<th>2日</th>
				<th>3日</th>
				<th>4日</th>
				<th>5日</th>
				<th>6日</th>
				<th>7日</th>
				<th>14日</th>
				<th>30日</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${data}" var="stat" varStatus="status">
				<tr>
					<td>${stat.regTime}</td>
					<c:choose>
					   <c:when test="${statLTV.groupChildCkAppId == 1}"> 
					    <td>${fns:getByChildAppName(stat.ckAppId,stat.childCkAppId)}(${stat.childCkAppId}) </td>      
					   </c:when>
					</c:choose>
					<c:choose>
					   <c:when test="${statLTV.groupChannel == 1}"> 
					    <td>${fns:findChannelName(stat.ckChannelId,"")}(${stat.ckChannelId})</td>     
					   </c:when>
					</c:choose>
					<c:choose>
					   <c:when test="${statLTV.groupChildChannel == 1}"> 
					    <td>${stat.childChannelId}</td>     
					   </c:when>
					</c:choose>
					<td>${stat.totalNum }</td>
					<td>${stat.actNum }</td>
					<c:choose>
						<c:when test="${stat.totalNum == 0}"><td>--</td></c:when>
						<c:otherwise><td><fmt:formatNumber value="${stat.actNum/stat.totalNum}" pattern="0.00"/></td></c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${stat.d0Num == -1 || stat.totalNum == 0}"><td></td></c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${stat.regTime eq currentDate}">
									<td><span style="color: #B8860B"><fmt:formatNumber value="${stat.d0Num*0.01/(stat.totalNum+0.00001)}" pattern="0.00"/></span></td>
								</c:when>
								<c:otherwise>
									<td><fmt:formatNumber value="${stat.d0Num*0.01/(stat.totalNum+0.00001)}" pattern="0.00"/></td>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${stat.d1Num == -1 || stat.actNum == 0}"><td></td></c:when>
						<c:otherwise><td><fmt:formatNumber value="${stat.d1Num*0.01/(stat.totalNum+0.00001)}" pattern="0.00"/></td></c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${stat.d2Num == -1 || stat.actNum == 0}"><td></td></c:when>
						<c:otherwise><td><fmt:formatNumber value="${stat.d2Num*0.01/(stat.totalNum+0.00001)}" pattern="0.00"/></td></c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${stat.d3Num == -1 || stat.actNum == 0}"><td></td></c:when>
						<c:otherwise><td><fmt:formatNumber value="${stat.d3Num*0.01/(stat.totalNum+0.00001)}" pattern="0.00"/></td></c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${stat.d4Num == -1 || stat.actNum == 0}"><td></td></c:when>
						<c:otherwise><td><fmt:formatNumber value="${stat.d4Num*0.01/(stat.totalNum+0.00001)}" pattern="0.00"/></td></c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${stat.d5Num == -1 || stat.actNum == 0}"><td></td></c:when>
						<c:otherwise><td><fmt:formatNumber value="${stat.d5Num*0.01/(stat.totalNum+0.00001)}" pattern="0.00"/></td></c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${stat.d6Num == -1 || stat.actNum == 0}"><td></td></c:when>
						<c:otherwise><td><fmt:formatNumber value="${stat.d6Num*0.01/(stat.totalNum+0.00001)}" pattern="0.00"/></td></c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${stat.d7Num == -1 || stat.actNum == 0}"><td></td></c:when>
						<c:otherwise><td><fmt:formatNumber value="${stat.d7Num*0.01/(stat.totalNum+0.00001)}" pattern="0.00"/></td></c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${stat.d14Num == -1 || stat.actNum == 0}"><td></td></c:when>
						<c:otherwise><td><fmt:formatNumber value="${stat.d14Num*0.01/(stat.totalNum+0.00001)}" pattern="0.00"/></td></c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${stat.d30Num == -1 || stat.actNum == 0}"><td></td></c:when>
						<c:otherwise><td><fmt:formatNumber value="${stat.d30Num*0.01/(stat.totalNum+0.00001)}" pattern="0.00"/></td></c:otherwise>
					</c:choose>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>