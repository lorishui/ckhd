<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>游戏报表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function reportView(id){
			//$("#reportDetail").attr("src","${ctx}/stats/gamereport/reportview?id="+id);
			window.open("${ctx}/stats/gamereport/reportview?id="+id);
		}
		function selectDateFn(val){
			var startDate = $("#date");
			if(val == 0){
				var day =  GetDateStr(0);
				startDate.val(day);
			}else if(val == 1){
				var day = GetDateStr(-1);
				startDate.val( day);
			}else if(val == 2){
				startDate.val( GetDateStr(-2));
			}
			if(val >= 0){
				$("#searchForm").submit();
			}
		}
		function GetDateStr(AddDayCount) {
		    var dd = new Date();
		    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
		    var y = dd.getFullYear();
		    var m = dd.getMonth()+1;//获取当前月份的日期
		    var d = dd.getDate();
		    m=m<10?"0"+m:m;
		    d=d<10?"0"+d:d;
		    return y+"-"+m+"-"+d;
		}
		function submitTask(){
			//
			$("#addTask").val("true");
			$("#searchForm").submit();
		}
	</script>
</head>
<body>
	<div id="position">游戏报表</div>
		<form:form id="searchForm"  modelAttribute="gameReport" action="${ctx}/stats/gamereport/dailyreport" method="post" class="breadcrumb form-search">
		<div>
			<label>游戏：</label>
			<form:select id="ckAppId" path="ckAppId" class="input-medium" onChange="changeAPPID()">
				<form:options items="${fns:getAPPCkList()}" itemLabel="name" itemValue="ckappId" htmlEscape="false"/>
		    </form:select>
		     <span><font color="red">*</font> </span>
		     <label>时间范围：</label>
			<a href="#" onclick="javascript:selectDateFn(0);void(0)">今天</a>
			<a href="#" onclick="javascript:selectDateFn(1);void(0)">昨天</a>
			<a href="#" onclick="javascript:selectDateFn(2);void(0)">前天</a>
			<span><font color="red">*</font> </span>
	   		<label>统计日期：</label>
			<span><font color="red">*</font> </span>
		    <input id="date" name="date" type="text" readonly="readonly" maxlength="20" value="${gameReport.date}"
		    class="WdateTime required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			&nbsp;&nbsp;
			<span><font color="red">*</font> </span>
			<input id="btnSubmit" class="btn btn-primary" type="submit" style="width:100px" onClick = "return query();" value="查  询"/>
			<input id="addTask" name="addTask" type="hidden" />
	   	</div>

	</form:form>
	<div>
		<c:choose>
			<c:when test="${countTask == 0}">
				<a href="#" onclick="submitTask()">提交统计任务(${gameReport.date})</a>
			</c:when>
		</c:choose>
		<span style="color:red">${message}</span>
	</div>
	<table class="table2excel table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="width:10%;text-align:center">序号</th>
				<th style="width:20%;text-align:center">报表日期</th>
				<th style="width:50%;text-align:center">统计任务提交时间(最近的五条统计信息)</th>
				<th style="width:20%;text-align:center">任务执行状态</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${empty gameReports}">
				<tr>
				<td style="width:10%;text-align:center"></td>
				<td style="width:20%;text-align:center"></td>
				<td style="width:50%;text-align:center">还没有提交该天的统计任务</td>
				<td style="width:20%;text-align:center">-</td></tr>
			</c:if>
			<c:forEach items="${gameReports}" var="gameReport" varStatus="status">
				<tr style="width:50%;text-align:center">
					<td style="width:10%;text-align:center">${status.count}</td>
					<td style="width:20%;text-align:center">${gameReport.date}</td>
					<td style="width:50%;text-align:center"><c:choose>
							<c:when test="${gameReport.status eq '1'}">
								<a href="#" onclick="reportView('${gameReport.id}')"><fmt:formatDate value="${gameReport.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></a></td>
							</c:when>
							<c:otherwise><fmt:formatDate value="${gameReport.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></c:otherwise>
						</c:choose>
					<td style="width:20%;text-align:center"><c:choose>
							<c:when test="${gameReport.status eq '0'}">任务执行中</c:when>
							<c:when test="${gameReport.status eq '1'}">计算完成</c:when>
							<c:otherwise>计算失败</c:otherwise>
						</c:choose></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<!--iframe id="reportDetail" marginwidth="0" marginheight="0" frameborder="0" scrolling="auto"  width="100%" height="400px" /-->
</body>
</html>